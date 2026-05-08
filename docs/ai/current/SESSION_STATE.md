# SESSION_STATE

Last updated: 2026-05-06

## 当前目标

使用项目级 `project-onboarding` Skill 重新初始化/校准项目理解：读取项目结构、已有 `/docs/ai/` 记忆文件、技术栈、命令、核心模块和高风险区域，并更新指定记忆文件。不修改业务代码。

## 本轮已完成

- 读取 `.codex/skills/project-onboarding/SKILL.md`，确认本轮按项目 onboarding 流程执行。
- 读取项目结构：`rg --files`。
- 读取已有记忆：
  - `docs/ai/PROJECT_MEMORY.md`
  - `docs/ai/COMMANDS.md`
  - `docs/ai/RISKS.md`
  - `docs/ai/PROJECT_MAP.md`
  - `docs/ai/current/SESSION_STATE.md`
- 复核关键配置：
  - root `pom.xml`
  - `admin/project/gateway/aggregation` POM
  - `console-vue/package.json`
  - 各模块 `application.yaml`
  - gateway `application-dev.yaml` / `application-aggregation.yaml`
- 扫描入口、控制器、Feign、Sentinel、测试文件。
- 更新指定记忆文件：
  - `docs/ai/PROJECT_MEMORY.md`
  - `docs/ai/COMMANDS.md`
  - `docs/ai/RISKS.md`
  - `docs/ai/PROJECT_MAP.md`
  - `docs/ai/current/SESSION_STATE.md`

## 当前理解

- `admin`：后管服务，管理用户、分组、登录和后管 API facade。
- `project`：短链核心服务，负责创建、跳转、回收站和统计。
- `gateway`：网关服务，使用 Redis 中的登录 token 做鉴权并路由。
- `aggregation`：聚合服务，同时扫描 admin/project，适合简化启动或演示。
- `console-vue`：Vue 3 + Vite 前端控制台。

## 当前风险

- 短链跳转、短链创建、缓存一致性、分库分表、认证/session、统计消费是最高风险区域。
- 自动化测试薄弱，当前只发现一个 SQL 生成型测试工具类。
- `gateway` 和 `aggregation` 默认端口冲突。
- Maven `compile`、前端 `lint/format` 都可能改写文件。
- 源码和文档存在 mojibake，编辑中文文本需谨慎。

## 本轮禁止事项执行情况

- 未修改业务代码。
- 未执行破坏性命令。
- 未运行会改写业务代码的格式化/修复命令。
- 只更新了用户指定的 `docs/ai` 记忆文件。

## 下一步建议

- 确认本地 MySQL、Redis、Nacos 是否运行。
- 选择拆分服务模式还是 `aggregation` 聚合模式。
- 在改业务前优先补充短链创建/跳转/回收站/登录/统计链路的回归测试。
- 如只做环境验证，可先运行 `.\mvnw.cmd -pl aggregation -am test` 和前端 `npm run build`。
