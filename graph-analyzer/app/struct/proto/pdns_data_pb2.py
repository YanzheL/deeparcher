# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: com/hitnslab/dnssecurity/deeparcher/api/proto/pdns_data.proto

import sys

_b = sys.version_info[0] < 3 and (lambda x: x) or (lambda x: x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database

# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()

DESCRIPTOR = _descriptor.FileDescriptor(
    name='com/hitnslab/dnssecurity/deeparcher/api/proto/pdns_data.proto',
    package='com.hitnslab.dnssecurity.deeparcher.api.proto',
    syntax='proto3',
    serialized_options=_b(
        '\n<com.hitnslab.dnssecurity.deeparcher.api.proto.generated.javaB\rPDnsDataProtoH\001\370\001\001'),
    serialized_pb=_b(
        '\n=com/hitnslab/dnssecurity/deeparcher/api/proto/pdns_data.proto\x12-com.hitnslab.dnssecurity.deeparcher.api.proto\"\xa9\x01\n\x08PDnsData\x12\x0c\n\x04\x66qdn\x18\x01 \x01(\t\x12\x0e\n\x06\x64omain\x18\x02 \x01(\t\x12\x11\n\tclient_ip\x18\x03 \x01(\x0c\x12\x0e\n\x06q_time\x18\x04 \x01(\x03\x12\x0e\n\x06q_type\x18\x05 \x01(\x05\x12\x0e\n\x06r_code\x18\x06 \x01(\x05\x12\x14\n\x0cr_ipv4_addrs\x18\x07 \x01(\x0c\x12\x14\n\x0cr_ipv6_addrs\x18\x08 \x01(\x0c\x12\x10\n\x08r_cnames\x18\t \x03(\tBR\n<com.hitnslab.dnssecurity.deeparcher.api.proto.generated.javaB\rPDnsDataProtoH\x01\xf8\x01\x01\x62\x06proto3')
)

_PDNSDATA = _descriptor.Descriptor(
    name='PDnsData',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsData',
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    fields=[
        _descriptor.FieldDescriptor(
            name='fqdn', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsData.fqdn', index=0,
            number=1, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=_b("").decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='domain', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsData.domain', index=1,
            number=2, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=_b("").decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='client_ip', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsData.client_ip', index=2,
            number=3, type=12, cpp_type=9, label=1,
            has_default_value=False, default_value=_b(""),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='q_time', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsData.q_time', index=3,
            number=4, type=3, cpp_type=2, label=1,
            has_default_value=False, default_value=0,
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='q_type', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsData.q_type', index=4,
            number=5, type=5, cpp_type=1, label=1,
            has_default_value=False, default_value=0,
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='r_code', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsData.r_code', index=5,
            number=6, type=5, cpp_type=1, label=1,
            has_default_value=False, default_value=0,
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='r_ipv4_addrs', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsData.r_ipv4_addrs',
            index=6,
            number=7, type=12, cpp_type=9, label=1,
            has_default_value=False, default_value=_b(""),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='r_ipv6_addrs', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsData.r_ipv6_addrs',
            index=7,
            number=8, type=12, cpp_type=9, label=1,
            has_default_value=False, default_value=_b(""),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='r_cnames', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsData.r_cnames', index=8,
            number=9, type=9, cpp_type=9, label=3,
            has_default_value=False, default_value=[],
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
    serialized_start=113,
    serialized_end=282,
)

DESCRIPTOR.message_types_by_name['PDnsData'] = _PDNSDATA
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

PDnsData = _reflection.GeneratedProtocolMessageType('PDnsData', (_message.Message,), {
    'DESCRIPTOR': _PDNSDATA,
    '__module__': 'com.hitnslab.dnssecurity.deeparcher.api.proto.pdns_data_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsData)
})
_sym_db.RegisterMessage(PDnsData)

DESCRIPTOR._options = None
# @@protoc_insertion_point(module_scope)
