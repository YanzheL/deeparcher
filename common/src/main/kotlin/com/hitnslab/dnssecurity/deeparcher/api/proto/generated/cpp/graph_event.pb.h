// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: com/hitnslab/dnssecurity/deeparcher/api/proto/graph_event.proto

#ifndef GOOGLE_PROTOBUF_INCLUDED_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fgraph_5fevent_2eproto
#define GOOGLE_PROTOBUF_INCLUDED_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fgraph_5fevent_2eproto

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
#include <google/protobuf/map.h>  // IWYU pragma: export
#include <google/protobuf/map_entry.h>
#include <google/protobuf/map_field_inl.h>
#include <google/protobuf/generated_enum_reflection.h>
#include <google/protobuf/unknown_field_set.h>
#include <google/protobuf/any.pb.h>
// @@protoc_insertion_point(includes)
#include <google/protobuf/port_def.inc>
#define PROTOBUF_INTERNAL_EXPORT_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fgraph_5fevent_2eproto
PROTOBUF_NAMESPACE_OPEN
namespace internal {
class AnyMetadata;
}  // namespace internal
PROTOBUF_NAMESPACE_CLOSE

// Internal implementation detail -- do not use these members.
struct TableStruct_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fgraph_5fevent_2eproto {
  static const ::PROTOBUF_NAMESPACE_ID::internal::ParseTableField entries[]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::AuxiliaryParseTableField aux[]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::ParseTable schema[2]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::FieldMetadata field_metadata[];
  static const ::PROTOBUF_NAMESPACE_ID::internal::SerializationTable serialization_table[];
  static const ::PROTOBUF_NAMESPACE_ID::uint32 offsets[];
};
extern const ::PROTOBUF_NAMESPACE_ID::internal::DescriptorTable descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fgraph_5fevent_2eproto;
namespace com {
namespace hitnslab {
namespace dnssecurity {
namespace deeparcher {
namespace api {
namespace proto {
class GraphEvent;
class GraphEventDefaultTypeInternal;
extern GraphEventDefaultTypeInternal _GraphEvent_default_instance_;
class GraphEvent_AttributesEntry_DoNotUse;
class GraphEvent_AttributesEntry_DoNotUseDefaultTypeInternal;
extern GraphEvent_AttributesEntry_DoNotUseDefaultTypeInternal _GraphEvent_AttributesEntry_DoNotUse_default_instance_;
}  // namespace proto
}  // namespace api
}  // namespace deeparcher
}  // namespace dnssecurity
}  // namespace hitnslab
}  // namespace com
PROTOBUF_NAMESPACE_OPEN
template<> ::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent* Arena::CreateMaybeMessage<::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent>(Arena*);
template<> ::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_AttributesEntry_DoNotUse* Arena::CreateMaybeMessage<::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_AttributesEntry_DoNotUse>(Arena*);
PROTOBUF_NAMESPACE_CLOSE
namespace com {
namespace hitnslab {
namespace dnssecurity {
namespace deeparcher {
namespace api {
namespace proto {

enum GraphEvent_Action : int {
  GraphEvent_Action_UPSERT = 0,
  GraphEvent_Action_DELETE = 1,
  GraphEvent_Action_UPDATE = 2,
  GraphEvent_Action_CREATE = 3,
  GraphEvent_Action_GraphEvent_Action_INT_MIN_SENTINEL_DO_NOT_USE_ = std::numeric_limits<::PROTOBUF_NAMESPACE_ID::int32>::min(),
  GraphEvent_Action_GraphEvent_Action_INT_MAX_SENTINEL_DO_NOT_USE_ = std::numeric_limits<::PROTOBUF_NAMESPACE_ID::int32>::max()
};
bool GraphEvent_Action_IsValid(int value);
constexpr GraphEvent_Action GraphEvent_Action_Action_MIN = GraphEvent_Action_UPSERT;
constexpr GraphEvent_Action GraphEvent_Action_Action_MAX = GraphEvent_Action_CREATE;
constexpr int GraphEvent_Action_Action_ARRAYSIZE = GraphEvent_Action_Action_MAX + 1;

const ::PROTOBUF_NAMESPACE_ID::EnumDescriptor* GraphEvent_Action_descriptor();
template<typename T>
inline const std::string& GraphEvent_Action_Name(T enum_t_value) {
  static_assert(::std::is_same<T, GraphEvent_Action>::value ||
    ::std::is_integral<T>::value,
    "Incorrect type passed to function GraphEvent_Action_Name.");
  return ::PROTOBUF_NAMESPACE_ID::internal::NameOfEnum(
    GraphEvent_Action_descriptor(), enum_t_value);
}
inline bool GraphEvent_Action_Parse(
    ::PROTOBUF_NAMESPACE_ID::ConstStringParam name, GraphEvent_Action* value) {
  return ::PROTOBUF_NAMESPACE_ID::internal::ParseNamedEnum<GraphEvent_Action>(
    GraphEvent_Action_descriptor(), name, value);
}
// ===================================================================

class GraphEvent_AttributesEntry_DoNotUse : public ::PROTOBUF_NAMESPACE_ID::internal::MapEntry<GraphEvent_AttributesEntry_DoNotUse, 
    std::string, PROTOBUF_NAMESPACE_ID::Any,
    ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::TYPE_STRING,
    ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::TYPE_MESSAGE,
    0 > {
public:
  typedef ::PROTOBUF_NAMESPACE_ID::internal::MapEntry<GraphEvent_AttributesEntry_DoNotUse, 
    std::string, PROTOBUF_NAMESPACE_ID::Any,
    ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::TYPE_STRING,
    ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::TYPE_MESSAGE,
    0 > SuperType;
  GraphEvent_AttributesEntry_DoNotUse();
  explicit GraphEvent_AttributesEntry_DoNotUse(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  void MergeFrom(const GraphEvent_AttributesEntry_DoNotUse& other);
  static const GraphEvent_AttributesEntry_DoNotUse* internal_default_instance() { return reinterpret_cast<const GraphEvent_AttributesEntry_DoNotUse*>(&_GraphEvent_AttributesEntry_DoNotUse_default_instance_); }
  static bool ValidateKey(std::string* s) {
    return ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::VerifyUtf8String(s->data(), static_cast<int>(s->size()), ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::PARSE, "com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent.AttributesEntry.key");
 }
  static bool ValidateValue(void*) { return true; }
  void MergeFrom(const ::PROTOBUF_NAMESPACE_ID::Message& other) final;
  ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadata() const final;
  private:
  static ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadataStatic() {
    ::PROTOBUF_NAMESPACE_ID::internal::AssignDescriptors(&::descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fgraph_5fevent_2eproto);
    return ::descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fgraph_5fevent_2eproto.file_level_metadata[0];
  }

  public:
};

// -------------------------------------------------------------------

class GraphEvent PROTOBUF_FINAL :
    public ::PROTOBUF_NAMESPACE_ID::Message /* @@protoc_insertion_point(class_definition:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent) */ {
 public:
  inline GraphEvent() : GraphEvent(nullptr) {};
  virtual ~GraphEvent();

  GraphEvent(const GraphEvent& from);
  GraphEvent(GraphEvent&& from) noexcept
    : GraphEvent() {
    *this = ::std::move(from);
  }

  inline GraphEvent& operator=(const GraphEvent& from) {
    CopyFrom(from);
    return *this;
  }
  inline GraphEvent& operator=(GraphEvent&& from) noexcept {
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
  static const GraphEvent& default_instance();

  static void InitAsDefaultInstance();  // FOR INTERNAL USE ONLY
  static inline const GraphEvent* internal_default_instance() {
    return reinterpret_cast<const GraphEvent*>(
               &_GraphEvent_default_instance_);
  }
  static constexpr int kIndexInFileMessages =
    1;

  friend void swap(GraphEvent& a, GraphEvent& b) {
    a.Swap(&b);
  }
  inline void Swap(GraphEvent* other) {
    if (other == this) return;
    if (GetArena() == other->GetArena()) {
      InternalSwap(other);
    } else {
      ::PROTOBUF_NAMESPACE_ID::internal::GenericSwap(this, other);
    }
  }
  void UnsafeArenaSwap(GraphEvent* other) {
    if (other == this) return;
    GOOGLE_DCHECK(GetArena() == other->GetArena());
    InternalSwap(other);
  }

  // implements Message ----------------------------------------------

  inline GraphEvent* New() const final {
    return CreateMaybeMessage<GraphEvent>(nullptr);
  }

  GraphEvent* New(::PROTOBUF_NAMESPACE_ID::Arena* arena) const final {
    return CreateMaybeMessage<GraphEvent>(arena);
  }
  void CopyFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) final;
  void MergeFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) final;
  void CopyFrom(const GraphEvent& from);
  void MergeFrom(const GraphEvent& from);
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
  void InternalSwap(GraphEvent* other);
  friend class ::PROTOBUF_NAMESPACE_ID::internal::AnyMetadata;
  static ::PROTOBUF_NAMESPACE_ID::StringPiece FullMessageName() {
    return "com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent";
  }
  protected:
  explicit GraphEvent(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  private:
  static void ArenaDtor(void* object);
  inline void RegisterArenaDtor(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  public:

  ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadata() const final;
  private:
  static ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadataStatic() {
    ::PROTOBUF_NAMESPACE_ID::internal::AssignDescriptors(&::descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fgraph_5fevent_2eproto);
    return ::descriptor_table_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fgraph_5fevent_2eproto.file_level_metadata[kIndexInFileMessages];
  }

  public:

  // nested types ----------------------------------------------------


  typedef GraphEvent_Action Action;
  static constexpr Action UPSERT =
    GraphEvent_Action_UPSERT;
  static constexpr Action DELETE =
    GraphEvent_Action_DELETE;
  static constexpr Action UPDATE =
    GraphEvent_Action_UPDATE;
  static constexpr Action CREATE =
    GraphEvent_Action_CREATE;
  static inline bool Action_IsValid(int value) {
    return GraphEvent_Action_IsValid(value);
  }
  static constexpr Action Action_MIN =
    GraphEvent_Action_Action_MIN;
  static constexpr Action Action_MAX =
    GraphEvent_Action_Action_MAX;
  static constexpr int Action_ARRAYSIZE =
    GraphEvent_Action_Action_ARRAYSIZE;
  static inline const ::PROTOBUF_NAMESPACE_ID::EnumDescriptor*
  Action_descriptor() {
    return GraphEvent_Action_descriptor();
  }
  template<typename T>
  static inline const std::string& Action_Name(T enum_t_value) {
    static_assert(::std::is_same<T, Action>::value ||
      ::std::is_integral<T>::value,
      "Incorrect type passed to function Action_Name.");
    return GraphEvent_Action_Name(enum_t_value);
  }
  static inline bool Action_Parse(::PROTOBUF_NAMESPACE_ID::ConstStringParam name,
      Action* value) {
    return GraphEvent_Action_Parse(name, value);
  }

  // accessors -------------------------------------------------------

  enum : int {
    kAttributesFieldNumber = 4,
    kNode1FieldNumber = 1,
    kNode2FieldNumber = 2,
    kActionFieldNumber = 3,
  };
  // map<string, .google.protobuf.Any> attributes = 4;
  int attributes_size() const;
  private:
  int _internal_attributes_size() const;
  public:
  void clear_attributes();
  private:
  const ::PROTOBUF_NAMESPACE_ID::Map< std::string, PROTOBUF_NAMESPACE_ID::Any >&
      _internal_attributes() const;
  ::PROTOBUF_NAMESPACE_ID::Map< std::string, PROTOBUF_NAMESPACE_ID::Any >*
      _internal_mutable_attributes();
  public:
  const ::PROTOBUF_NAMESPACE_ID::Map< std::string, PROTOBUF_NAMESPACE_ID::Any >&
      attributes() const;
  ::PROTOBUF_NAMESPACE_ID::Map< std::string, PROTOBUF_NAMESPACE_ID::Any >*
      mutable_attributes();

  // uint32 node1 = 1;
  void clear_node1();
  ::PROTOBUF_NAMESPACE_ID::uint32 node1() const;
  void set_node1(::PROTOBUF_NAMESPACE_ID::uint32 value);
  private:
  ::PROTOBUF_NAMESPACE_ID::uint32 _internal_node1() const;
  void _internal_set_node1(::PROTOBUF_NAMESPACE_ID::uint32 value);
  public:

  // uint32 node2 = 2;
  void clear_node2();
  ::PROTOBUF_NAMESPACE_ID::uint32 node2() const;
  void set_node2(::PROTOBUF_NAMESPACE_ID::uint32 value);
  private:
  ::PROTOBUF_NAMESPACE_ID::uint32 _internal_node2() const;
  void _internal_set_node2(::PROTOBUF_NAMESPACE_ID::uint32 value);
  public:

  // .com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent.Action action = 3;
  void clear_action();
  ::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action action() const;
  void set_action(::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action value);
  private:
  ::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action _internal_action() const;
  void _internal_set_action(::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action value);
  public:

  // @@protoc_insertion_point(class_scope:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent)
 private:
  class _Internal;

  template <typename T> friend class ::PROTOBUF_NAMESPACE_ID::Arena::InternalHelper;
  typedef void InternalArenaConstructable_;
  typedef void DestructorSkippable_;
  ::PROTOBUF_NAMESPACE_ID::internal::MapField<
      GraphEvent_AttributesEntry_DoNotUse,
      std::string, PROTOBUF_NAMESPACE_ID::Any,
      ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::TYPE_STRING,
      ::PROTOBUF_NAMESPACE_ID::internal::WireFormatLite::TYPE_MESSAGE,
      0 > attributes_;
  ::PROTOBUF_NAMESPACE_ID::uint32 node1_;
  ::PROTOBUF_NAMESPACE_ID::uint32 node2_;
  int action_;
  mutable ::PROTOBUF_NAMESPACE_ID::internal::CachedSize _cached_size_;
  friend struct ::TableStruct_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fgraph_5fevent_2eproto;
};
// ===================================================================


// ===================================================================

#ifdef __GNUC__
  #pragma GCC diagnostic push
  #pragma GCC diagnostic ignored "-Wstrict-aliasing"
#endif  // __GNUC__
// -------------------------------------------------------------------

// GraphEvent

// uint32 node1 = 1;
inline void GraphEvent::clear_node1() {
  node1_ = 0u;
}
inline ::PROTOBUF_NAMESPACE_ID::uint32 GraphEvent::_internal_node1() const {
  return node1_;
}
inline ::PROTOBUF_NAMESPACE_ID::uint32 GraphEvent::node1() const {
  // @@protoc_insertion_point(field_get:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent.node1)
  return _internal_node1();
}
inline void GraphEvent::_internal_set_node1(::PROTOBUF_NAMESPACE_ID::uint32 value) {
  
  node1_ = value;
}
inline void GraphEvent::set_node1(::PROTOBUF_NAMESPACE_ID::uint32 value) {
  _internal_set_node1(value);
  // @@protoc_insertion_point(field_set:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent.node1)
}

// uint32 node2 = 2;
inline void GraphEvent::clear_node2() {
  node2_ = 0u;
}
inline ::PROTOBUF_NAMESPACE_ID::uint32 GraphEvent::_internal_node2() const {
  return node2_;
}
inline ::PROTOBUF_NAMESPACE_ID::uint32 GraphEvent::node2() const {
  // @@protoc_insertion_point(field_get:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent.node2)
  return _internal_node2();
}
inline void GraphEvent::_internal_set_node2(::PROTOBUF_NAMESPACE_ID::uint32 value) {
  
  node2_ = value;
}
inline void GraphEvent::set_node2(::PROTOBUF_NAMESPACE_ID::uint32 value) {
  _internal_set_node2(value);
  // @@protoc_insertion_point(field_set:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent.node2)
}

// .com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent.Action action = 3;
inline void GraphEvent::clear_action() {
  action_ = 0;
}
inline ::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action GraphEvent::_internal_action() const {
  return static_cast< ::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action >(action_);
}
inline ::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action GraphEvent::action() const {
  // @@protoc_insertion_point(field_get:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent.action)
  return _internal_action();
}
inline void GraphEvent::_internal_set_action(::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action value) {
  
  action_ = value;
}
inline void GraphEvent::set_action(::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action value) {
  _internal_set_action(value);
  // @@protoc_insertion_point(field_set:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent.action)
}

// map<string, .google.protobuf.Any> attributes = 4;
inline int GraphEvent::_internal_attributes_size() const {
  return attributes_.size();
}
inline int GraphEvent::attributes_size() const {
  return _internal_attributes_size();
}
inline const ::PROTOBUF_NAMESPACE_ID::Map< std::string, PROTOBUF_NAMESPACE_ID::Any >&
GraphEvent::_internal_attributes() const {
  return attributes_.GetMap();
}
inline const ::PROTOBUF_NAMESPACE_ID::Map< std::string, PROTOBUF_NAMESPACE_ID::Any >&
GraphEvent::attributes() const {
  // @@protoc_insertion_point(field_map:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent.attributes)
  return _internal_attributes();
}
inline ::PROTOBUF_NAMESPACE_ID::Map< std::string, PROTOBUF_NAMESPACE_ID::Any >*
GraphEvent::_internal_mutable_attributes() {
  return attributes_.MutableMap();
}
inline ::PROTOBUF_NAMESPACE_ID::Map< std::string, PROTOBUF_NAMESPACE_ID::Any >*
GraphEvent::mutable_attributes() {
  // @@protoc_insertion_point(field_mutable_map:com.hitnslab.dnssecurity.deeparcher.api.proto.GraphEvent.attributes)
  return _internal_mutable_attributes();
}

#ifdef __GNUC__
  #pragma GCC diagnostic pop
#endif  // __GNUC__
// -------------------------------------------------------------------


// @@protoc_insertion_point(namespace_scope)

}  // namespace proto
}  // namespace api
}  // namespace deeparcher
}  // namespace dnssecurity
}  // namespace hitnslab
}  // namespace com

PROTOBUF_NAMESPACE_OPEN

template <> struct is_proto_enum< ::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action> : ::std::true_type {};
template <>
inline const EnumDescriptor* GetEnumDescriptor< ::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action>() {
  return ::com::hitnslab::dnssecurity::deeparcher::api::proto::GraphEvent_Action_descriptor();
}

PROTOBUF_NAMESPACE_CLOSE

// @@protoc_insertion_point(global_scope)

#include <google/protobuf/port_undef.inc>
#endif  // GOOGLE_PROTOBUF_INCLUDED_GOOGLE_PROTOBUF_INCLUDED_com_2fhitnslab_2fdnssecurity_2fdeeparcher_2fapi_2fproto_2fgraph_5fevent_2eproto
