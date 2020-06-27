// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: com/hitnslab/dnssecurity/deeparcher/api/proto/domain_dns_detail.proto

#ifndef GOOGLE_PROTOBUF_INCLUDED_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto
#define GOOGLE_PROTOBUF_INCLUDED_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto

#include <limits>
#include <string>

#include <google/protobuf/port_def.inc>
#if PROTOBUF_VERSION < 3012000
#error This file was generated by a newer version of protoc which is
#error incompatible with your Protocol Buffer headers. Please update
#error your headers.
#endif
#if 3012003 < PROTOBUF_MIN_PROTOC_VERSION
#error This file was generated by an older version of protoc which is
#error incompatible with your Protocol Buffer headers. Please
#error regenerate this file with a newer version of protoc.
#endif

#include <google/protobuf/port_undef.inc>
#include <google/protobuf/io/coded_stream.h>
#include <google/protobuf/arena.h>
#include <google/protobuf/arenastring.h>
#include <google/protobuf/generated_message_table_driven.h>
#include <google/protobuf/generated_message_util.h>
#include <google/protobuf/inlined_string_field.h>
#include <google/protobuf/metadata_lite.h>
#include <google/protobuf/generated_message_reflection.h>
#include <google/protobuf/message.h>
#include <google/protobuf/repeated_field.h>  // IWYU pragma: export
#include <google/protobuf/extension_set.h>  // IWYU pragma: export
#include <google/protobuf/unknown_field_set.h>
// @@protoc_insertion_point(includes)
#include <google/protobuf/port_def.inc>
#define PROTOBUF_INTERNAL_EXPORT_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto
PROTOBUF_NAMESPACE_OPEN
namespace internal {
class AnyMetadata;
}  // namespace internal
PROTOBUF_NAMESPACE_CLOSE

// Internal implementation detail -- do not use these members.
struct TableStruct_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto {
  static const ::PROTOBUF_NAMESPACE_ID::internal::ParseTableField entries[]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::AuxiliaryParseTableField aux[]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::ParseTable schema[1]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::FieldMetadata field_metadata[];
  static const ::PROTOBUF_NAMESPACE_ID::internal::SerializationTable serialization_table[];
  static const ::PROTOBUF_NAMESPACE_ID::uint32 offsets[];
};
extern const ::PROTOBUF_NAMESPACE_ID::internal::DescriptorTable descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto;
namespace com {
namespace hitnslab {
namespace dnssecurity {
namespace deeparcher {
namespace api {
namespace proto {
class DomainDnsDetail;
class DomainDnsDetailDefaultTypeInternal;
extern DomainDnsDetailDefaultTypeInternal _DomainDnsDetail_default_instance_;
}  // namespace proto
}  // namespace api
}  // namespace deeparcher
}  // namespace dnssecurity
}  // namespace hitnslab
}  // namespace com
PROTOBUF_NAMESPACE_OPEN
template<> ::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail* Arena::CreateMaybeMessage<::com::hitnslab::dnssecurity::deeparcher::api::proto::DomainDnsDetail>(Arena*);
PROTOBUF_NAMESPACE_CLOSE
namespace com {
namespace hitnslab {
namespace dnssecurity {
namespace deeparcher {
namespace api {
namespace proto {

// ===================================================================

class DomainDnsDetail PROTOBUF_FINAL :
    public ::PROTOBUF_NAMESPACE_ID::Message /* @@protoc_insertion_point(class_definition:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail) */ {
 public:
  inline DomainDnsDetail() : DomainDnsDetail(nullptr) {};
  virtual ~DomainDnsDetail();

  DomainDnsDetail(const DomainDnsDetail& from);
  DomainDnsDetail(DomainDnsDetail&& from) noexcept
    : DomainDnsDetail() {
    *this = ::std::move(from);
  }

  inline DomainDnsDetail& operator=(const DomainDnsDetail& from) {
    CopyFrom(from);
    return *this;
  }
  inline DomainDnsDetail& operator=(DomainDnsDetail&& from) noexcept {
    if (GetArena() == from.GetArena()) {
      if (this != &from) InternalSwap(&from);
    } else {
      CopyFrom(from);
    }
    return *this;
  }

  static const ::PROTOBUF_NAMESPACE_ID::Descriptor* descriptor() {
    return GetDescriptor();
  }
  static const ::PROTOBUF_NAMESPACE_ID::Descriptor* GetDescriptor() {
    return GetMetadataStatic().descriptor;
  }
  static const ::PROTOBUF_NAMESPACE_ID::Reflection* GetReflection() {
    return GetMetadataStatic().reflection;
  }
  static const DomainDnsDetail& default_instance();

  static void InitAsDefaultInstance();  // FOR INTERNAL USE ONLY
  static inline const DomainDnsDetail* internal_default_instance() {
    return reinterpret_cast<const DomainDnsDetail*>(
               &_DomainDnsDetail_default_instance_);
  }
  static constexpr int kIndexInFileMessages =
    0;

  friend void swap(DomainDnsDetail& a, DomainDnsDetail& b) {
    a.Swap(&b);
  }
  inline void Swap(DomainDnsDetail* other) {
    if (other == this) return;
    if (GetArena() == other->GetArena()) {
      InternalSwap(other);
    } else {
      ::PROTOBUF_NAMESPACE_ID::internal::GenericSwap(this, other);
    }
  }
  void UnsafeArenaSwap(DomainDnsDetail* other) {
    if (other == this) return;
    GOOGLE_DCHECK(GetArena() == other->GetArena());
    InternalSwap(other);
  }

  // implements Message ----------------------------------------------

  inline DomainDnsDetail* New() const final {
    return CreateMaybeMessage<DomainDnsDetail>(nullptr);
  }

  DomainDnsDetail* New(::PROTOBUF_NAMESPACE_ID::Arena* arena) const final {
    return CreateMaybeMessage<DomainDnsDetail>(arena);
  }
  void CopyFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) final;
  void MergeFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) final;
  void CopyFrom(const DomainDnsDetail& from);
  void MergeFrom(const DomainDnsDetail& from);
  PROTOBUF_ATTRIBUTE_REINITIALIZES void Clear() final;
  bool IsInitialized() const final;

