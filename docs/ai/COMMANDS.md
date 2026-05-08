# COMMANDS

Last updated: 2026-05-06

默认从 `E:\JavaWeb\shortlink\shortlink` 执行后端命令；前端命令从 `E:\JavaWeb\shortlink\shortlink\console-vue` 执行。

## 后端命令

| 类型 | 命令 | 来源 | 确定性 |
| --- | --- | --- | --- |
| 编译全部模块 | `.\mvnw.cmd clean compile` | root `pom.xml` + Maven wrapper | 确定 |
| 构建全部模块 | `.\mvnw.cmd clean package` | root `pom.xml` + Maven wrapper | 确定 |
| 运行全部 Maven 测试 | `.\mvnw.cmd test` | Maven 标准生命周期 | 确定 |
| 运行 admin | `.\mvnw.cmd -pl admin spring-boot:run` | `admin/pom.xml` + Spring Boot app | 确定 |
| 运行 project | `.\mvnw.cmd -pl project spring-boot:run` | `project/pom.xml` + Spring Boot app | 确定 |
| 运行 gateway | `.\mvnw.cmd -pl gateway spring-boot:run` | `gateway/pom.xml` + Spring Boot app | 确定 |
| 运行 aggregation | `.\mvnw.cmd -pl aggregation spring-boot:run` | `aggregation/pom.xml` + Spring Boot app | 确定 |
| 构建单模块及依赖 | `.\mvnw.cmd -pl project -am package` | Maven reactor | 确定 |
| 触发 Spotless apply | `.\mvnw.cmd compile` | root `pom.xml` compile phase | 确定，有副作用 |

## 前端命令

| 类型 | 命令 | 来源 | 确定性 |
| --- | --- | --- | --- |
| 安装依赖 | `npm install` | `console-vue/package-lock.json` + `package.json` | 确定 |
| 启动开发服务 | `npm run dev` | `console-vue/package.json` | 确定 |
| 构建 | `npm run build` | `console-vue/package.json` | 确定 |
| 预览构建产物 | `npm run preview` | `console-vue/package.json` | 确定 |
| Lint 并自动修复 | `npm run lint` | `console-vue/package.json` | 确定，有改写副作用 |
| 格式化前端源码 | `npm run format` | `console-vue/package.json` | 确定，有改写副作用 |

## 本地依赖

| 依赖 | 地址/配置 | 来源 | 说明 |
| --- | --- | --- | --- |
| MySQL | `127.0.0.1:3306/link` | ShardingSphere dev YAML | 启动后端前需可用 |
| Redis | `127.0.0.1:6379` | application YAML | 登录、缓存、锁、消息流依赖 |
| Nacos | `127.0.0.1:8848` | application YAML | 拆分服务 + gateway 路由依赖 |
| 数据库脚本 | `resources/database/link.sql`、`link-data.sql` | 文件结构 | 初始化 schema/data |

## 启动组合

- 拆分服务模式：启动 MySQL/Redis/Nacos，再启动 `admin`、`project`、`gateway`。
- 聚合模式：启动 MySQL/Redis/Nacos，再启动 `aggregation`；如仍使用网关，可将 `gateway` profile 切到 `aggregation` 或使用 `application-aggregation.yaml`。
- 注意：`gateway` 和 `aggregation` 默认都使用端口 `8003`，不能同时用默认端口启动。

## 测试现状

- 确定：只发现 `admin/src/test/java/com/nageoffer/shortlink/admin/test/UserTableShardingTest.java`，它是带 `main` 的 SQL 生成工具，不是标准 JUnit 测试。
- 确定：`console-vue/package.json` 未提供 `test` 脚本。
- 建议：业务修改后至少运行相关 Maven 模块编译/测试；前端修改后至少运行 `npm run build`。
