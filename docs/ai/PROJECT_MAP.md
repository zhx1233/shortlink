# PROJECT_MAP

Last updated: 2026-05-06

## 顶层结构

| 路径 | 作用 | 重要性 |
| --- | --- | --- |
| `pom.xml` | Maven parent，统一版本、模块、编译和 Spotless 配置 | 高 |
| `admin/` | 后管服务：用户、登录、分组、后管 API facade | 高 |
| `project/` | 短链核心服务：创建、跳转、回收站、统计 | 高 |
| `gateway/` | Spring Cloud Gateway 路由与 token 校验 | 高 |
| `aggregation/` | 聚合启动服务，合并 admin/project 能力 | 中 |
| `console-vue/` | Vue 3 管理控制台 | 高 |
| `resources/database/` | MySQL DDL 与初始化数据 | 高 |
| `format/` | Spotless license/formatter 资源 | 中 |
| `.codex/skills/` | 项目级 Codex Skills | 中 |
| `docs/ai/` | AI 项目记忆 | 中 |

## 后端模块

### admin

- 入口：`admin/src/main/java/com/nageoffer/shortlink/admin/ShortLinkAdminApplication.java`
- 端口：`8002`
- 主要职责：用户注册/登录、用户信息、分组管理、后管短链 API、回收站 facade、统计 facade。
- 关键控制器：`UserController`、`GroupController`、`ShortLinkController`、`RecycleBinController`、`ShortLinkStatsController`、`UrlTitleController`。
- 关键服务：`UserServiceImpl`、`GroupServiceImpl`、`RecycleBinServiceImpl`。
- 跨模块调用：`ShortLinkActualRemoteService` 使用 OpenFeign 调用 `short-link-project`。
- 横切逻辑：`UserTransmitFilter`、`UserFlowRiskControlFilter`、Redis Lua 限流。

### project

- 入口：`project/src/main/java/com/nageoffer/shortlink/project/ShortLinkApplication.java`
- 端口：`8001`
- 主要职责：短链核心业务。
- 关键控制器：`ShortLinkController`、`RecycleBinController`、`ShortLinkStatsController`、`UrlTitleController`、`ShortLinkNotfoundController`。
- 关键服务：`ShortLinkServiceImpl`、`RecycleBinServiceImpl`、`ShortLinkStatsServiceImpl`、`LinkStatsTodayServiceImpl`。
- 消息链路：`ShortLinkStatsSaveProducer`、`ShortLinkStatsSaveConsumer`、`DelayShortLinkStatsProducer`、`DelayShortLinkStatsConsumer`、`MessageQueueIdempotentHandler`。
- 配置：`RBloomFilterConfiguration`、`RedisStreamConfiguration`、`ShortLinkStatsStreamInitializeTask`、`SentinelRuleConfig`、`GotoDomainWhiteListConfiguration`。

### gateway

- 入口：`gateway/src/main/java/com/nageoffer/shortlink/gateway/GatewayServiceApplication.java`
- 端口：`8003`
- `application-dev.yaml`：路由到 `short-link-admin` 与 `short-link-project`。
- `application-aggregation.yaml`：路由到 `short-link-aggregation`。
- 核心过滤器：`TokenValidateGatewayFilterFactory`，校验 Redis token 并转发 `userId` / `realName`。

### aggregation

- 入口：`aggregation/src/main/java/com/nageoffer/shortlink/aggregation/AggregationServiceApplication.java`
- 端口：`8003`
- 扫描包：`admin`、`project`、`aggregation`。
- 附加能力：Actuator、Prometheus metrics、demo-mode blacklist。

## 前端模块

- 入口：`console-vue/src/main.js`
- 根组件：`console-vue/src/App.vue`
- 路由：`console-vue/src/router/index.js`
- 状态：`console-vue/src/store/index.js`
- 认证：`console-vue/src/core/auth.js`
- API：`console-vue/src/api/axios.js`、`src/api/modules/*`
- 主要页面：`LoginIndex.vue`、`HomeIndex.vue`、`MySpaceIndex.vue`、`RecycleBinIndex.vue`、`MineIndex.vue`。
- 短链组件：`views/mySpace/components/createLink`、`editLink`、`chartsInfo`、`qrCode`。

## 数据与配置

- DDL：`resources/database/link.sql`
- 初始化数据：`resources/database/link-data.sql`
- admin 分片：`admin/src/main/resources/shardingsphere-config-dev.yaml`
  - `t_user` by `username`
  - `t_group` by `username`
  - `t_user.phone` / `mail` AES 加密
- project 分片：`project/src/main/resources/shardingsphere-config-dev.yaml`
  - `t_link` by `gid`
  - `t_link_goto` by `full_short_url`
  - `t_group` by `username`
- aggregation 分片：`aggregation/src/main/resources/shardingsphere-config-dev.yaml`
  - 合并 user/group/link/link_goto 规则和用户字段加密。

## 项目级 Skills

- `.codex/skills/project-onboarding/SKILL.md`
- `.codex/skills/project-optimization-workflow/SKILL.md`
- `.codex/skills/project-memory-updater/SKILL.md`
