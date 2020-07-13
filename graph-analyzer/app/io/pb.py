from __future__ import annotations

from typing import TYPE_CHECKING, Dict

if TYPE_CHECKING:
    from app.struct.graph import Graph
    from app.struct.proto.graph_pb2 import Graph as GraphProto
    from app.struct.proto.graph_attributes_pb2 import GraphElementsAttrMap


def to_pb(graph: Graph, path: str):
    with open(path, 'wb') as f:
        f.write(to_pb_object(graph).SerializeToString())


def to_pb_object(graph: Graph) -> GraphProto:
    from app.struct.proto.graph_pb2 import Graph as GraphProto
    from app.struct.proto.graph_attributes_pb2 import ComponentAttr as ComponentAttrProto
    proto = GraphProto()
    proto.id = graph.id
    proto.nodes = graph.nodes
    proto.edges = graph.edges
    adj, adj_type = _matrix_to_proto(graph.adj)
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


def from_pb(path: str) -> Graph:
    from app.struct.proto.graph_pb2 import Graph as GraphProto
    with open(path, 'rb') as f:
        proto = GraphProto()
        proto.ParseFromString(f.read())
        return from_pb_object(proto)


def from_pb_object(proto: GraphProto) -> Graph:
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


def _matrix_to_proto(m):
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
