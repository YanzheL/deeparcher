# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: com/hitnslab/dnssecurity/deeparcher/api/proto/matrix.proto

import sys

_b = sys.version_info[0] < 3 and (lambda x: x) or (lambda x: x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database

# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()

DESCRIPTOR = _descriptor.FileDescriptor(
    name='com/hitnslab/dnssecurity/deeparcher/api/proto/matrix.proto',
    package='com.hitnslab.dnssecurity.deeparcher.api.proto',
    syntax='proto3',
    serialized_options=_b(
        '\n<com.hitnslab.dnssecurity.deeparcher.api.proto.generated.javaB\013MatrixProtoH\001\370\001\001'),
    serialized_pb=_b(
        '\n:com/hitnslab/dnssecurity/deeparcher/api/proto/matrix.proto\x12-com.hitnslab.dnssecurity.deeparcher.api.proto\"-\n\rDenseMatFloat\x12\x0c\n\x04\x64ims\x18\x01 \x03(\r\x12\x0e\n\x06values\x18\n \x03(\x02\"G\n\x0b\x43ooMatFloat\x12\x0c\n\x04\x64ims\x18\x01 \x03(\r\x12\x0c\n\x04rows\x18\x02 \x03(\r\x12\x0c\n\x04\x63ols\x18\x03 \x03(\r\x12\x0e\n\x06values\x18\n \x03(\x02\"U\n\x0b\x43srMatFloat\x12\x0c\n\x04\x64ims\x18\x01 \x03(\r\x12\x13\n\x0brow_offsets\x18\x02 \x03(\r\x12\x13\n\x0b\x63ol_indices\x18\x03 \x03(\r\x12\x0e\n\x06values\x18\n \x03(\x02\"U\n\x0b\x43scMatFloat\x12\x0c\n\x04\x64ims\x18\x01 \x03(\r\x12\x13\n\x0b\x63ol_offsets\x18\x02 \x03(\r\x12\x13\n\x0brow_indices\x18\x03 \x03(\r\x12\x0e\n\x06values\x18\n \x03(\x02\"-\n\rDenseMatInt32\x12\x0c\n\x04\x64ims\x18\x01 \x03(\r\x12\x0e\n\x06values\x18\n \x03(\x05\"G\n\x0b\x43ooMatInt32\x12\x0c\n\x04\x64ims\x18\x01 \x03(\r\x12\x0c\n\x04rows\x18\x02 \x03(\r\x12\x0c\n\x04\x63ols\x18\x03 \x03(\r\x12\x0e\n\x06values\x18\n \x03(\x05\"U\n\x0b\x43srMatInt32\x12\x0c\n\x04\x64ims\x18\x01 \x03(\r\x12\x13\n\x0brow_offsets\x18\x02 \x03(\r\x12\x13\n\x0b\x63ol_indices\x18\x03 \x03(\r\x12\x0e\n\x06values\x18\n \x03(\x05\"U\n\x0b\x43scMatInt32\x12\x0c\n\x04\x64ims\x18\x01 \x03(\r\x12\x13\n\x0b\x63ol_offsets\x18\x02 \x03(\r\x12\x13\n\x0brow_indices\x18\x03 \x03(\r\x12\x0e\n\x06values\x18\n \x03(\x05\x42P\n<com.hitnslab.dnssecurity.deeparcher.api.proto.generated.javaB\x0bMatrixProtoH\x01\xf8\x01\x01\x62\x06proto3')
)

_DENSEMATFLOAT = _descriptor.Descriptor(
    name='DenseMatFloat',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.DenseMatFloat',
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    fields=[
        _descriptor.FieldDescriptor(
            name='dims', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.DenseMatFloat.dims', index=0,
            number=1, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='values', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.DenseMatFloat.values', index=1,
            number=10, type=2, cpp_type=6, label=3,
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
    serialized_start=109,
    serialized_end=154,
)

_COOMATFLOAT = _descriptor.Descriptor(
    name='CooMatFloat',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatFloat',
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    fields=[
        _descriptor.FieldDescriptor(
            name='dims', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatFloat.dims', index=0,
            number=1, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='rows', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatFloat.rows', index=1,
            number=2, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='cols', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatFloat.cols', index=2,
            number=3, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='values', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatFloat.values', index=3,
            number=10, type=2, cpp_type=6, label=3,
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
    serialized_start=156,
    serialized_end=227,
)

_CSRMATFLOAT = _descriptor.Descriptor(
    name='CsrMatFloat',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatFloat',
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    fields=[
        _descriptor.FieldDescriptor(
            name='dims', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatFloat.dims', index=0,
            number=1, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='row_offsets', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatFloat.row_offsets',
            index=1,
            number=2, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='col_indices', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatFloat.col_indices',
            index=2,
            number=3, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='values', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatFloat.values', index=3,
            number=10, type=2, cpp_type=6, label=3,
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
    serialized_start=229,
    serialized_end=314,
)

_CSCMATFLOAT = _descriptor.Descriptor(
    name='CscMatFloat',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatFloat',
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    fields=[
        _descriptor.FieldDescriptor(
            name='dims', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatFloat.dims', index=0,
            number=1, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='col_offsets', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatFloat.col_offsets',
            index=1,
            number=2, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='row_indices', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatFloat.row_indices',
            index=2,
            number=3, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='values', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatFloat.values', index=3,
            number=10, type=2, cpp_type=6, label=3,
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
    serialized_start=316,
    serialized_end=401,
)

_DENSEMATINT32 = _descriptor.Descriptor(
    name='DenseMatInt32',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.DenseMatInt32',
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    fields=[
        _descriptor.FieldDescriptor(
            name='dims', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.DenseMatInt32.dims', index=0,
            number=1, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='values', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.DenseMatInt32.values', index=1,
            number=10, type=5, cpp_type=1, label=3,
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
    serialized_start=403,
    serialized_end=448,
)

_COOMATINT32 = _descriptor.Descriptor(
    name='CooMatInt32',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatInt32',
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    fields=[
        _descriptor.FieldDescriptor(
            name='dims', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatInt32.dims', index=0,
            number=1, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='rows', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatInt32.rows', index=1,
            number=2, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='cols', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatInt32.cols', index=2,
            number=3, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='values', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatInt32.values', index=3,
            number=10, type=5, cpp_type=1, label=3,
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
    serialized_start=450,
    serialized_end=521,
)

_CSRMATINT32 = _descriptor.Descriptor(
    name='CsrMatInt32',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatInt32',
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    fields=[
        _descriptor.FieldDescriptor(
            name='dims', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatInt32.dims', index=0,
            number=1, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='row_offsets', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatInt32.row_offsets',
            index=1,
            number=2, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='col_indices', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatInt32.col_indices',
            index=2,
            number=3, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='values', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatInt32.values', index=3,
            number=10, type=5, cpp_type=1, label=3,
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
    serialized_start=523,
    serialized_end=608,
)

_CSCMATINT32 = _descriptor.Descriptor(
    name='CscMatInt32',
    full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatInt32',
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    fields=[
        _descriptor.FieldDescriptor(
            name='dims', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatInt32.dims', index=0,
            number=1, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='col_offsets', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatInt32.col_offsets',
            index=1,
            number=2, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='row_indices', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatInt32.row_indices',
            index=2,
            number=3, type=13, cpp_type=3, label=3,
            has_default_value=False, default_value=[],
            message_type=None, enum_type=None, containing_type=None,
            is_extension=False, extension_scope=None,
            serialized_options=None, file=DESCRIPTOR),
        _descriptor.FieldDescriptor(
            name='values', full_name='com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatInt32.values', index=3,
            number=10, type=5, cpp_type=1, label=3,
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
    serialized_start=610,
    serialized_end=695,
)

DESCRIPTOR.message_types_by_name['DenseMatFloat'] = _DENSEMATFLOAT
DESCRIPTOR.message_types_by_name['CooMatFloat'] = _COOMATFLOAT
DESCRIPTOR.message_types_by_name['CsrMatFloat'] = _CSRMATFLOAT
DESCRIPTOR.message_types_by_name['CscMatFloat'] = _CSCMATFLOAT
DESCRIPTOR.message_types_by_name['DenseMatInt32'] = _DENSEMATINT32
DESCRIPTOR.message_types_by_name['CooMatInt32'] = _COOMATINT32
DESCRIPTOR.message_types_by_name['CsrMatInt32'] = _CSRMATINT32
DESCRIPTOR.message_types_by_name['CscMatInt32'] = _CSCMATINT32
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

DenseMatFloat = _reflection.GeneratedProtocolMessageType('DenseMatFloat', (_message.Message,), {
    'DESCRIPTOR': _DENSEMATFLOAT,
    '__module__': 'com.hitnslab.dnssecurity.deeparcher.api.proto.matrix_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.DenseMatFloat)
})
_sym_db.RegisterMessage(DenseMatFloat)

CooMatFloat = _reflection.GeneratedProtocolMessageType('CooMatFloat', (_message.Message,), {
    'DESCRIPTOR': _COOMATFLOAT,
    '__module__': 'com.hitnslab.dnssecurity.deeparcher.api.proto.matrix_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatFloat)
})
_sym_db.RegisterMessage(CooMatFloat)

CsrMatFloat = _reflection.GeneratedProtocolMessageType('CsrMatFloat', (_message.Message,), {
    'DESCRIPTOR': _CSRMATFLOAT,
    '__module__': 'com.hitnslab.dnssecurity.deeparcher.api.proto.matrix_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatFloat)
})
_sym_db.RegisterMessage(CsrMatFloat)

CscMatFloat = _reflection.GeneratedProtocolMessageType('CscMatFloat', (_message.Message,), {
    'DESCRIPTOR': _CSCMATFLOAT,
    '__module__': 'com.hitnslab.dnssecurity.deeparcher.api.proto.matrix_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatFloat)
})
_sym_db.RegisterMessage(CscMatFloat)

DenseMatInt32 = _reflection.GeneratedProtocolMessageType('DenseMatInt32', (_message.Message,), {
    'DESCRIPTOR': _DENSEMATINT32,
    '__module__': 'com.hitnslab.dnssecurity.deeparcher.api.proto.matrix_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.DenseMatInt32)
})
_sym_db.RegisterMessage(DenseMatInt32)

CooMatInt32 = _reflection.GeneratedProtocolMessageType('CooMatInt32', (_message.Message,), {
    'DESCRIPTOR': _COOMATINT32,
    '__module__': 'com.hitnslab.dnssecurity.deeparcher.api.proto.matrix_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.CooMatInt32)
})
_sym_db.RegisterMessage(CooMatInt32)

CsrMatInt32 = _reflection.GeneratedProtocolMessageType('CsrMatInt32', (_message.Message,), {
    'DESCRIPTOR': _CSRMATINT32,
    '__module__': 'com.hitnslab.dnssecurity.deeparcher.api.proto.matrix_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.CsrMatInt32)
})
_sym_db.RegisterMessage(CsrMatInt32)

CscMatInt32 = _reflection.GeneratedProtocolMessageType('CscMatInt32', (_message.Message,), {
    'DESCRIPTOR': _CSCMATINT32,
    '__module__': 'com.hitnslab.dnssecurity.deeparcher.api.proto.matrix_pb2'
    # @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.CscMatInt32)
})
_sym_db.RegisterMessage(CscMatInt32)

DESCRIPTOR._options = None
# @@protoc_insertion_point(module_scope)
