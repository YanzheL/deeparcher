// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: com/hitnslab/dnssecurity/deeparcher/api/proto/domain_dns_detail.proto

#include "com/hitnslab/dnssecurity/deeparcher/api/proto/domain_dns_detail.pb.h"

#include <algorithm>

#include <google/protobuf/io/coded_stream.h>
#include <google/protobuf/extension_set.h>
#include <google/protobuf/wire_format_lite.h>
#include <google/protobuf/descriptor.h>
#include <google/protobuf/generated_message_reflection.h>
#include <google/protobuf/reflection_ops.h>
#include <google/protobuf/wire_format.h>
// @@protoc_insertion_point(includes)
#include <google/protobuf/port_def.inc>
namespace com {
namespace hitnslab {
namespace dnssecurity {
namespace deeparcher {
namespace api {
namespace proto {
class DomainDnsDetailDefaultTypeInternal {
 public:
  ::PROTOBUF_NAMESPACE_ID::internal::ExplicitlyConstructed<DomainDnsDetail> _instance;
} _DomainDnsDetail_default_instance_;
}  // namespace proto
}  // namespace api
}  // namespace deeparcher
}  // namespace dnssecurity
}  // namespace hitnslab
}  // namespace com
static void InitDefaultsscc_info_DomainDnsDetail_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto() {
  GOOGLE_PROTOBUF_VERIFY_VERSION;

  {
    void* ptr = &::com::hitnslab::dnssecurity::deeparcher::api::proto::_DomainDnsDetail_default_instance_;
    new (ptr) ::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail();
    ::PROTOBUF_NAMESPACE_ID::internal::OnShutdownDestroyMessage(ptr);
  }
  ::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail::InitAsDefaultInstance();
}

::PROTOBUF_NAMESPACE_ID::internal::SCCInfo<0> scc_info_DomainDnsDetail_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto =
    {{ATOMIC_VAR_INIT(::PROTOBUF_NAMESPACE_ID::internal::SCCInfoBase::kUninitialized), 0, 0, InitDefaultsscc_info_DomainDnsDetail_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto}, {}};

static ::PROTOBUF_NAMESPACE_ID::Metadata file_level_metadata_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto[1];
static constexpr ::PROTOBUF_NAMESPACE_ID::EnumDescriptor const** file_level_enum_descriptors_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto = nullptr;
static constexpr ::PROTOBUF_NAMESPACE_ID::ServiceDescriptor const** file_level_service_descriptors_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto = nullptr;

const ::PROTOBUF_NAMESPACE_ID::uint32 TableStruct_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto::offsets[] PROTOBUF_SECTION_VARIABLE(protodesc_cold) = {
  ~0u,  // no _has_bits_
  PROTOBUF_FIELD_OFFSET(::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail, _internal_metadata_),
  ~0u,  // no _extensions_
  ~0u,  // no _oneof_case_
  ~0u,  // no _weak_field_map_
  PROTOBUF_FIELD_OFFSET(::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail, fqdn_),
  PROTOBUF_FIELD_OFFSET(::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail, domain_),
  PROTOBUF_FIELD_OFFSET(::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail, ipv4_addrs_),
  PROTOBUF_FIELD_OFFSET(::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail, ipv6_addrs_),
  PROTOBUF_FIELD_OFFSET(::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail, cnames_),
};
static const ::PROTOBUF_NAMESPACE_ID::internal::MigrationSchema schemas[] PROTOBUF_SECTION_VARIABLE(protodesc_cold) = {
  { 0, -1, sizeof(::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail)},
};

static ::PROTOBUF_NAMESPACE_ID::Message const * const file_default_instances[] = {
  reinterpret_cast<const ::PROTOBUF_NAMESPACE_ID::Message*>(&::com::hitnslab::dnssecurity::deeparcher::api::proto::_DomainDnsDetail_default_instance_),
};