  size_t ByteSizeLong() const final;
  const char* _InternalParse(const char* ptr, ::PROTOBUF_NAMESPACE_ID::internal::ParseContext* ctx) final;
  ::PROTOBUF_NAMESPACE_ID::uint8* _InternalSerialize(
      ::PROTOBUF_NAMESPACE_ID::uint8* target, ::PROTOBUF_NAMESPACE_ID::io::EpsCopyOutputStream* stream) const final;
  int GetCachedSize() const final { return _cached_size_.Get(); }

  private:
  inline void SharedCtor();
  inline void SharedDtor();
  void SetCachedSize(int size) const final;
  void InternalSwap(DomainDnsDetail* other);
  friend class ::PROTOBUF_NAMESPACE_ID::internal::AnyMetadata;
  static ::PROTOBUF_NAMESPACE_ID::StringPiece FullMessageName() {
    return "com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail";
  }
  protected:
  explicit DomainDnsDetail(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  private:
  static void ArenaDtor(void* object);
  inline void RegisterArenaDtor(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  public:

  ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadata() const final;
  private:
  static ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadataStatic() {
    ::PROTOBUF_NAMESPACE_ID::internal::AssignDescriptors(&::descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto);
    return ::descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto.file_level_metadata[kIndexInFileMessages];
  }

  public:

  // nested types ----------------------------------------------------

  // accessors -------------------------------------------------------

  enum : int {
    kCnamesFieldNumber = 9,
    kFqdnFieldNumber = 1,
    kDomainFieldNumber = 2,
    kIpv4AddrsFieldNumber = 7,
    kIpv6AddrsFieldNumber = 8,
  };
  // repeated string cnames = 9;
  int cnames_size() const;
  private:
  int _internal_cnames_size() const;
  public:
  void clear_cnames();
  const std::string& cnames(int index) const;
  std::string* mutable_cnames(int index);
  void set_cnames(int index, const std::string& value);
  void set_cnames(int index, std::string&& value);
  void set_cnames(int index, const char* value);
  void set_cnames(int index, const char* value, size_t size);
  std::string* add_cnames();
  void add_cnames(const std::string& value);
  void add_cnames(std::string&& value);
  void add_cnames(const char* value);
  void add_cnames(const char* value, size_t size);
  const ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField<std::string>& cnames() const;
  ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField<std::string>* mutable_cnames();
  private:
  const std::string& _internal_cnames(int index) const;
  std::string* _internal_add_cnames();
  public:

  // string fqdn = 1;
  void clear_fqdn();
  const std::string& fqdn() const;
  void set_fqdn(const std::string& value);
  void set_fqdn(std::string&& value);
  void set_fqdn(const char* value);
  void set_fqdn(const char* value, size_t size);
  std::string* mutable_fqdn();
  std::string* release_fqdn();
  void set_allocated_fqdn(std::string* fqdn);
  private:
  const std::string& _internal_fqdn() const;
  void _internal_set_fqdn(const std::string& value);
  std::string* _internal_mutable_fqdn();
  public:

  // string domain = 2;
  void clear_domain();
  const std::string& domain() const;
  void set_domain(const std::string& value);
  void set_domain(std::string&& value);
  void set_domain(const char* value);
  void set_domain(const char* value, size_t size);
  std::string* mutable_domain();
  std::string* release_domain();
  void set_allocated_domain(std::string* domain);
  private:
  const std::string& _internal_domain() const;
  void _internal_set_domain(const std::string& value);
  std::string* _internal_mutable_domain();
  public:

  // bytes ipv4_addrs = 7;
  void clear_ipv4_addrs();
  const std::string& ipv4_addrs() const;
  void set_ipv4_addrs(const std::string& value);
  void set_ipv4_addrs(std::string&& value);
  void set_ipv4_addrs(const char* value);
  void set_ipv4_addrs(const void* value, size_t size);
  std::string* mutable_ipv4_addrs();
  std::string* release_ipv4_addrs();
  void set_allocated_ipv4_addrs(std::string* ipv4_addrs);
  private:
  const std::string& _internal_ipv4_addrs() const;
  void _internal_set_ipv4_addrs(const std::string& value);
  std::string* _internal_mutable_ipv4_addrs();
  public:

  // bytes ipv6_addrs = 8;
  void clear_ipv6_addrs();
  const std::string& ipv6_addrs() const;
  void set_ipv6_addrs(const std::string& value);
  void set_ipv6_addrs(std::string&& value);
  void set_ipv6_addrs(const char* value);
  void set_ipv6_addrs(const void* value, size_t size);
  std::string* mutable_ipv6_addrs();
  std::string* release_ipv6_addrs();
  void set_allocated_ipv6_addrs(std::string* ipv6_addrs);
  private:
  const std::string& _internal_ipv6_addrs() const;
  void _internal_set_ipv6_addrs(const std::string& value);
  std::string* _internal_mutable_ipv6_addrs();
  public:

  // @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail)
 private:
  class _Internal;

  template <typename T> friend class ::PROTOBUF_NAMESPACE_ID::Arena::InternalHelper;
  typedef void InternalArenaConstructable_;
  typedef void DestructorSkippable_;
  ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField<std::string> cnames_;
  ::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr fqdn_;
  ::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr domain_;
  ::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr ipv4_addrs_;
  ::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr ipv6_addrs_;
  mutable ::PROTOBUF_NAMESPACE_ID::internal::CachedSize _cached_size_;
  friend struct ::TableStruct_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto;
};
// ===================================================================


// ===================================================================

#ifdef __GNUC__
  #pragma GCC diagnostic push
  #pragma GCC diagnostic ignored "-Wstrict-aliasing"
#endif  // __GNUC__
// DomainDnsDetail

// string fqdn = 1;
inline void DomainDnsDetail::clear_fqdn() {
  fqdn_.ClearToEmpty(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline const std::string& DomainDnsDetail::fqdn() const {
  // @@protoc_insertion_point(field_get:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.fqdn)
  return _internal_fqdn();
}
inline void DomainDnsDetail::set_fqdn(const std::string& value) {
  _internal_set_fqdn(value);
  // @@protoc_insertion_point(field_set:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.fqdn)
}
inline std::string* DomainDnsDetail::mutable_fqdn() {
  // @@protoc_insertion_point(field_mutable:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.fqdn)
  return _internal_mutable_fqdn();
}
inline const std::string& DomainDnsDetail::_internal_fqdn() const {
  return fqdn_.Get();
}
inline void DomainDnsDetail::_internal_set_fqdn(const std::string& value) {
  
  fqdn_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), value, GetArena());
}
inline void DomainDnsDetail::set_fqdn(std::string&& value) {
  
  fqdn_.Set(
    &::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::move(value), GetArena());
  // @@protoc_insertion_point(field_set_rvalue:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.fqdn)
}
inline void DomainDnsDetail::set_fqdn(const char* value) {
  GOOGLE_DCHECK(value != nullptr);
  
  fqdn_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::string(value),
              GetArena());
  // @@protoc_insertion_point(field_set_char:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.fqdn)
}
inline void DomainDnsDetail::set_fqdn(const char* value,
    size_t size) {
  
  fqdn_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::string(
      reinterpret_cast<const char*>(value), size), GetArena());
  // @@protoc_insertion_point(field_set_pointer:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.fqdn)
}
inline std::string* DomainDnsDetail::_internal_mutable_fqdn() {
  
  return fqdn_.Mutable(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline std::string* DomainDnsDetail::release_fqdn() {
  // @@protoc_insertion_point(field_release:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.fqdn)
  return fqdn_.Release(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline void DomainDnsDetail::set_allocated_fqdn(std::string* fqdn) {
  if (fqdn != nullptr) {
    
  } else {
    
  }
  fqdn_.SetAllocated(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), fqdn,
      GetArena());
  // @@protoc_insertion_point(field_set_allocated:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.fqdn)
}

// string domain = 2;
inline void DomainDnsDetail::clear_domain() {
  domain_.ClearToEmpty(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline const std::string& DomainDnsDetail::domain() const {
  // @@protoc_insertion_point(field_get:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.domain)
  return _internal_domain();
}
inline void DomainDnsDetail::set_domain(const std::string& value) {
  _internal_set_domain(value);
  // @@protoc_insertion_point(field_set:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.domain)
}
inline std::string* DomainDnsDetail::mutable_domain() {
  // @@protoc_insertion_point(field_mutable:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.domain)
  return _internal_mutable_domain();
}
inline const std::string& DomainDnsDetail::_internal_domain() const {
  return domain_.Get();
}
inline void DomainDnsDetail::_internal_set_domain(const std::string& value) {
  
  domain_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), value, GetArena());
}
inline void DomainDnsDetail::set_domain(std::string&& value) {
  
  domain_.Set(
    &::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::move(value), GetArena());
  // @@protoc_insertion_point(field_set_rvalue:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.domain)
}
inline void DomainDnsDetail::set_domain(const char* value) {
  GOOGLE_DCHECK(value != nullptr);
  
  domain_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::string(value),
              GetArena());
  // @@protoc_insertion_point(field_set_char:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.domain)
}
inline void DomainDnsDetail::set_domain(const char* value,
    size_t size) {
  
  domain_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::string(
      reinterpret_cast<const char*>(value), size), GetArena());
  // @@protoc_insertion_point(field_set_pointer:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.domain)
}
inline std::string* DomainDnsDetail::_internal_mutable_domain() {
  
  return domain_.Mutable(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline std::string* DomainDnsDetail::release_domain() {
  // @@protoc_insertion_point(field_release:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.domain)
  return domain_.Release(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline void DomainDnsDetail::set_allocated_domain(std::string* domain) {
  if (domain != nullptr) {
    
  } else {
    
  }
  domain_.SetAllocated(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), domain,
      GetArena());
  // @@protoc_insertion_point(field_set_allocated:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.domain)
}

// bytes ipv4_addrs = 7;
inline void DomainDnsDetail::clear_ipv4_addrs() {
  ipv4_addrs_.ClearToEmpty(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline const std::string& DomainDnsDetail::ipv4_addrs() const {
  // @@protoc_insertion_point(field_get:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv4_addrs)
  return _internal_ipv4_addrs();
}
inline void DomainDnsDetail::set_ipv4_addrs(const std::string& value) {
  _internal_set_ipv4_addrs(value);
  // @@protoc_insertion_point(field_set:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv4_addrs)
}
inline std::string* DomainDnsDetail::mutable_ipv4_addrs() {
  // @@protoc_insertion_point(field_mutable:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv4_addrs)
  return _internal_mutable_ipv4_addrs();
}
inline const std::string& DomainDnsDetail::_internal_ipv4_addrs() const {
  return ipv4_addrs_.Get();
}
inline void DomainDnsDetail::_internal_set_ipv4_addrs(const std::string& value) {
  
  ipv4_addrs_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), value, GetArena());
}
inline void DomainDnsDetail::set_ipv4_addrs(std::string&& value) {
  
  ipv4_addrs_.Set(
    &::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::move(value), GetArena());
  // @@protoc_insertion_point(field_set_rvalue:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv4_addrs)
}
inline void DomainDnsDetail::set_ipv4_addrs(const char* value) {
  GOOGLE_DCHECK(value != nullptr);
  
  ipv4_addrs_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::string(value),
              GetArena());
  // @@protoc_insertion_point(field_set_char:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv4_addrs)
}
inline void DomainDnsDetail::set_ipv4_addrs(const void* value,
    size_t size) {
  
  ipv4_addrs_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::string(
      reinterpret_cast<const char*>(value), size), GetArena());
  // @@protoc_insertion_point(field_set_pointer:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv4_addrs)
}
inline std::string* DomainDnsDetail::_internal_mutable_ipv4_addrs() {
  
  return ipv4_addrs_.Mutable(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline std::string* DomainDnsDetail::release_ipv4_addrs() {
  // @@protoc_insertion_point(field_release:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv4_addrs)
  return ipv4_addrs_.Release(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline void DomainDnsDetail::set_allocated_ipv4_addrs(std::string* ipv4_addrs) {
  if (ipv4_addrs != nullptr) {
    
  } else {
    
  }
  ipv4_addrs_.SetAllocated(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ipv4_addrs,
      GetArena());
  // @@protoc_insertion_point(field_set_allocated:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv4_addrs)
}

// bytes ipv6_addrs = 8;
inline void DomainDnsDetail::clear_ipv6_addrs() {
  ipv6_addrs_.ClearToEmpty(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline const std::string& DomainDnsDetail::ipv6_addrs() const {
  // @@protoc_insertion_point(field_get:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv6_addrs)
  return _internal_ipv6_addrs();
}
inline void DomainDnsDetail::set_ipv6_addrs(const std::string& value) {
  _internal_set_ipv6_addrs(value);
  // @@protoc_insertion_point(field_set:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv6_addrs)
}
inline std::string* DomainDnsDetail::mutable_ipv6_addrs() {
  // @@protoc_insertion_point(field_mutable:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv6_addrs)
  return _internal_mutable_ipv6_addrs();
}
inline const std::string& DomainDnsDetail::_internal_ipv6_addrs() const {
  return ipv6_addrs_.Get();
}
inline void DomainDnsDetail::_internal_set_ipv6_addrs(const std::string& value) {
  
  ipv6_addrs_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), value, GetArena());
}
inline void DomainDnsDetail::set_ipv6_addrs(std::string&& value) {
  
  ipv6_addrs_.Set(
    &::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::move(value), GetArena());
  // @@protoc_insertion_point(field_set_rvalue:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv6_addrs)
}
inline void DomainDnsDetail::set_ipv6_addrs(const char* value) {
  GOOGLE_DCHECK(value != nullptr);
  
  ipv6_addrs_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::string(value),
              GetArena());
  // @@protoc_insertion_point(field_set_char:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv6_addrs)
}
inline void DomainDnsDetail::set_ipv6_addrs(const void* value,
    size_t size) {
  
  ipv6_addrs_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ::std::string(
      reinterpret_cast<const char*>(value), size), GetArena());
  // @@protoc_insertion_point(field_set_pointer:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv6_addrs)
}
inline std::string* DomainDnsDetail::_internal_mutable_ipv6_addrs() {
  
  return ipv6_addrs_.Mutable(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline std::string* DomainDnsDetail::release_ipv6_addrs() {
  // @@protoc_insertion_point(field_release:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv6_addrs)
  return ipv6_addrs_.Release(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArena());
}
inline void DomainDnsDetail::set_allocated_ipv6_addrs(std::string* ipv6_addrs) {
  if (ipv6_addrs != nullptr) {
    
  } else {
    
  }
  ipv6_addrs_.SetAllocated(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), ipv6_addrs,
      GetArena());
  // @@protoc_insertion_point(field_set_allocated:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.ipv6_addrs)
}

// repeated string cnames = 9;
inline int DomainDnsDetail::_internal_cnames_size() const {
  return cnames_.size();
}
inline int DomainDnsDetail::cnames_size() const {
  return _internal_cnames_size();
}
inline void DomainDnsDetail::clear_cnames() {
  cnames_.Clear();
}
inline std::string* DomainDnsDetail::add_cnames() {
  // @@protoc_insertion_point(field_add_mutable:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
  return _internal_add_cnames();
}
inline const std::string& DomainDnsDetail::_internal_cnames(int index) const {
  return cnames_.Get(index);
}
inline const std::string& DomainDnsDetail::cnames(int index) const {
  // @@protoc_insertion_point(field_get:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
  return _internal_cnames(index);
}
inline std::string* DomainDnsDetail::mutable_cnames(int index) {
  // @@protoc_insertion_point(field_mutable:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
  return cnames_.Mutable(index);
}
inline void DomainDnsDetail::set_cnames(int index, const std::string& value) {
  // @@protoc_insertion_point(field_set:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
  cnames_.Mutable(index)->assign(value);
}
inline void DomainDnsDetail::set_cnames(int index, std::string&& value) {
  // @@protoc_insertion_point(field_set:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
  cnames_.Mutable(index)->assign(std::move(value));
}
inline void DomainDnsDetail::set_cnames(int index, const char* value) {
  GOOGLE_DCHECK(value != nullptr);
  cnames_.Mutable(index)->assign(value);
  // @@protoc_insertion_point(field_set_char:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
}
inline void DomainDnsDetail::set_cnames(int index, const char* value, size_t size) {
  cnames_.Mutable(index)->assign(
    reinterpret_cast<const char*>(value), size);
  // @@protoc_insertion_point(field_set_pointer:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
}
inline std::string* DomainDnsDetail::_internal_add_cnames() {
  return cnames_.Add();
}
inline void DomainDnsDetail::add_cnames(const std::string& value) {
  cnames_.Add()->assign(value);
  // @@protoc_insertion_point(field_add:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
}
inline void DomainDnsDetail::add_cnames(std::string&& value) {
  cnames_.Add(std::move(value));
  // @@protoc_insertion_point(field_add:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
}
inline void DomainDnsDetail::add_cnames(const char* value) {
  GOOGLE_DCHECK(value != nullptr);
  cnames_.Add()->assign(value);
  // @@protoc_insertion_point(field_add_char:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
}
inline void DomainDnsDetail::add_cnames(const char* value, size_t size) {
  cnames_.Add()->assign(reinterpret_cast<const char*>(value), size);
  // @@protoc_insertion_point(field_add_pointer:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
}
inline const ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField<std::string>&
DomainDnsDetail::cnames() const {
  // @@protoc_insertion_point(field_list:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
  return cnames_;
}
inline ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField<std::string>*
DomainDnsDetail::mutable_cnames() {
  // @@protoc_insertion_point(field_mutable_list:com.hitnslab.dnssecurity.deeparcher.api.proto.DomainDnsDetail.cnames)
  return &cnames_;
}

#ifdef __GNUC__
  #pragma GCC diagnostic pop
#endif  // __GNUC__

// @@protoc_insertion_point(namespace_scope)

}  // namespace proto
}  // namespace api
}  // namespace deeparcher
}  // namespace dnssecurity
}  // namespace hitnslab
}  // namespace com

// @@protoc_insertion_point(global_scope)

#include <google/protobuf/port_undef.inc>
#endif  // GOOGLE_PROTOBUF_INCLUDED_GOOGLE_PROTOBUF_INCLUDED_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fdomain_5fdns_5fdetail_2eproto
