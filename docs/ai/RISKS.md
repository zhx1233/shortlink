# RISKS

Last updated: 2026-05-06

## 高风险区域

| 区域 | 风险 | 影响 | 建议 |
| --- | --- | --- | --- |
| 短链跳转热路径 | `ShortLinkServiceImpl.restoreUrl` 同时处理 Redis 正向缓存、Bloom filter、null-cache、DB 回源、统计投递和 HTTP redirect | 高并发下易引入延迟、错误跳转、误 404 或缓存击穿 | 修改前补场景测试；重点覆盖缓存命中、DB 回源、过期链接、不存在链接 |
| 短链生成 | `createShortLink` / `generateSuffix` 依赖 hash、随机 UUID、唯一约束、Bloom filter 和重试上限 | 冲突或 Bloom 状态异常会导致创建失败或重复 | 保持 DB 唯一约束兜底；压测/并发测试后再改算法 |
| 缓存一致性 | 创建、更新、回收站 save/recover/remove 涉及 Redis 正向缓存与 null-cache 删除/重建 | 可能产生旧链接仍跳转、恢复后仍 404、删除后仍可访问 | 所有状态变更都检查 Redis key 删除策略和 DB 事务边界 |
| 分库分表 | ShardingSphere 按 `username`、`gid`、`full_short_url` 路由 | 缺少分片键会广播查询，性能和一致性风险上升 | Mapper/Wrapper 查询尽量携带分片键；统计查询需特别审查 |
| 认证/session | 网关使用请求头 `username`/`token` 查 Redis；下游依赖网关注入用户头 | 绕过网关或伪造 header 可能造成越权 | 下游服务如暴露内网外需增加防护；接口测试覆盖未登录/过期 token |
| 用户/分组创建 | Bloom filter + Redisson lock 防重复 | Bloom 误判或状态丢失会阻塞创建/放大 DB 压力 | 保留 DB 唯一约束；准备 Bloom 重建/降级方案 |
| 统计消息链路 | Redis Stream + consumer + 幂等 key + 多张统计表 | 消费失败、重复消费、延迟队列异常会造成统计不准 | 关注 pending/重试/幂等状态；改动后做重复消费验证 |
| 分布式锁 | 创建短链、跳转回源、gid 迁移、注册等路径使用 Redisson 锁 | 锁粒度或异常路径错误可能造成死锁、性能下降或并发不一致 | 检查 try/finally、锁 key 粒度和超时策略 |
| 聚合模式 | `aggregation` 扫描 admin/project/aggregation 多包 | bean 冲突、配置差异、端口冲突更容易出现 | 聚合模式和拆分模式分别验证启动 |
| 配置/密钥 | YAML 中有本地 DB 密码、AES key、AMap key | 泄漏或误用于生产 | 生产配置应改为环境变量/配置中心；不要扩大暴露范围 |
| 编码问题 | README、注释、部分配置中文出现 mojibake | 修改文案容易进一步损坏用户可见文本 | 编辑文本前确认编码；非必要不碰大段中文注释/README |

## 操作风险

- 后端依赖 MySQL、Redis、Nacos；缺一都会影响启动或核心功能。
- `gateway` 与 `aggregation` 默认端口均为 `8003`。
- Maven `compile` 会触发 Spotless `apply`，可能改写 Java 文件格式/许可证头。
- `npm run lint` 和 `npm run format` 都有自动改写副作用。
- 当前工作树已有多处非本次 onboarding 的修改，后续操作需避免误认为 AI 产生。

## 测试风险

- 自动化测试覆盖不足：没有发现实际 JUnit 用例和前端测试脚本。
- 高风险链路缺少回归保障：短链创建、跳转、回收站、登录、统计消费都应补测试。
- 若暂时不补测试，至少做 API 手工验证和模块编译。

## 待确认风险

- 生产部署是否使用 Nacos 配置中心或仅本地 YAML。
- Redis Stream pending 消息是否有恢复/补偿策略。
- Bloom filter 初始化是否能从 DB 全量重建。
- 前端代理/API baseURL 具体本地运行方式需结合 `vite.config.js` 和 axios 配置继续确认。
