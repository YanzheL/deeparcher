from typing import *

import numpy as np
from cugraph.components.connectivity import weakly_connected_components
from scipy.sparse import csr_matrix

from app.analyzer.interface import GraphAnalyzer
from app.io.graph_converter import build_cugraph
from app.struct.graph import Graph
from app.struct.graph_attributes import ComponentAttr


class ConnectedComponentsAnalyzer(GraphAnalyzer):

    def analyze(self, graph: Graph, ctx: dict, **runtime_configs) -> Union[Graph, List[Graph], None]:
        if graph.connected or len(graph.component_attrs) == 1:
            self.logger.warn('Current graph is connected, now skipped.')
            graph.connected = True
            return graph
        if len(graph.component_attrs) == 0:
            self.logger.info('Computing weakly connected components...')
            if 'cugraph' not in graph.meta:
                graph.meta['cugraph'] = build_cugraph(graph.adj)
            cug = graph.meta['cugraph']
            cc_df = weakly_connected_components(cug)['labels']
            n_components = cc_df.max()
            self.logger.info('Got {} weakly connected components.'.format(n_components))
            if n_components == 1:
                graph.connected = True
                return graph
            else:
                for i in range(1, cc_df.max() + 1):
                    component = cc_df.query("labels == {}".format(i))['vertices'].to_array()
                    component_attr = ComponentAttr(i - 1, component)
                    graph.component_attrs.append(component_attr)
        else:
            self.logger.info('Found {} precomputed weakly connected components in graph attributes.'.format(
                len(graph.component_attrs)))
        return [self.extract_subgraph(graph, component_attr) for component_attr in graph.component_attrs]

    @staticmethod
    def extract_subgraph(graph: Graph, component_attr: ComponentAttr) -> Graph:
        component = component_attr.components
        adj: csr_matrix = graph.adj.tocsr()[component, component]
        return Graph(
            id=component_attr.component_id,
            nodes=component.size,
            edges=adj.count_nonzero(),
            adj=adj,
            directed=graph.directed,
            connected=True,
            unweighted=graph.unweighted,
            parent_id=graph.id,
            node_id_remap=component,
            node_attrs=ConnectedComponentsAnalyzer.extract_sub_attr_map(graph.node_attrs, component),
            edge_attrs=ConnectedComponentsAnalyzer.extract_sub_attr_map(graph.edge_attrs, component)
        )

    @staticmethod
    def extract_sub_attr_map(attrs: Dict[str, Dict[int, object]], component: np.ndarray) \
            -> Dict[str, Dict[int, object]]:
        ret = {}
        for name, data in attrs.items():
            ids = data.keys()
            parent_attr_ids, component_attr_ids, _ = np.intersect1d(component, ids, assume_unique=True,
                                                                    return_indices=True)
            values = [data[i] for i in parent_attr_ids]
            sub_attr_map = {k: v for k, v in zip(component_attr_ids, values)}
            if len(sub_attr_map) > 0:
                ret[name] = sub_attr_map
        return ret