const char descriptor_table_protodef_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto[] PROTOBUF_SECTION_VARIABLE(protodesc_cold) =
  "\nEcom/hitnslab/dnssecurity/deeparcher/ap"
  "i/proto/domain_dns_detail.proto\022-com.hit"
  "nslab.dnssecurity.deeparcher.api.proto\"g"
  "\n\017DomainDnsDetail\022\014\n\004fqdn\030\001 \001(\t\022\016\n\006domai"
  "n\030\002 \001(\t\022\022\n\nipv4_addrs\030\007 \001(\014\022\022\n\nipv6_addr"
  "s\030\010 \001(\014\022\016\n\006cnames\030\t \003(\tBY\n<com.hitnslab."
  "dnssecurity.deeparcher.api.proto.generat"
  "ed.javaB\024DomainDnsDetailProtoH\001\370\001\001b\006prot"
  "o3"
  ;
static const ::PROTOBUF_NAMESPACE_ID::internal::DescriptorTable*const descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto_deps[1] = {
};
static ::PROTOBUF_NAMESPACE_ID::internal::SCCInfoBase*const descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto_sccs[1] = {
  &scc_info_DomainDnsDetail_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto.base,
};
static ::PROTOBUF_NAMESPACE_ID::internal::once_flag descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto_once;
const ::PROTOBUF_NAMESPACE_ID::internal::DescriptorTable descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto = {
  false, false, descriptor_table_protodef_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto, "com/hitnslab/dnssecurity/deeparcher/api/proto/domain_dns_detail.proto", 322,
  &descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto_once, descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto_sccs, descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto_deps, 1, 0,
  schemas, file_default_instances, TableStruct_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto::offsets,
  file_level_metadata_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto, 1, file_level_enum_descriptors_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto, file_level_service_descriptors_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto,
};

