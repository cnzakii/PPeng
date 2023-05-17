# ”烹烹“—PPeng

烹烹（PPeng）是一款菜谱应用程序，旨在为用户提供一个平台，让他们能够分享自己的菜谱创意，同时获得他人分享的菜谱和专业的烹饪学习教程。该应用通过社区互动和专业资源的结合，为用户带来丰富的烹饪体验和灵感。



## 系统要求

为了确保烹烹（PPeng）应用后端的正常运行，建议至少使用一台配置为`8核8G`的服务器或服务器集群。

ps: `4核4G`被完全干爆，最终使用了`16核8G`的服务器才完全启动😭，下图是**没有任何请求进入时**的服务器性能使用情况：

<img src="./assets/server_usage.png" style="zoom:40%;" />





## 技术栈

### 前端

- 框架

  `uni-app`

- UI库

  `vantui`

- 图标库

  `iconfont`

### 后端

- 主体框架 

  `Spring Boot`,`Spring Cloud`,`Spring Cloud Alibaba`

- 微服务相关

  - 服务注册与发现,配置中心: `Nacos`
  - 服务网关: `Spring Cloud Gateway`
  - 熔断与限流: `Sentinel`
  - 远程调用: `Spring Cloud OpenFeign`

- 数据库相关

  - 数据库: `MySQL`
  - 数据库连接池: `Druid`
  - 数据库框架: `MyBatis-Plus`

- 缓存相关

  - 分布式缓存: `Redis`
  - 本地缓存: `Caffeine`
  - 缓存同步: `Canal`

- 其他

  - 鉴权框架: `Sa-Token`
  - 工具包: `Hutool`
  - 内容审核和菜品识别: [`百度API`](https://ai.baidu.com/)
  - 消息队列: `RabbitMQ`



## 架构概述

## 模块说明

## 快速开始

## 部署

## 使用示例


