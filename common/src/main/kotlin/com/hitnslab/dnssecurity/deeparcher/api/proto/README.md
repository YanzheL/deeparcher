# 模块间接口数据结构

本目录下后缀为`.proto`的文件为Protobuf格式的数据结构定义。其中字段详细描述见各`.proto`文件内容。

## 使用方式

`*.proto`文件经过protoc编译器做代码生成后，能生成不同编程语言环境下的序列化与反序列化类。[`generated/`](generated/)目录下有预生成的JAVA、Python、C++语言的代码文件，所使用的protoc版本为`3.12.3`。

具体使用方式请参考[Protobuf 官方文档](https://developers.google.com/protocol-buffers/docs/proto3)。

## 定义与描述

### 被动DNS数据

**Protobuf定义：** [pdns_data.proto](pdns_data.proto)

**类型名：**`PDnsData`

**功能：**用于表示对原始DNS日志数据结构化后得到的被动DNS时间序列。

**属性定义：**

| 字段名       | 类型     | 必填  | 描述                                                         |
| ------------ | -------- | ----- | ------------------------------------------------------------ |
| fqdn         | string   | 是    | 完整域名FQDN                                                 |
| domain       | string   | 是    | 顶级私有域                                                   |
| client_ip    | bytes    | 否/"" | 客户端IP(二进制格式)                                         |
| q_time       | int64    | 是    | 请求时间                                                     |
| q_type       | int32    | 是    | 请求类型，详见[RFC标准定义](https://www.iana.org/assignments/dns-parameters/dns-parameters.xhtml) |
| r_code       | int32    | 是    | 返回状态码，详见[RFC标准定义](https://www.iana.org/assignments/dns-parameters/dns-parameters.xhtml) |
| r_ipv4_addrs | bytes    | 否/"" | 返回的IPv4地址集合(二进制格式)                               |
| r_ipv6_addrs | bytes    | 否/"" | 返回的IPv6地址集合(二进制格式)                               |
| r_cnames     | string[] | 否/[] | 返回的CNAME集合                                              |


### 域名聚合属性 

**Protobuf定义：** [domain_dns_detail.proto](domain_dns_detail.proto)

**类型名：**`DomainDnsDetail`

**功能：**用于表示被动DNS数据流经过Kafka Streams框架按照FQDN字段做聚合操作(GroupBy FQDN)后得到的域名的IPv4集合、IPv6集合、CNAME集合。

**属性定义：**

| 字段名     | 类型     | 必填/默认值 | 描述                                       |
| ---------- | -------- | ----------- | ------------------------------------------ |
| fqdn       | string   | 是          | 完整域名FQDN                               |
| domain     | string   | 是          | 顶级私有域                                 |
| ipv4_addrs | bytes    | 否/""       | 该域名聚合后的所有IPv4地址集合(二进制格式) |
| ipv6_addrs | bytes    | 否/""       | 该域名聚合后的所有IPv6地址集合(二进制格式) |
| cnames     | string[] | 否/[]       | 该域名聚合后的所有CNAME集合                |

### 二维矩阵

**Protobuf定义：** [matrix.proto](matrix.proto)

**功能：**用于表示矩阵。支持的稀疏矩阵格式有[COO](https://en.wikipedia.org/wiki/Sparse_matrix#Coordinate_list_\(COO\))、[CSC](https://en.wikipedia.org/wiki/Sparse_matrix#Compressed_sparse_row_(CSR,_CRS_or_Yale_format))、[CSR](https://en.wikipedia.org/wiki/Sparse_matrix#Compressed_sparse_row_(CSR,_CRS_or_Yale_format))，同时也支持稠密矩阵。

**结构名：**`CooMat`、`CscMat`、`CsrMat`、`DenseMat`

### 图属性

**Protobuf定义：** [graph_attributes.proto](graph_attributes.proto)

**类型名：**`ComponentAttr`

**功能：**存储一个子图的相关信息。

**属性定义：**

| 字段名     | 类型     | 必填 | 描述                   |
| ---------- | -------- | ---- | ---------------------- |
| id         | uint32   | 是   | 子图ID（相对于原图）   |
| components | uint32[] | 是   | 该子图所包含的节点列表 |

**类型名：**`GraphElementsAttrMap`

**功能：**属性值稀疏映射表，以字典的方式来表示图上的节点ID到属性值的稀疏映射。

**属性定义：**

| 字段名   | 类型              | 必填 | 描述                                                         |
| -------- | ----------------- | ---- | ------------------------------------------------------------ |
| data     | map<uint32,bytes> | 是   | 节点ID到属性值的稀疏映射。其中属性值以序列化后的二进制形式存储。该字典中所有的值共享相同的类型标识符(type_url)。使用时可将值和type_url构造为Protobuf `Any`类型，从而读取真实值。 |
| type_url | string            | 是   | Protobuf类型标识符，详见[Protobuf Any类型文档](https://developers.google.com/protocol-buffers/docs/reference/csharp/class/google/protobuf/well-known-types/any)                                     |

### 图结构

**Protobuf定义：** [graph.proto](graph.proto)

**类型名：**`Graph`

**功能：**表示一个稀疏图。

**属性定义：**

| 字段名        | 类型                              | 必填/默认值 | 描述                               |
| ------------- | --------------------------------- | ----------- | ---------------------------------- |
| id            | uint32                            | 是          | 图ID                               |
| nodes         | int32                             | 是          | 节点数                             |
| edges         | int32                             | 是          | 边数                               |
| adj           | CooMat/CscMat/CsrMat/DenseMat     | 是          | （二维）邻接矩阵                   |
| parent_id     | uint32                            | 否/0        | 父图ID（如果本图为其中的子图）     |
| directed      | bool                              | 否/False    | 是否为有向图                       |
| connected     | bool                              | 否/False    | 是否为联通图                       |
| unweighted    | bool                              | 否/False    | 是否为无权图                       |
| node_id_remap | uint32[]                          | 否/[]       | 节点ID重映射表                     |
| node_attrs    | map<string, GraphElementsAttrMap> | 否/{}       | 节点属性名到属性值稀疏映射表的映射 |
| edge_attrs    | map<string, GraphElementsAttrMap> | 否/{}       | 边属性名到属性值稀疏映射表的映射   |
| attributes    | map<string, google.protobuf.Any>  | 否/{}       | 其他关于该图的属性映射             |

### 图更新事件

**Protobuf定义：** [graph_event.proto](graph_event.proto)

**类型名：**`GraphEvent`

**功能：**表示一个图更新事件。

**属性定义：**

| 字段名     | 类型                             | 必填/默认值 | 描述                 |
| ---------- | -------------------------------- | ----------- | -------------------- |
| node1      | uint32                           | 是          | 边节点ID 1           |
| node2      | uint32                           | 是          | 边节点ID 2           |
| action     | Action                           | 否/UPSERT   | 操作类型             |
| attributes | map<string, google.protobuf.Any> | 是          | 本次更新的属性映射表 |

**类型名：**`Action`

**功能：**枚举类型，表示该更新操作的类型

**属性定义：**

| 枚举名 | 值   | 描述                         |
| ------ | ---- | ---------------------------- |
| UPSERT | 0    | 如果该边存在则更新，否则添加 |
| DELETE | 1    | 删除已有的边                 |
| UPDATE | 2    | 更新已有的边                 |
| CREATE | 3    | 添加不存在的边               |

### 已知恶意域名信息

**Protobuf定义：** [known_malicious_domain.proto](known_malicious_domain.proto)

**类型名：**`KnownMaliciousDomain`

**功能：**表示从外部渠道获取到的一条恶意域名信息，如网络爬取、外部数据库导入等等。

**属性定义：**

| 字段名        | 类型     | 必填/默认值 | 描述                                   |
| :------------ | -------- | ----------- | -------------------------------------- |
| timestamp     | int64    | 是          | 获取时间                               |
| source        | string   | 是          | 数据来源，如virustotal.com             |
| method        | string   | 是          | 获取方法，如WEB_CRAWLING               |
| fqdn          | string   | 是          | 完整域名FQDN                           |
| domain        | string   | 是          | 顶级私有域                             |
| url           | string   | 否          | 恶意URL，由数据源提供                  |
| created_at    | int64    | 否          | 在数据源中首次出现的时间，由数据源提供 |
| updated_at    | int64    | 否          | 在数据源中被更新的时间，由数据源提供   |
| service_title | string   | 否          | 恶意服务名称，由数据源提供             |
| ipv4_addrs    | bytes    | 否          | IPv4地址集合，由数据源提供             |
| ipv6_addrs    | bytes    | 否          | IPv6地址集合，由数据源提供             |
| cnames        | string[] | 否          | CNAME地址集合，由数据源提供            |
| type          | string   | 否          | 恶意类型，由数据源提供                 |
| asn           | int32    | 否          | 自治系统编号，由数据源提供             |
| registrar     | string   | 否          | 域名注册商，由数据源提供               |
| remark        | string   | 否          | 备注，由数据源提供                     |
| status        | Status   | 否/UNKNOWN  | 状态，由数据源提供                     |

**类型名：**`Status`

**功能：**枚举类型，表示该恶意域名的状态

**属性定义：**

| 枚举名  | 值   | 描述 |
| ------- | ---- | ---- |
| UNKNOWN | 0    | 未知 |
| ACTIVE  | 1    | 活跃 |
| HOLD    | 2    | 暂停 |
| DEAD    | 3    | 管控 |
| OTHER   | 4    | 其他 |