// Force running AddDescriptors() at dynamic initialization time.
static bool dynamic_init_dummy_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto = (static_cast<void>(::PROTOBUF_NAMESPACE_ID::internal::AddDescriptors(&descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto)), true);
namespace com {
namespace hitnslab {
namespace dnssecurity {
namespace deeparcher {
namespace api {
namespace proto {

// ===================================================================

void DomainDnsDetail::InitAsDefaultInstance() {
}
class DomainDnsDetail::_Internal {
 public:
};

DomainDnsDetail::DomainDnsDetail(::PROTOBUF_NAMESPACE_ID::Arena* arena)
  : ::PROTOBUF_NAMESPACE_ID::Message(arena),
  cnames_(arena) {
  SharedCtor();
  RegisterArenaDtor(arena);
  // @@protoc_insertion_point(arena_constructor:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
}
DomainDnsDetail::DomainDnsDetail(const DomainDnsDetail& from)
  : ::PROTOBUF_NAMESPACE_ID::Message(),
      cnames_(from.cnames_) {
  _internal_metadata_.MergeFrom<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>(from._internal_metadata_);
  fqdn_.UnsafeSetDefault(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
  if (!from._internal_fqdn().empty()) {
    fqdn_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), from._internal_fqdn(),
      GetArena());
  }
  domain_.UnsafeSetDefault(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
  if (!from._internal_domain().empty()) {
    domain_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), from._internal_domain(),
      GetArena());
  }
  ipv4_addrs_.UnsafeSetDefault(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
  if (!from._internal_ipv4_addrs().empty()) {
    ipv4_addrs_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), from._internal_ipv4_addrs(),
      GetArena());
  }
  ipv6_addrs_.UnsafeSetDefault(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
  if (!from._internal_ipv6_addrs().empty()) {
    ipv6_addrs_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), from._internal_ipv6_addrs(),
      GetArena());
  }
  // @@protoc_insertion_point(copy_constructor:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
}

void DomainDnsDetail::SharedCtor() {
  ::PROTOBUF_NAMESPACE_ID::internal::InitSCC(&scc_info_DomainDnsDetail_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto.base);
  fqdn_.UnsafeSetDefault(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
  domain_.UnsafeSetDefault(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
  ipv4_addrs_.UnsafeSetDefault(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
  ipv6_addrs_.UnsafeSetDefault(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
}

DomainDnsDetail::~DomainDnsDetail() {
  // @@protoc_insertion_point(destructor:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
  SharedDtor();
  _internal_metadata_.Delete<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>();
}

void DomainDnsDetail::SharedDtor() {
  GOOGLE_DCHECK(GetArena() == nullptr);
  fqdn_.DestroyNoArena(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
  domain_.DestroyNoArena(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
  ipv4_addrs_.DestroyNoArena(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
  ipv6_addrs_.DestroyNoArena(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited());
}

void DomainDnsDetail::ArenaDtor(void* object) {
  DomainDnsDetail* _this = reinterpret_cast< DomainDnsDetail* >(object);
  (void)_this;
}
void DomainDnsDetail::RegisterArenaDtor(::PROTOBUF_NAMESPACE_ID::Arena*) {
}
void DomainDnsDetail::SetCachedSize(int size) const {
  _cached_size_.Set(size);
}
const DomainDnsDetail& DomainDnsDetail::default_instance() {
  ::PROTOBUF_NAMESPACE_ID::internal::InitSCC(&::scc_info_DomainDnsDetail_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto.base);
  return *internal_default_instance();
}


void DomainDnsDetail::Clear() {
// @@protoc_insertion_point(message_clear_start:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
  ::PROTOBUF_NAMESPACE_ID::uint32 cached_has_bits = 0;
  // Prevent compiler warnings about cached_has_bits being unused
  (void) cached_has_bits;

  cnames_.Clear();
  fqdn_.ClearToEmpty(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
  domain_.ClearToEmpty(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
  ipv4_addrs_.ClearToEmpty(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
  ipv6_addrs_.ClearToEmpty(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
  _internal_metadata_.Clear<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>();
}

const char* DomainDnsDetail::_InternalParse(const char* ptr, ::PROTOBUF_NAMESPACE_ID::internal::ParseContext* ctx) {
#define CHK_(x) if (PROTOBUF_PREDICT_FALSE(!(x))) goto failure
  ::PROTOBUF_NAMESPACE_ID::Arena* arena = GetArena(); (void)arena;
  while (!ctx->Done(&ptr)) {
    ::PROTOBUF_NAMESPACE_ID::uint32 tag;
    ptr = ::PROTOBUF_NAMESPACE_ID::internal::ReadTag(ptr, &tag);
    CHK_(ptr);
    switch (tag >> 3) {
      // string fqdn = 1;
      case 1:
        if (PROTOBUF_PREDICT_TRUE(static_cast<::PROTOBUF_NAMESPACE_ID::uint8>(tag) == 10)) {
          auto str = _internal_mutable_fqdn();
          ptr = ::PROTOBUF_NAMESPACE_ID::internal::InlineGreedyStringParser(str, ptr, ctx);
          CHK_(::PROTOBUF_NAMESPACE_ID::internal::VerifyUTF8(str, "com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.fqdn"));
          CHK_(ptr);
        } else goto handle_unusual;
        continue;
      // string domain = 2;
      case 2:
        if (PROTOBUF_PREDICT_TRUE(static_cast<::PROTOBUF_NAMESPACE_ID::uint8>(tag) == 18)) {
          auto str = _internal_mutable_domain();
          ptr = ::PROTOBUF_NAMESPACE_ID::internal::InlineGreedyStringParser(str, ptr, ctx);
          CHK_(::PROTOBUF_NAMESPACE_ID::internal::VerifyUTF8(str, "com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.domain"));
          CHK_(ptr);
        } else goto handle_unusual;
        continue;
      // bytes ipv4_addrs = 7;
      case 7:
        if (PROTOBUF_PREDICT_TRUE(static_cast<::PROTOBUF_NAMESPACE_ID::uint8>(tag) == 58)) {
          auto str = _internal_mutable_ipv4_addrs();
          ptr = ::PROTOBUF_NAMESPACE_ID::internal::InlineGreedyStringParser(str, ptr, ctx);
          CHK_(ptr);
        } else goto handle_unusual;
        continue;
      // bytes ipv6_addrs = 8;
      case 8:
        if (PROTOBUF_PREDICT_TRUE(static_cast<::PROTOBUF_NAMESPACE_ID::uint8>(tag) == 66)) {
          auto str = _internal_mutable_ipv6_addrs();
          ptr = ::PROTOBUF_NAMESPACE_ID::internal::InlineGreedyStringParser(str, ptr, ctx);
          CHK_(ptr);
        } else goto handle_unusual;
        continue;
      // repeated string cnames = 9;
      case 9:
        if (PROTOBUF_PREDICT_TRUE(static_cast<::PROTOBUF_NAMESPACE_ID::uint8>(tag) == 74)) {
          ptr -= 1;
          do {
            ptr += 1;
            auto str = _internal_add_cnames();
            ptr = ::PROTOBUF_NAMESPACE_ID::internal::InlineGreedyStringParser(str, ptr, ctx);
            CHK_(::PROTOBUF_NAMESPACE_ID::internal::VerifyUTF8(str, "com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames"));
            CHK_(ptr);
            if (!ctx->DataAvailable(ptr)) break;
          } while (::PROTOBUF_NAMESPACE_ID::internal::ExpectTag<74>(ptr));
        } else goto handle_unusual;
        continue;
      default: {
      handle_unusual:
        if ((tag & 7) == 4 || tag == 0) {
          ctx->SetLastTag(tag);
          goto success;
        }
        ptr = UnknownFieldParse(tag,
            _internal_metadata_.mutable_unknown_fields<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>(),
            ptr, ctx);
        CHK_(ptr != nullptr);
        continue;
      }
    }  // switch
  }  // while
success:
  return ptr;
failure:
  ptr = nullptr;
  goto success;
#undef CHK_
}

::PROTOBUF_NAMESPACE_ID::uint8* DomainDnsDetail::_InternalSerialize(
    ::PROTOBUF_NAMESPACE_ID::uint8* target, ::PROTOBUF_NAMESPACE_ID::io::EpsCopyOutputStream* stream) const {
  // @@protoc_insertion_point(serialize_to_array_start:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
  ::PROTOBUF_NAMESPACE_ID::uint32 cached_has_bits = 0;
  (void) cached_has_bits;

  // string fqdn = 1;
  if (this->fqdn().size() > 0) {
    ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::VerifyUtf8String(
      this->_internal_fqdn().data(), static_cast<int>(this->_internal_fqdn().length()),
      ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::SERIALIZE,
      "com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.fqdn");
    target = stream->WriteStringMaybeAliased(
        1, this->_internal_fqdn(), target);
  }

  // string domain = 2;
  if (this->domain().size() > 0) {
    ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::VerifyUtf8String(
      this->_internal_domain().data(), static_cast<int>(this->_internal_domain().length()),
      ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::SERIALIZE,
      "com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.domain");
    target = stream->WriteStringMaybeAliased(
        2, this->_internal_domain(), target);
  }

  // bytes ipv4_addrs = 7;
  if (this->ipv4_addrs().size() > 0) {
    target = stream->WriteBytesMaybeAliased(
        7, this->_internal_ipv4_addrs(), target);
  }

  // bytes ipv6_addrs = 8;
  if (this->ipv6_addrs().size() > 0) {
    target = stream->WriteBytesMaybeAliased(
        8, this->_internal_ipv6_addrs(), target);
  }

  // repeated string cnames = 9;
  for (int i = 0, n = this->_internal_cnames_size(); i < n; i++) {
    const auto& s = this->_internal_cnames(i);
    ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::VerifyUtf8String(
      s.data(), static_cast<int>(s.length()),
      ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::SERIALIZE,
      "com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames");
    target = stream->WriteString(9, s, target);
  }

  if (PROTOBUF_PREDICT_FALSE(_internal_metadata_.have_unknown_fields())) {
    target = ::PROTOBUF_NAMESPACE_ID::internal::WireFormat::InternalSerializeUnknownFieldsToArray(
        _internal_metadata_.unknown_fields<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>(::PROTOBUF_NAMESPACE_ID::UnknownFieldSet::default_instance), target, stream);
  }
  // @@protoc_insertion_point(serialize_to_array_end:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
  return target;
}

size_t DomainDnsDetail::ByteSizeLong() const {
// @@protoc_insertion_point(message_byte_size_start:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
  size_t total_size = 0;

  ::PROTOBUF_NAMESPACE_ID::uint32 cached_has_bits = 0;
  // Prevent compiler warnings about cached_has_bits being unused
  (void) cached_has_bits;

  // repeated string cnames = 9;
  total_size += 1 *
      ::PROTOBUF_NAMESPACE_ID::internal::FromIntSize(cnames_.size());
  for (int i = 0, n = cnames_.size(); i < n; i++) {
    total_size += ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::StringSize(
      cnames_.Get(i));
  }

  // string fqdn = 1;
  if (this->fqdn().size() > 0) {
    total_size += 1 +
      ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::StringSize(
        this->_internal_fqdn());
  }

  // string domain = 2;
  if (this->domain().size() > 0) {
    total_size += 1 +
      ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::StringSize(
        this->_internal_domain());
  }

  // bytes ipv4_addrs = 7;
  if (this->ipv4_addrs().size() > 0) {
    total_size += 1 +
      ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::BytesSize(
        this->_internal_ipv4_addrs());
  }

  // bytes ipv6_addrs = 8;
  if (this->ipv6_addrs().size() > 0) {
    total_size += 1 +
      ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::BytesSize(
        this->_internal_ipv6_addrs());
  }

  if (PROTOBUF_PREDICT_FALSE(_internal_metadata_.have_unknown_fields())) {
    return ::PROTOBUF_NAMESPACE_ID::internal::ComputeUnknownFieldsSize(
        _internal_metadata_, total_size, &_cached_size_);
  }
  int cached_size = ::PROTOBUF_NAMESPACE_ID::internal::ToCachedSize(total_size);
  SetCachedSize(cached_size);
  return total_size;
}

void DomainDnsDetail::MergeFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) {
// @@protoc_insertion_point(generalized_merge_from_start:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
  GOOGLE_DCHECK_NE(&from, this);
  const DomainDnsDetail* source =
      ::PROTOBUF_NAMESPACE_ID::DynamicCastToGenerated<DomainDnsDetail>(
          &from);
  if (source == nullptr) {
  // @@protoc_insertion_point(generalized_merge_from_cast_fail:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
    ::PROTOBUF_NAMESPACE_ID::internal::ReflectionOps::Merge(from, this);
  } else {
  // @@protoc_insertion_point(generalized_merge_from_cast_success:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
    MergeFrom(*source);
  }
}

void DomainDnsDetail::MergeFrom(const DomainDnsDetail& from) {
// @@protoc_insertion_point(class_specific_merge_from_start:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
  GOOGLE_DCHECK_NE(&from, this);
  _internal_metadata_.MergeFrom<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>(from._internal_metadata_);
  ::PROTOBUF_NAMESPACE_ID::uint32 cached_has_bits = 0;
  (void) cached_has_bits;

  cnames_.MergeFrom(from.cnames_);
  if (from.fqdn().size() > 0) {
    _internal_set_fqdn(from._internal_fqdn());
  }
  if (from.domain().size() > 0) {
    _internal_set_domain(from._internal_domain());
  }
  if (from.ipv4_addrs().size() > 0) {
    _internal_set_ipv4_addrs(from._internal_ipv4_addrs());
  }
  if (from.ipv6_addrs().size() > 0) {
    _internal_set_ipv6_addrs(from._internal_ipv6_addrs());
  }
}

void DomainDnsDetail::CopyFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) {
// @@protoc_insertion_point(generalized_copy_from_start:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
  if (&from == this) return;
  Clear();
  MergeFrom(from);
}

void DomainDnsDetail::CopyFrom(const DomainDnsDetail& from) {
// @@protoc_insertion_point(class_specific_copy_from_start:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
  if (&from == this) return;
  Clear();
  MergeFrom(from);
}

bool DomainDnsDetail::IsInitialized() const {
  return true;
}

void DomainDnsDetail::InternalSwap(DomainDnsDetail* other) {
  using std::swap;
  _internal_metadata_.Swap<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>(&other->_internal_metadata_);
  cnames_.InternalSwap(&other->cnames_);
  fqdn_.Swap(&other->fqdn_, &::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
  domain_.Swap(&other->domain_, &::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
  ipv4_addrs_.Swap(&other->ipv4_addrs_, &::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
  ipv6_addrs_.Swap(&other->ipv6_addrs_, &::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}

::PROTOBUF_NAMESPACE_ID::Metadata DomainDnsDetail::GetMetadata() const {
  return GetMetadataStatic();
}


// @@protoc_insertion_point(namespace_scope)
}  // namespace proto
}  // namespace api
}  // namespace deeparcher
}  // namespace dnssecurity
}  // namespace hitnslab
}  // namespace com
PROTOBUF_NAMESPACE_OPEN
template<> PROTOBUF_NOINLINE ::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail* Arena::CreateMaybeMessage< ::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail >(Arena* arena) {
  return Arena::CreateMessageInternal< ::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail >(arena);
}
PROTOBUF_NAMESPACE_CLOSE

// @@protoc_insertion_point(global_scope)
#include <google/protobuf/port_undef.inc>
