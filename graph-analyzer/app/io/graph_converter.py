from __future__ import annotations

from typing import TYPE_CHECKING, Dict, Tuple

if TYPE_CHECKING:
    from app.struct.graph import Graph

from app.util.logger_router import LoggerRouter

DEFAULT_LOGGER = LoggerRouter().get_logger(__name__)


def build_cugraph(adj: 'spmatrix', symmetrized=True, weight_transform=None) -> 'cugraph.Graph':
    import cudf
    import cugraph
    adj = adj.tocoo()
    df = cudf.DataFrame()
    df['src'] = adj.row
    df['dst'] = adj.col
    df['weights'] = adj.data
    if weight_transform is not None:
        df['weights'] = df['weights'].applymap(weight_transform)
    g = cugraph.Graph(symmetrized=symmetrized)
    g.from_cudf_edgelist(df, 'src', 'dst', 'weights')
    # adj = self.graph.adj.tocsr()
    # offsets = cudf.Series(adj.indptr)
    # indices = cudf.Series(adj.indices)
    # g.from_cudf_adjlist(offsets, indices, None)
    return g


def from_dot(path: str, extract_node_attrs=False, extract_edge_attrs=False, logger=DEFAULT_LOGGER) -> Graph:
    import pygraphviz as pgv
    from scipy.sparse import coo_matrix
    import numpy as np
    from app.struct.graph import Graph
    import gc
    logger.debug("Loading dot graph from <{}>".format(path))
    g = pgv.AGraph(filename=path)
    gc.collect()
    number_of_nodes = g.number_of_nodes()
    number_of_edges = g.number_of_edges()
    logger.debug("Converting a dot graph with {} nodes and {} edges...".format(number_of_nodes, number_of_edges))
    row = np.zeros((number_of_edges,), dtype=np.int32)
    col = np.zeros((number_of_edges,), dtype=np.int32)
    data = np.ones((number_of_edges,), dtype=np.float32)
    unweighted = True
    node_id_remap = np.array([int(n) for n in g.nodes_iter()], dtype=np.int32)
    node_id_remap.sort()
    node_id_reverse_map = {i: idx for idx, i in enumerate(node_id_remap)}
    logger.debug("Converted node_id_remaps.")
    edge_attrs_unordered: Dict[str, Dict[Tuple[int, int], object]] = {}
    for idx, edge in enumerate(g.edges_iter()):
        i, j = edge
        attrs = g.get_edge(i, j).attr
        vi = node_id_reverse_map[int(i)]
        vj = node_id_reverse_map[int(j)]
        row[idx] = vi
        col[idx] = vj
        for k, v in attrs.iteritems():
            if k == 'weight':
                data[idx] = float(v)
                unweighted = False
                continue
            try:
                v = float(v)
            except Exception:
                pass
            if extract_edge_attrs:
                d = edge_attrs_unordered.get(k, {})
                d[(vi, vj)] = v
                edge_attrs_unordered[k] = d
    logger.debug("Converted graph topology.")
    edge_attrs: Dict[str, Dict[int, object]] = {}
    if extract_edge_attrs:
        all_edge_pairs = np.unique(np.array([row, col]).T, axis=0)
        logger.debug("Got all {} edge pairs.".format(all_edge_pairs.size))
        for name, value_dict in edge_attrs_unordered.items():
            attrs_map: Dict[int, object] = {}
            for pair, value in value_dict.items():
                edge_id = (all_edge_pairs == pair).all(axis=1).nonzero()[0][0]
                # edge_id = np.argwhere(np.all(all_edge_pairs == pair, axis=1))[0][0]
                attrs_map[edge_id] = value
            edge_attrs[name] = attrs_map
        logger.debug("Converted {} edge attributes.".format(len(edge_attrs)))
    node_attrs: Dict[str, Dict[int, object]] = {}
    if extract_node_attrs:
        for i in g.nodes_iter():
            attrs = g.get_node(i).attr
            for k, v in attrs.iteritems():
                try:
                    v = float(v)
                except Exception:
                    pass
                value_dict = node_attrs.get(k, {})
                value_dict[node_id_reverse_map[int(i)]] = v
                node_attrs[k] = value_dict
        logger.debug("Converted {} node attributes.".format(len(node_attrs)))
    adj = coo_matrix((data, (row, col)), shape=(number_of_nodes, number_of_nodes), dtype=np.float32).tocsr()
    return Graph(
        id=0,
        nodes=number_of_nodes,
        edges=g.number_of_edges(),
        adj=adj,
        directed=g.is_directed(),
        connected=False,
        unweighted=unweighted,
        node_id_remap=node_id_remap,
        node_attrs=node_attrs,
        edge_attrs=edge_attrs
    )
