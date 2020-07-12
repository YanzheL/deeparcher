# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: com/hitnslab/dnssecurity/deeparcher/api/proto/graph_attributes.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='com/hitnslab/dnssecurity/deeparcher/api/proto/graph_attributes.proto',
  package='com.hitnslab.dnssecurity.deeparcher.api.proto',
  syntax='proto3',
  serialized_options=b'\n<com.hitnslab.dnssecurity.deeparcher.api.proto.generated.javaB\024GraphAttributesProtoH\001\370\001\001',
  create_key=_descriptor._internal_create_key,
  serialized_pb=b'\nDcom/hitnslab/dnssecurity/deeparcher/api/proto/graph_attributes.proto\x12-com.hitnslab.dnssecurity.deeparcher.api.proto\"/\n\rComponentAttr\x12\n\n\x02id\x18\x02 \x01(\r\x12\x12\n\ncomponents\x18\x03 \x03(\r\"\xb2\x01\n\x14GraphElementsAttrMap\x12[\n\x04\x64\x61ta\x18\x01 \x03(\x0b\x32M.com.hitnslab.dnssecurity.deeparcher.api.proto.GraphElementsAttrMap.DataEntry\x12\x10\n\x08type_url\x18\x02 \x01(\t\x1a+\n\tDataEntry\x12\x0b\n\x03key\x18\x01 \x01(\r\x12\r\n\x05value\x18\x02 \x01(\x0c:\x02\x38\x01\x42Y\n<com.hitnslab.dnssecurity.deeparcher.api.proto.generated.javaB\x14GraphAttributesProtoH\x01\xf8\x01\x01\x62\x06proto3'
)




_COMPONENTATTR = _descriptor.Descriptor(
  name='ComponentAttr',
  full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.ComponentAttr',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.ComponentAttr.id', index=0,
      number=2, type=13, cpp_type=3, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='components', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.ComponentAttr.components', index=1,
      number=3, type=13, cpp_type=3, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
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
  serialized_start=119,
  serialized_end=166,
)


_GRAPHELEMENTSATTRMAP_DATAENTRY = _descriptor.Descriptor(
  name='DataEntry',
  full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.GraphElementsAttrMap.DataEntry',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.GraphElementsAttrMap.DataEntry.key', index=0,
      number=1, type=13, cpp_type=3, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='value', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.GraphElementsAttrMap.DataEntry.value', index=1,
      number=2, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value=b"",
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=b'8\001',
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=304,
  serialized_end=347,
)

_GRAPHELEMENTSATTRMAP = _descriptor.Descriptor(
  name='GraphElementsAttrMap',
  full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.GraphElementsAttrMap',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='data', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.GraphElementsAttrMap.data', index=0,
      number=1, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='type_url', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.GraphElementsAttrMap.type_url', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[_GRAPHELEMENTSATTRMAP_DATAENTRY, ],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=169,
  serialized_end=347,
)

_GRAPHELEMENTSATTRMAP_DATAENTRY.containing_type = _GRAPHELEMENTSATTRMAP
_GRAPHELEMENTSATTRMAP.fields_by_name['data'].message_type = _GRAPHELEMENTSATTRMAP_DATAENTRY
DESCRIPTOR.message_types_by_name['ComponentAttr'] = _COMPONENTATTR
DESCRIPTOR.message_types_by_name['GraphElementsAttrMap'] = _GRAPHELEMENTSATTRMAP
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

ComponentAttr = _reflection.GeneratedProtocolMessageType('ComponentAttr', (_message.Message,), {
  'DESCRIPTOR' : _COMPONENTATTR,
  '__module__' : 'com.hitnslab.dnssecurity.deeparcher.api.proto.graph_attributes_pb2'
  # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.ComponentAttr)
  })
_sym_db.RegisterMessage(ComponentAttr)

GraphElementsAttrMap = _reflection.GeneratedProtocolMessageType('GraphElementsAttrMap', (_message.Message,), {

  'DataEntry' : _reflection.GeneratedProtocolMessageType('DataEntry', (_message.Message,), {
    'DESCRIPTOR' : _GRAPHELEMENTSATTRMAP_DATAENTRY,
    '__module__' : 'com.hitnslab.dnssecurity.deeparcher.api.proto.graph_attributes_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphElementsAttrMap.DataEntry)
    })
  ,
  'DESCRIPTOR' : _GRAPHELEMENTSATTRMAP,
  '__module__' : 'com.hitnslab.dnssecurity.deeparcher.api.proto.graph_attributes_pb2'
  # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphElementsAttrMap)
  })
_sym_db.RegisterMessage(GraphElementsAttrMap)
_sym_db.RegisterMessage(GraphElementsAttrMap.DataEntry)


DESCRIPTOR._options = None
_GRAPHELEMENTSATTRMAP_DATAENTRY._options = None
# @@protoc_insertion_point(module_scope)
