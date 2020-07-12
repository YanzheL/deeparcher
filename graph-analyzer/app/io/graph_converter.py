from __future__ import annotations

from typing import TYPE_CHECKING, Dict, Tuple

if TYPE_CHECKING:
    import cugraph
    from app.struct.graph import Graph
    from app.struct.proto.graph_pb2 import Graph as GraphProto
    from app.struct.proto.graph_attributes_pb2 import GraphElementsAttrMap
    from scipy.sparse import spmatrix

from app.util.logger_router import LoggerRouter

DEFAULT_LOGGER = LoggerRouter().get_logger(__name__)


def build_cugraph(adj: spmatrix, symmetrized=True, weight_transform=None) -> cugraph.Graph:
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


def to_pb(graph: Graph) -> GraphProto:
    from app.struct.proto.graph_pb2 import Graph as GraphProto
    from app.struct.proto.graph_attributes_pb2 import ComponentAttr as ComponentAttrProto
    proto = GraphProto()
    proto.id = graph.id
    proto.nodes = graph.nodes
    proto.edges = graph.edges
    adj, adj_type = matrix_to_proto(graph.adj)
    getattr(proto, adj_type.lower()).CopyFrom(adj)
    proto.directed = graph.directed
    proto.connected = graph.connected
    proto.unweighted = graph.unweighted
    proto.parent_id = graph.parent_id
    proto.node_id_remap.extend(graph.node_id_remap)
    for cc in graph.component_attrs:
        cc_proto = ComponentAttrProto()
        cc_proto.id = cc.id
        cc_proto.components.extend(cc.components)
        proto.component_attrs.append(cc_proto)
    _graph_elements_attrs_to_proto(graph.node_attrs, proto.node_attrs)
    _graph_elements_attrs_to_proto(graph.edge_attrs, proto.edge_attrs)
    return proto


def from_pb(proto: GraphProto) -> Graph:
    from scipy.sparse import coo_matrix, csr_matrix, csc_matrix
    from app.struct.graph import Graph
    import numpy as np
    adj = None
    if proto.HasField('coo'):
        adj_proto = proto.coo
        adj = coo_matrix((adj_proto.values, (adj_proto.rows, adj_proto.cols)), shape=adj_proto.dims, dtype=np.float32)
    elif proto.HasField('csr'):
        adj_proto = proto.csr
        adj = csr_matrix((adj_proto.values, adj_proto.col_indices, adj_proto.row_offsets), shape=adj_proto.dims,
                         dtype=np.float32)
    elif proto.HasField('csc'):
        adj_proto = proto.csr
        adj = csc_matrix((adj_proto.values, adj_proto.row_indices, adj_proto.col_offsets), shape=adj_proto.dims,
                         dtype=np.float32)

    # cc = [ComponentAttr(cc_proto.id, np.array(cc_proto.components, dtype=np.int32))
    #       for cc_proto in proto.component_attrs]

    node_attrs = {}
    edge_attrs = {}
    _graph_elements_attrs_from_proto(proto.node_attrs, node_attrs)
    _graph_elements_attrs_from_proto(proto.edge_attrs, edge_attrs)
    return Graph(
        id=proto.id,
        nodes=proto.nodes,
        edges=proto.edges,
        adj=adj,
        directed=proto.directed,
        connected=proto.connected,
        unweighted=proto.unweighted,
        parent_id=proto.parent_id,
        node_id_remap=np.array(proto.node_id_remap, dtype=np.int32),
        # component_attrs=cc,
        node_attrs=node_attrs,
        edge_attrs=edge_attrs
    )


def matrix_to_proto(m):
    from scipy.sparse import coo_matrix, csr_matrix, csc_matrix
    import numpy as np
    from app.struct.proto.matrix_pb2 import CooMatFloat, CscMatFloat, CsrMatFloat, DenseMatFloat
    proto = None
    type = None
    if isinstance(m, coo_matrix):
        proto = CooMatFloat()
        type = 'COO'
        proto.rows.extend(m.row)
        proto.cols.extend(m.col)
        proto.values.extend(m.data)
    elif isinstance(m, csr_matrix):
        proto = CsrMatFloat()
        type = 'CSR'
        proto.row_offsets.extend(m.indptr)
        proto.col_indices.extend(m.indices)
        proto.values.extend(m.data)
    elif isinstance(m, csc_matrix):
        proto = CscMatFloat()
        type = 'CSC'
        proto.col_offsets.extend(m.indptr)
        proto.row_indices.extend(m.indices)
        proto.values.extend(m.data)
    elif isinstance(m, np.ndarray):
        proto = DenseMatFloat()
        type = 'DENSE'
        proto.values.extend(m.flatten())
    if proto is None:
        raise ValueError("Invalid src matrix type.")
    proto.dims.extend(m.shape)
    return proto, type


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


def _get_proper_proto_wrapper(value):
    from google.protobuf.wrappers_pb2 import StringValue, FloatValue, Int32Value
    import numpy as np
    if isinstance(value, str):
        clz = StringValue
    elif isinstance(value, (np.float32, float)):
        clz = FloatValue
    elif isinstance(value, (np.int32, int)):
        clz = Int32Value
    else:
        raise ValueError("Unsupported value type {}".format(type(value)))
    return clz


def _get_wrapped_value(data, type_url):
    from google.protobuf.any_pb2 import Any
    from google.protobuf.wrappers_pb2 import StringValue, FloatValue, Int32Value
    any = Any()
    any.value = data
    any.type_url = type_url
    v = None
    if any.Is(StringValue.DESCRIPTOR):
        v = StringValue()
    elif any.Is(FloatValue.DESCRIPTOR):
        v = FloatValue()
    elif any.Is(Int32Value.DESCRIPTOR):
        v = Int32Value()
    any.Unpack(v)
    return v.value


def _graph_elements_attrs_from_proto(attrs_proto, attrs: Dict[str, Dict[int, object]]):
    for name in attrs_proto:
        attr_map = attrs_proto[name]
        type_url = attr_map.type_url
        mapped = attr_map.data
        for i in mapped:
            data_dict = attrs.get(name, {})
            data_dict[i] = _get_wrapped_value(mapped[i], type_url)
            attrs[name] = data_dict


def _graph_elements_attrs_to_proto(attrs: Dict[str, Dict[int, object]], attrs_proto):
    from google.protobuf.any_pb2 import Any
    for name, data in attrs.items():
        attr_map: GraphElementsAttrMap = attrs_proto[name]
        # Assume that all values in data.values() are same type.
        sample = None
        clz = None
        for i, value in data.items():
            if sample is None:
                sample = value
                clz = _get_proper_proto_wrapper(sample)
                sample_wrapped = clz()
                sample_wrapped.value = sample
                any = Any()
                any.Pack(sample_wrapped)
                attr_map.type_url = any.type_url
            v = clz()
            v.value = value
            attr_map.data[i] = v.SerializeToString()


if __name__ == '__main__':
    from app.struct.proto.graph_pb2 import Graph as GraphProto

    with open('data/graph.pb', 'rb') as f:
        proto = GraphProto()
        proto.ParseFromString(f.read())
        graph = from_pb(proto)
