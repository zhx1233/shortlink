# PROJECT_MEMORY

Last updated: 2026-05-06

## 项目目标

- 确定：这是一个 SaaS 短链接系统，提供短链创建、批量创建、跳转还原、访问统计、回收站、用户登录、分组管理和前端控制台。
- 确定：后端采用 Java 多模块 Maven 项目，包含拆分服务部署模式和 `aggregation` 聚合部署模式。
- 推测：项目主要用于学习/演示高并发短链系统设计，README 和源码注释中大量出现课程/面试导向说明。
- 待确认：生产环境部署方式、真实域名、CI/CD、线上配置与本地配置是否一致。

## 技术栈

- 后端：Java 17、Spring Boot 3.0.7、Spring Cloud 2022.0.3、Spring Cloud Alibaba 2022.0.0.0-RC2。
- 后端模块：Maven parent `shortlink-all`，子模块 `admin`、`project`、`gateway`、`aggregation`。
- 数据库：MySQL，MyBatis-Plus 3.5.3.1，ShardingSphere JDBC 5.3.2。
- 缓存与并发：Redis、Spring Data Redis、Redisson、RBloomFilter、分布式锁、Redis Stream、Redisson delayed queue。
- 服务发现/网关：Nacos discovery、Spring Cloud Gateway、OpenFeign。
- 限流/保护：`project` 使用 Sentinel；`admin` 使用 Redis Lua 用户访问频控。
- 工具库：fastjson2、Hutool、Dozer、jsoup、EasyExcel、Guava。
- 前端：Vue 3.3、Vite 4.4、Vue Router、Vuex、Element Plus、View UI Plus、Axios、ECharts 4.8、qrcode、sortablejs。
- 代码格式：Spotless Maven plugin，在 Maven `compile` 阶段绑定 `apply`。

## 运行形态

- `admin`：后管服务，端口 `8002`，负责用户、分组、登录、后管 API facade，通过 Feign 调用实际短链服务。
- `project`：短链核心服务，端口 `8001`，负责短链生成、跳转、回收站、统计写入与查询。
- `gateway`：网关服务，端口 `8003`，通过 Nacos 路由到 `admin` / `project`，并做 Redis token 校验。
- `aggregation`：聚合服务，端口 `8003`，扫描 `admin` + `project` + `aggregation` 包，适合单体式启动/演示。
- `console-vue`：Vue 管理控制台。

## 外部依赖

- MySQL：`127.0.0.1:3306`，数据库名 `link`。
- Redis：`127.0.0.1:6379`。
- Nacos：`127.0.0.1:8848`。
- AMap key：配置于 `project` / `aggregation` 的地域统计配置中。
- 默认短链域名：`project` 为 `nurl.ink:8001`，`aggregation` 为 `nurl.ink:8003`。

## 全局业务规则

- 短链跳转通过 `/{short-uri}` 进入 `project`，优先查 Redis 正向缓存，再查 Bloom filter/null-cache/DB，最后重定向或进入 notfound 页面。
- 短链生成需要校验目标域名白名单；`project` 默认开启白名单，`aggregation` 当前配置为关闭。
- 登录 token 存储在 Redis hash：`short-link:login:{username}`，网关读取请求头 `username` / `token` 校验。
- 用户、分组、短链创建使用 Bloom filter 与 Redisson 锁降低重复创建/缓存穿透风险。
- 访问统计通过 Redis Stream 异步写入，包含 PV、UV、UIP、浏览器、系统、设备、网络、地域等维度。

## 全局代码规则

- 小步修改，禁止无关重构。
- 修改前先确认当前工作树已有用户改动，不能回退他人改动。
- 高风险链路需要优先补测试或至少做 targeted manual/API 验证。
- 注意中文编码问题：README、注释、配置中的部分中文已呈现 mojibake，编辑文本时要谨慎。

## 高风险模块

- 短链跳转链路：缓存、Bloom filter、DB 回源、null-cache、统计投递都在同一热路径。
- 短链生成链路：hash/base62、唯一约束、Bloom filter、重试次数和缓存预热。
- Redis/MySQL 一致性：创建、更新、回收站、恢复/删除都涉及缓存失效和 DB 状态同步。
- 分库分表：`t_link` 按 `gid`，`t_link_goto` 按 `full_short_url`，`t_user`/`t_group` 按 `username`。
- 认证与权限：token 完全依赖 Redis + 请求头，需关注网关绕过和 header 信任边界。
- 统计消费：Redis Stream、幂等 key、分布式锁和多张统计表之间可能出现重复/延迟/丢失。
- 聚合模式：`aggregation` 同时扫描 admin/project，可能出现 bean/config 差异或端口冲突。
- 本地敏感配置：YAML 中存在 DB 密码、AES key、AMap key 等本地/演示配置。

## 当前项目状态

- 确定：`docs/ai` 已存在，并在本次 onboarding 中按项目 Skill 重新合并更新。
- 确定：项目根目录已存在 `.codex/skills`，包含 `project-onboarding`、`project-optimization-workflow`、`project-memory-updater`。
- 确定：自动化测试覆盖很弱，只发现一个 `UserTableShardingTest` SQL 生成工具类，没有发现 JUnit 测试或前端测试脚本。
- 待确认：本机 Maven/npm 依赖是否已安装完毕；本地 MySQL/Redis/Nacos 当前是否运行。

## 项目地图摘要

- `pom.xml`：Maven parent 与版本管理。
- `admin/`：用户、分组、登录、后管 facade。
- `project/`：短链核心、跳转、回收站、统计。
- `gateway/`：路由与 token 校验。
- `aggregation/`：聚合启动入口。
- `console-vue/`：前端控制台。
- `resources/database/`：建表与初始化数据。
- `docs/ai/`：AI 项目记忆。
