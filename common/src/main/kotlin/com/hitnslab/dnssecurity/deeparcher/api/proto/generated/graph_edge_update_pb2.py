# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: com/hitnslab/dnssecurity/deeparcher/api/proto/graph_edge_update.proto

from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database

# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()

DESCRIPTOR = _descriptor.FileDescriptor(
    name='com/hitnslab/dnssecurity/deeparcher/api/proto/graph_edge_update.proto',
    package='com.hitnslab.dnssecurity.deeparcher.api.proto',
    syntax='proto3',
    serialized_options=b'\n7com.hitnslab.dnssecurity.deeparcher.api.proto.generatedB\031GraphAssocEdgeUpdateProtoH\001\370\001\001',
    serialized_pb=b'\nEcom/hitnslab/dnssecurity/deeparcher/api/proto/graph_edge_update.proto\x12-com.hitnslab.dnssecurity.deeparcher.api.proto\"M\n\x14GraphAssocEdgeUpdate\x12\r\n\x05\x66qdn1\x18\x01 \x01(\t\x12\r\n\x05\x66qdn2\x18\x02 \x01(\t\x12\x17\n\x0fn_shared_fields\x18\x03 \x01(\x05\x42Y\n7com.hitnslab.dnssecurity.deeparcher.api.proto.generatedB\x19GraphAssocEdgeUpdateProtoH\x01\xf8\x01\x01\x62\x06proto3'
)

_GRAPHASSOCEDGEUPDATE = _descriptor.Descriptor(
    name='GraphAssocEdgeUpdate',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate',
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    fields=[
        _descriptor.FieldDescriptor(
            name='fqdn1', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate.fqdn1', index=0,
            number=1, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=b"".decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='fqdn2', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate.fqdn2', index=1,
            number=2, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=b"".decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='n_shared_fields',
            full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate.n_shared_fields', index=2,
            number=3, type=5, cpp_type=1, label=1,
            has_default_value=False, default_value=0,
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
    ],
    extensions=[
    ],
    nested_types=[],
    enum_types=[
    ],
    serialized_options=None,
    is_extendable=False,
    syntax='proto3',
    extension_ranges=[],
    oneofs=[
    ],
    serialized_start=120,
    serialized_end=197,
)

DESCRIPTOR.message_types_by_name['GraphAssocEdgeUpdate'] = _GRAPHASSOCEDGEUPDATE
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

GraphAssocEdgeUpdate = _reflection.GeneratedProtocolMessageType('GraphAssocEdgeUpdate', (_message.Message,), {
    'DESCRIPTOR': _GRAPHASSOCEDGEUPDATE,
    '__module__': 'com.hitnslab.dnssecurity.deeparcher.api.proto.graph_edge_update_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate)
})
_sym_db.RegisterMessage(GraphAssocEdgeUpdate)

DESCRIPTOR._options = None
# @@protoc_insertion_point(module_scope)
