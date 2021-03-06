# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: com/hitnslab/dnssecurity/deeparcher/api/proto/known_malicious_domain.proto

import sys

_b = sys.version_info[0] < 3 and (lambda x: x) or (lambda x: x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database

# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()

DESCRIPTOR = _descriptor.FileDescriptor(
    name='com/hitnslab/dnssecurity/deeparcher/api/proto/known_malicious_domain.proto',
    package='com.hitnslab.dnssecurity.deeparcher.api.proto',
    syntax='proto3',
    serialized_options=_b(
        '\n<com.hitnslab.dnssecurity.deeparcher.api.proto.generated.javaB\031KnownMaliciousDomainProtoH\001\370\001\001'),
    serialized_pb=_b(
        '\nJcom/hitnslab/dnssecurity/deeparcher/api/proto/known_malicious_domain.proto\x12-com.hitnslab.dnssecurity.deeparcher.api.proto\"\xc7\x03\n\x14KnownMaliciousDomain\x12\x11\n\ttimestamp\x18\x01 \x01(\x03\x12\x0e\n\x06source\x18\x02 \x01(\t\x12\x0e\n\x06method\x18\x03 \x01(\t\x12\x0c\n\x04\x66qdn\x18\x04 \x01(\t\x12\x0e\n\x06\x64omain\x18\x05 \x01(\t\x12\x0b\n\x03url\x18\x06 \x01(\t\x12\x12\n\ncreated_at\x18\x07 \x01(\x03\x12\x12\n\nupdated_at\x18\x08 \x01(\x03\x12\x15\n\rservice_title\x18\x10 \x01(\t\x12\x12\n\nipv4_addrs\x18\x11 \x01(\x0c\x12\x12\n\nipv6_addrs\x18\x12 \x01(\x0c\x12\x0e\n\x06\x63names\x18\x13 \x03(\t\x12\x0c\n\x04type\x18\x14 \x01(\t\x12\x0b\n\x03\x61sn\x18\x15 \x01(\x05\x12\x11\n\tregistrar\x18\x16 \x01(\t\x12\x0e\n\x06remark\x18\x17 \x01(\t\x12Z\n\x06status\x18\x18 \x01(\x0e\x32J.com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.Status\"@\n\x06Status\x12\x0b\n\x07UNKNOWN\x10\x00\x12\n\n\x06\x41\x43TIVE\x10\x01\x12\x08\n\x04HOLD\x10\x02\x12\x08\n\x04\x44\x45\x41\x44\x10\x03\x12\t\n\x05OTHER\x10\x04\x42^\n<com.hitnslab.dnssecurity.deeparcher.api.proto.generated.javaB\x19KnownMaliciousDomainProtoH\x01\xf8\x01\x01\x62\x06proto3')
)

_KNOWNMALICIOUSDOMAIN_STATUS = _descriptor.EnumDescriptor(
    name='Status',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.Status',
    filename=None,
    file=DESCRIPTOR,
    values=[
        _descriptor.EnumValueDescriptor(
            name='UNKNOWN', index=0, number=0,
            serialized_options=None,
            type=None),
        _descriptor.EnumValueDescriptor(
            name='ACTIVE', index=1, number=1,
            serialized_options=None,
            type=None),
        _descriptor.EnumValueDescriptor(
            name='HOLD', index=2, number=2,
            serialized_options=None,
            type=None),
        _descriptor.EnumValueDescriptor(
            name='DEAD', index=3, number=3,
            serialized_options=None,
            type=None),
        _descriptor.EnumValueDescriptor(
            name='OTHER', index=4, number=4,
            serialized_options=None,
            type=None),
    ],
    containing_type=None,
    serialized_options=None,
    serialized_start=517,
    serialized_end=581,
)
_sym_db.RegisterEnumDescriptor(_KNOWNMALICIOUSDOMAIN_STATUS)

_KNOWNMALICIOUSDOMAIN = _descriptor.Descriptor(
    name='KnownMaliciousDomain',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain',
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    fields=[
        _descriptor.FieldDescriptor(
            name='timestamp', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.timestamp',
            index=0,
            number=1, type=3, cpp_type=2, label=1,
            has_default_value=False, default_value=0,
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='source', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.source',
            index=1,
            number=2, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=_b("").decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='method', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.method',
            index=2,
            number=3, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=_b("").decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='fqdn', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.fqdn', index=3,
            number=4, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=_b("").decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='domain', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.domain',
            index=4,
            number=5, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=_b("").decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='url', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.url', index=5,
            number=6, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=_b("").decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='created_at',
            full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.created_at', index=6,
            number=7, type=3, cpp_type=2, label=1,
            has_default_value=False, default_value=0,
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='updated_at',
            full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.updated_at', index=7,
            number=8, type=3, cpp_type=2, label=1,
            has_default_value=False, default_value=0,
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='service_title',
            full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.service_title', index=8,
            number=16, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=_b("").decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='ipv4_addrs',
            full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.ipv4_addrs', index=9,
            number=17, type=12, cpp_type=9, label=1,
            has_default_value=False, default_value=_b(""),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='ipv6_addrs',
            full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.ipv6_addrs', index=10,
            number=18, type=12, cpp_type=9, label=1,
            has_default_value=False, default_value=_b(""),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='cnames', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.cnames',
            index=11,
            number=19, type=9, cpp_type=9, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='type', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.type', index=12,
            number=20, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=_b("").decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='asn', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.asn', index=13,
            number=21, type=5, cpp_type=1, label=1,
            has_default_value=False, default_value=0,
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='registrar', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.registrar',
            index=14,
            number=22, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=_b("").decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='remark', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.remark',
            index=15,
            number=23, type=9, cpp_type=9, label=1,
            has_default_value=False, default_value=_b("").decode('utf-8'),
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='status', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain.status',
            index=16,
            number=24, type=14, cpp_type=8, label=1,
            has_default_value=False, default_value=0,
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
    ],
    extensions=[
    ],
    nested_types=[],
    enum_types=[
        _KNOWNMALICIOUSDOMAIN_STATUS,
    ],
    serialized_options=None,
    is_extendable=False,
    syntax='proto3',
    extension_ranges=[],
    oneofs=[
    ],
    serialized_start=126,
    serialized_end=581,
)

_KNOWNMALICIOUSDOMAIN.fields_by_name['status'].enum_type = _KNOWNMALICIOUSDOMAIN_STATUS
_KNOWNMALICIOUSDOMAIN_STATUS.containing_type = _KNOWNMALICIOUSDOMAIN
DESCRIPTOR.message_types_by_name['KnownMaliciousDomain'] = _KNOWNMALICIOUSDOMAIN
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

KnownMaliciousDomain = _reflection.GeneratedProtocolMessageType('KnownMaliciousDomain', (_message.Message,), {
    'DESCRIPTOR': _KNOWNMALICIOUSDOMAIN,
    '__module__': 'com.hitnslab.dnssecurity.deeparcher.api.proto.known_malicious_domain_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.KnownMaliciousDomain)
})
_sym_db.RegisterMessage(KnownMaliciousDomain)

DESCRIPTOR._options = None
# @@protoc_insertion_point(module_scope)
