// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: com/hitnslab/dnssecurity/deeparcher/api/proto/graph_assoc_edge_update.proto

package com.hitnslab.dnssecurity.deeparcher.api.proto.generated;

public final class GraphAssocEdgeUpdateProto {
    private static final com.google.protobuf.Descriptors.Descriptor
            internal_static_com_hitnslab_dnssecurity_deeparcher_api_proto_GraphAssocEdgeUpdate_descriptor;
    private static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_com_hitnslab_dnssecurity_deeparcher_api_proto_GraphAssocEdgeUpdate_fieldAccessorTable;
    private static com.google.protobuf.Descriptors.FileDescriptor
            descriptor;

    static {
        java.lang.String[] descriptorData = {
                "\nEcom/hitnslab/dnssecurity/deeparcher/ap" +
                        "i/proto/graph_assoc_edge_update.proto\022-com.hit" +
                        "nslab.dnssecurity.deeparcher.api.proto\"M" +
                        "\n\024GraphAssocEdgeUpdate\022\r\n\005fqdn1\030\001 \001(\t\022\r\n" +
                        "\005fqdn2\030\002 \001(\t\022\027\n\017n_shared_fields\030\003 \001(\005BY\n" +
                        "7com.hitnslab.dnssecurity.deeparcher.api" +
                        ".proto.generatedB\031GraphAssocEdgeUpdatePr" +
                        "otoH\001\370\001\001b\006proto3"
        };
        descriptor = com.google.protobuf.Descriptors.FileDescriptor
                .internalBuildGeneratedFileFrom(descriptorData,
                        new com.google.protobuf.Descriptors.FileDescriptor[]{
                        });
        internal_static_com_hitnslab_dnssecurity_deeparcher_api_proto_GraphAssocEdgeUpdate_descriptor =
                getDescriptor().getMessageTypes().get(0);
        internal_static_com_hitnslab_dnssecurity_deeparcher_api_proto_GraphAssocEdgeUpdate_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_com_hitnslab_dnssecurity_deeparcher_api_proto_GraphAssocEdgeUpdate_descriptor,
                new java.lang.String[]{"Fqdn1", "Fqdn2", "NSharedFields",});
    }

    private GraphAssocEdgeUpdateProto() {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistry registry) {
        registerAllExtensions(
                (com.google.protobuf.ExtensionRegistryLite) registry);
    }

    public static com.google.protobuf.Descriptors.FileDescriptor
    getDescriptor() {
        return descriptor;
    }

    public interface GraphAssocEdgeUpdateOrBuilder extends
            // @@protoc_insertion_point(interface_extends:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate)
            com.google.protobuf.MessageOrBuilder {

        /**
         * <code>string fqdn1 = 1;</code>
         *
         * @return The fqdn1.
         */
        java.lang.String getFqdn1();

        /**
         * <code>string fqdn1 = 1;</code>
         *
         * @return The bytes for fqdn1.
         */
        com.google.protobuf.ByteString
        getFqdn1Bytes();

        /**
         * <code>string fqdn2 = 2;</code>
         *
         * @return The fqdn2.
         */
        java.lang.String getFqdn2();

        /**
         * <code>string fqdn2 = 2;</code>
         *
         * @return The bytes for fqdn2.
         */
        com.google.protobuf.ByteString
        getFqdn2Bytes();

        /**
         * <code>int32 n_shared_fields = 3;</code>
         *
         * @return The nSharedFields.
         */
        int getNSharedFields();
    }

    /**
     * Protobuf type {@code com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate}
     */
    public static final class GraphAssocEdgeUpdate extends
            com.google.protobuf.GeneratedMessageV3 implements
            // @@protoc_insertion_point(message_implements:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate)
            GraphAssocEdgeUpdateOrBuilder {
        public static final int FQDN1_FIELD_NUMBER = 1;
        public static final int FQDN2_FIELD_NUMBER = 2;
        public static final int N_SHARED_FIELDS_FIELD_NUMBER = 3;
        private static final long serialVersionUID = 0L;
        // @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate)
        private static final com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate DEFAULT_INSTANCE;
        private static final com.google.protobuf.Parser<GraphAssocEdgeUpdate>
                PARSER = new com.google.protobuf.AbstractParser<GraphAssocEdgeUpdate>() {
            @java.lang.Override
            public GraphAssocEdgeUpdate parsePartialFrom(
                    com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                    throws com.google.protobuf.InvalidProtocolBufferException {
                return new GraphAssocEdgeUpdate(input, extensionRegistry);
            }
        };

        static {
            DEFAULT_INSTANCE = new com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate();
        }

        private volatile java.lang.Object fqdn1_;
        private volatile java.lang.Object fqdn2_;
        private int nSharedFields_;
        private byte memoizedIsInitialized = -1;

        // Use GraphAssocEdgeUpdate.newBuilder() to construct.
        private GraphAssocEdgeUpdate(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
            super(builder);
        }

        private GraphAssocEdgeUpdate() {
            fqdn1_ = "";
            fqdn2_ = "";
        }

        private GraphAssocEdgeUpdate(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new java.lang.NullPointerException();
            }
            com.google.protobuf.UnknownFieldSet.Builder unknownFields =
                    com.google.protobuf.UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        case 10: {
                            java.lang.String s = input.readStringRequireUtf8();

                            fqdn1_ = s;
                            break;
                        }
                        case 18: {
                            java.lang.String s = input.readStringRequireUtf8();

                            fqdn2_ = s;
                            break;
                        }
                        case 24: {

                            nSharedFields_ = input.readInt32();
                            break;
                        }
                        default: {
                            if (!parseUnknownField(
                                    input, unknownFields, extensionRegistry, tag)) {
                                done = true;
                            }
                            break;
                        }
                    }
                }
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(this);
            } catch (java.io.IOException e) {
                throw new com.google.protobuf.InvalidProtocolBufferException(
                        e).setUnfinishedMessage(this);
            } finally {
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.internal_static_com_hitnslab_dnssecurity_deeparcher_api_proto_GraphAssocEdgeUpdate_descriptor;
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseFrom(
                java.nio.ByteBuffer data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseFrom(
                java.nio.ByteBuffer data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseFrom(
                com.google.protobuf.ByteString data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseFrom(
                com.google.protobuf.ByteString data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseFrom(byte[] data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseFrom(
                byte[] data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseFrom(java.io.InputStream input)
                throws java.io.IOException {
            return com.google.protobuf.GeneratedMessageV3
                    .parseWithIOException(PARSER, input);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseFrom(
                java.io.InputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return com.google.protobuf.GeneratedMessageV3
                    .parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseDelimitedFrom(java.io.InputStream input)
                throws java.io.IOException {
            return com.google.protobuf.GeneratedMessageV3
                    .parseDelimitedWithIOException(PARSER, input);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseDelimitedFrom(
                java.io.InputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return com.google.protobuf.GeneratedMessageV3
                    .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseFrom(
                com.google.protobuf.CodedInputStream input)
                throws java.io.IOException {
            return com.google.protobuf.GeneratedMessageV3
                    .parseWithIOException(PARSER, input);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parseFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return com.google.protobuf.GeneratedMessageV3
                    .parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        public static com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static com.google.protobuf.Parser<GraphAssocEdgeUpdate> parser() {
            return PARSER;
        }

        @java.lang.Override
        @SuppressWarnings({"unused"})
        protected java.lang.Object newInstance(
                UnusedPrivateParameter unused) {
            return new GraphAssocEdgeUpdate();
        }

        @java.lang.Override
        public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
            return this.unknownFields;
        }

        @java.lang.Override
        protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.internal_static_com_hitnslab_dnssecurity_deeparcher_api_proto_GraphAssocEdgeUpdate_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate.class, com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate.Builder.class);
        }

        /**
         * <code>string fqdn1 = 1;</code>
         *
         * @return The fqdn1.
         */
        public java.lang.String getFqdn1() {
            java.lang.Object ref = fqdn1_;
            if (ref instanceof java.lang.String) {
                return (java.lang.String) ref;
            } else {
                com.google.protobuf.ByteString bs =
                        (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                fqdn1_ = s;
                return s;
            }
        }

        /**
         * <code>string fqdn1 = 1;</code>
         *
         * @return The bytes for fqdn1.
         */
        public com.google.protobuf.ByteString
        getFqdn1Bytes() {
            java.lang.Object ref = fqdn1_;
            if (ref instanceof java.lang.String) {
                com.google.protobuf.ByteString b =
                        com.google.protobuf.ByteString.copyFromUtf8(
                                (java.lang.String) ref);
                fqdn1_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        /**
         * <code>string fqdn2 = 2;</code>
         *
         * @return The fqdn2.
         */
        public java.lang.String getFqdn2() {
            java.lang.Object ref = fqdn2_;
            if (ref instanceof java.lang.String) {
                return (java.lang.String) ref;
            } else {
                com.google.protobuf.ByteString bs =
                        (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                fqdn2_ = s;
                return s;
            }
        }

        /**
         * <code>string fqdn2 = 2;</code>
         *
         * @return The bytes for fqdn2.
         */
        public com.google.protobuf.ByteString
        getFqdn2Bytes() {
            java.lang.Object ref = fqdn2_;
            if (ref instanceof java.lang.String) {
                com.google.protobuf.ByteString b =
                        com.google.protobuf.ByteString.copyFromUtf8(
                                (java.lang.String) ref);
                fqdn2_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        /**
         * <code>int32 n_shared_fields = 3;</code>
         *
         * @return The nSharedFields.
         */
        public int getNSharedFields() {
            return nSharedFields_;
        }

        @java.lang.Override
        public final boolean isInitialized() {
            byte isInitialized = memoizedIsInitialized;
            if (isInitialized == 1) return true;
            if (isInitialized == 0) return false;

            memoizedIsInitialized = 1;
            return true;
        }

        @java.lang.Override
        public void writeTo(com.google.protobuf.CodedOutputStream output)
                throws java.io.IOException {
            if (!getFqdn1Bytes().isEmpty()) {
                com.google.protobuf.GeneratedMessageV3.writeString(output, 1, fqdn1_);
            }
            if (!getFqdn2Bytes().isEmpty()) {
                com.google.protobuf.GeneratedMessageV3.writeString(output, 2, fqdn2_);
            }
            if (nSharedFields_ != 0) {
                output.writeInt32(3, nSharedFields_);
            }
            unknownFields.writeTo(output);
        }

        @java.lang.Override
        public int getSerializedSize() {
            int size = memoizedSize;
            if (size != -1) return size;

            size = 0;
            if (!getFqdn1Bytes().isEmpty()) {
                size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, fqdn1_);
            }
            if (!getFqdn2Bytes().isEmpty()) {
                size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, fqdn2_);
            }
            if (nSharedFields_ != 0) {
                size += com.google.protobuf.CodedOutputStream
                        .computeInt32Size(3, nSharedFields_);
            }
            size += unknownFields.getSerializedSize();
            memoizedSize = size;
            return size;
        }

        @java.lang.Override
        public boolean equals(final java.lang.Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate)) {
                return super.equals(obj);
            }
            com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate other = (com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate) obj;

            if (!getFqdn1()
                    .equals(other.getFqdn1())) return false;
            if (!getFqdn2()
                    .equals(other.getFqdn2())) return false;
            if (getNSharedFields()
                    != other.getNSharedFields()) return false;
            if (!unknownFields.equals(other.unknownFields)) return false;
            return true;
        }

        @java.lang.Override
        public int hashCode() {
            if (memoizedHashCode != 0) {
                return memoizedHashCode;
            }
            int hash = 41;
            hash = (19 * hash) + getDescriptor().hashCode();
            hash = (37 * hash) + FQDN1_FIELD_NUMBER;
            hash = (53 * hash) + getFqdn1().hashCode();
            hash = (37 * hash) + FQDN2_FIELD_NUMBER;
            hash = (53 * hash) + getFqdn2().hashCode();
            hash = (37 * hash) + N_SHARED_FIELDS_FIELD_NUMBER;
            hash = (53 * hash) + getNSharedFields();
            hash = (29 * hash) + unknownFields.hashCode();
            memoizedHashCode = hash;
            return hash;
        }

        @java.lang.Override
        public Builder newBuilderForType() {
            return newBuilder();
        }

        @java.lang.Override
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE
                    ? new Builder() : new Builder().mergeFrom(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(
                com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        @java.lang.Override
        public com.google.protobuf.Parser<GraphAssocEdgeUpdate> getParserForType() {
            return PARSER;
        }

        @java.lang.Override
        public com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        /**
         * Protobuf type {@code com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate}
         */
        public static final class Builder extends
                com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
                // @@protoc_insertion_point(builder_implements:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate)
                com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdateOrBuilder {
            private java.lang.Object fqdn1_ = "";
            private java.lang.Object fqdn2_ = "";
            private int nSharedFields_;

            // Construct using com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate.newBuilder()
            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(
                    com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
                return com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.internal_static_com_hitnslab_dnssecurity_deeparcher_api_proto_GraphAssocEdgeUpdate_descriptor;
            }

            @java.lang.Override
            protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internalGetFieldAccessorTable() {
                return com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.internal_static_com_hitnslab_dnssecurity_deeparcher_api_proto_GraphAssocEdgeUpdate_fieldAccessorTable
                        .ensureFieldAccessorsInitialized(
                                com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate.class, com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate.Builder.class);
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessageV3
                        .alwaysUseFieldBuilders) {
                }
            }

            @java.lang.Override
            public Builder clear() {
                super.clear();
                fqdn1_ = "";

                fqdn2_ = "";

                nSharedFields_ = 0;

                return this;
            }

            @java.lang.Override
            public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
                return com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.internal_static_com_hitnslab_dnssecurity_deeparcher_api_proto_GraphAssocEdgeUpdate_descriptor;
            }

            @java.lang.Override
            public com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate getDefaultInstanceForType() {
                return com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate.getDefaultInstance();
            }

            @java.lang.Override
            public com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate build() {
                com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @java.lang.Override
            public com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate buildPartial() {
                com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate result = new com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate(this);
                result.fqdn1_ = fqdn1_;
                result.fqdn2_ = fqdn2_;
                result.nSharedFields_ = nSharedFields_;
                onBuilt();
                return result;
            }

            @java.lang.Override
            public Builder clone() {
                return super.clone();
            }

            @java.lang.Override
            public Builder setField(
                    com.google.protobuf.Descriptors.FieldDescriptor field,
                    java.lang.Object value) {
                return super.setField(field, value);
            }

            @java.lang.Override
            public Builder clearField(
                    com.google.protobuf.Descriptors.FieldDescriptor field) {
                return super.clearField(field);
            }

            @java.lang.Override
            public Builder clearOneof(
                    com.google.protobuf.Descriptors.OneofDescriptor oneof) {
                return super.clearOneof(oneof);
            }

            @java.lang.Override
            public Builder setRepeatedField(
                    com.google.protobuf.Descriptors.FieldDescriptor field,
                    int index, java.lang.Object value) {
                return super.setRepeatedField(field, index, value);
            }

            @java.lang.Override
            public Builder addRepeatedField(
                    com.google.protobuf.Descriptors.FieldDescriptor field,
                    java.lang.Object value) {
                return super.addRepeatedField(field, value);
            }

            @java.lang.Override
            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate) {
                    return mergeFrom((com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate other) {
                if (other == com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate.getDefaultInstance())
                    return this;
                if (!other.getFqdn1().isEmpty()) {
                    fqdn1_ = other.fqdn1_;
                    onChanged();
                }
                if (!other.getFqdn2().isEmpty()) {
                    fqdn2_ = other.fqdn2_;
                    onChanged();
                }
                if (other.getNSharedFields() != 0) {
                    setNSharedFields(other.getNSharedFields());
                }
                this.mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @java.lang.Override
            public final boolean isInitialized() {
                return true;
            }

            @java.lang.Override
            public Builder mergeFrom(
                    com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                    throws java.io.IOException {
                com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate parsedMessage = null;
                try {
                    parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
                } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                    parsedMessage = (com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate) e.getUnfinishedMessage();
                    throw e.unwrapIOException();
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            /**
             * <code>string fqdn1 = 1;</code>
             *
             * @return The fqdn1.
             */
            public java.lang.String getFqdn1() {
                java.lang.Object ref = fqdn1_;
                if (!(ref instanceof java.lang.String)) {
                    com.google.protobuf.ByteString bs =
                            (com.google.protobuf.ByteString) ref;
                    java.lang.String s = bs.toStringUtf8();
                    fqdn1_ = s;
                    return s;
                } else {
                    return (java.lang.String) ref;
                }
            }

            /**
             * <code>string fqdn1 = 1;</code>
             *
             * @param value The fqdn1 to set.
             * @return This builder for chaining.
             */
            public Builder setFqdn1(
                    java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }

                fqdn1_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>string fqdn1 = 1;</code>
             *
             * @return The bytes for fqdn1.
             */
            public com.google.protobuf.ByteString
            getFqdn1Bytes() {
                java.lang.Object ref = fqdn1_;
                if (ref instanceof String) {
                    com.google.protobuf.ByteString b =
                            com.google.protobuf.ByteString.copyFromUtf8(
                                    (java.lang.String) ref);
                    fqdn1_ = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            /**
             * <code>string fqdn1 = 1;</code>
             *
             * @param value The bytes for fqdn1 to set.
             * @return This builder for chaining.
             */
            public Builder setFqdn1Bytes(
                    com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                checkByteStringIsUtf8(value);

                fqdn1_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>string fqdn1 = 1;</code>
             *
             * @return This builder for chaining.
             */
            public Builder clearFqdn1() {

                fqdn1_ = getDefaultInstance().getFqdn1();
                onChanged();
                return this;
            }

            /**
             * <code>string fqdn2 = 2;</code>
             *
             * @return The fqdn2.
             */
            public java.lang.String getFqdn2() {
                java.lang.Object ref = fqdn2_;
                if (!(ref instanceof java.lang.String)) {
                    com.google.protobuf.ByteString bs =
                            (com.google.protobuf.ByteString) ref;
                    java.lang.String s = bs.toStringUtf8();
                    fqdn2_ = s;
                    return s;
                } else {
                    return (java.lang.String) ref;
                }
            }

            /**
             * <code>string fqdn2 = 2;</code>
             *
             * @param value The fqdn2 to set.
             * @return This builder for chaining.
             */
            public Builder setFqdn2(
                    java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }

                fqdn2_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>string fqdn2 = 2;</code>
             *
             * @return The bytes for fqdn2.
             */
            public com.google.protobuf.ByteString
            getFqdn2Bytes() {
                java.lang.Object ref = fqdn2_;
                if (ref instanceof String) {
                    com.google.protobuf.ByteString b =
                            com.google.protobuf.ByteString.copyFromUtf8(
                                    (java.lang.String) ref);
                    fqdn2_ = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            /**
             * <code>string fqdn2 = 2;</code>
             *
             * @param value The bytes for fqdn2 to set.
             * @return This builder for chaining.
             */
            public Builder setFqdn2Bytes(
                    com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                checkByteStringIsUtf8(value);

                fqdn2_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>string fqdn2 = 2;</code>
             *
             * @return This builder for chaining.
             */
            public Builder clearFqdn2() {

                fqdn2_ = getDefaultInstance().getFqdn2();
                onChanged();
                return this;
            }

            /**
             * <code>int32 n_shared_fields = 3;</code>
             *
             * @return The nSharedFields.
             */
            public int getNSharedFields() {
                return nSharedFields_;
            }

            /**
             * <code>int32 n_shared_fields = 3;</code>
             *
             * @param value The nSharedFields to set.
             * @return This builder for chaining.
             */
            public Builder setNSharedFields(int value) {

                nSharedFields_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>int32 n_shared_fields = 3;</code>
             *
             * @return This builder for chaining.
             */
            public Builder clearNSharedFields() {

                nSharedFields_ = 0;
                onChanged();
                return this;
            }

            @java.lang.Override
            public final Builder setUnknownFields(
                    final com.google.protobuf.UnknownFieldSet unknownFields) {
                return super.setUnknownFields(unknownFields);
            }

            @java.lang.Override
            public final Builder mergeUnknownFields(
                    final com.google.protobuf.UnknownFieldSet unknownFields) {
                return super.mergeUnknownFields(unknownFields);
            }


            // @@protoc_insertion_point(builder_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphAssocEdgeUpdate)
        }

    }

    // @@protoc_insertion_point(outer_class_scope)
}
