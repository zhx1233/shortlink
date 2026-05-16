# ShortLink 系统 JMeter 压测使用手册

## 一、下载与安装 JMeter

### 下载地址

Apache JMeter 官方下载页：**https://jmeter.apache.org/download_jmeter.cgi**

选择 **Binaries** 栏目下的 `apache-jmeter-5.6.3.zip`（或最新稳定版）。

### 系统要求

- **Java 8+**（推荐 JDK 11 或 17）
- 验证 Java 安装：`java -version`

### 安装步骤

**macOS：**
```bash
# 1. 解压
unzip apache-jmeter-5.6.3.zip -d ~/tools/

# 2. 启动（GUI 模式——编辑测试计划）
~/tools/apache-jmeter-5.6.3/bin/jmeter

# 3. 命令行模式（无 GUI 压测，推荐）
~/tools/apache-jmeter-5.6.3/bin/jmeter -n -t test.jmx -l result.jtl -e -o report/
```

**Windows：**
```powershell
# 解压到任意目录后双击
C:\tools\apache-jmeter-5.6.3\bin\jmeter.bat

# 或命令行
C:\tools\apache-jmeter-5.6.3\bin\jmeter -n -t test.jmx -l result.jtl -e -o report\
```

### 中文界面（可选）

修改 `bin/jmeter.properties`：
```properties
language=zh_CN
```

---

## 二、导入测试计划

1. 启动 JMeter GUI：`jmeter`
2. 菜单栏点击 **文件 → 打开**（或 `Ctrl+O` / `Cmd+O`）
3. 选择 `shortlink-api-test.jmx`
4. 测试计划包含三部分：
   - **setUp Thread Group** — 自动登录获取 Token
   - **业务压测线程组** — API 业务接口压测
   - **短链接重定向压测** — 302 跳转专项压测

---

## 三、修改配置变量

打开测试计划后，点击根节点 **ShortLink API 压测计划**，在下方的 **用户自定义变量** 中修改：

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `BASE_URL` | `127.0.0.1` | 服务地址 |
| `GATEWAY_PORT` | `8003` | 网关端口 |
| `PROJECT_PORT` | `8001` | project 服务端口（重定向直连） |
| `TEST_USERNAME` | `testuser` | 测试用户名 |
| `TEST_PASSWORD` | `password123` | 测试用户密码/Token |

> 注册新用户可调用 `POST /api/short-link/admin/v1/user` 接口（白名单路径，无需认证）。

---

## 四、压测场景说明

### 场景 A：业务 API 压测（通过网关 8003，需认证）

| 顺序 | 接口 | 说明 |
|------|------|------|
| setup | 用户登录 | 获取 Token，存为全局属性 |
| 01 | GET `/group` | 查询分组列表，提取 gid |
| 02 | POST `/create` | 创建短链接（带随机参数防缓存） |
| 03 | GET `/page` | 分页查询短链接 |
| 04 | GET `/title` | 获取页面标题 |
| 05 | GET `/user/{username}` | 查询用户信息 |
| 06 | GET `/stats` | 短链接统计数据 |
| 07 | GET `/recycle-bin/page` | 回收站分页查询 |

默认配置：**50 线程，10 秒预热，循环 10 次** = 500 次请求/接口。

### 场景 B：短链接重定向压测（直连 project 8001，无需认证）

默认配置：**100 线程，20 秒预热，循环 20 次** = 2000 次重定向请求。

---

## 五、调整压测参数

点击对应的 **Thread Group**，修改：

| 参数 | 说明 |
|------|------|
| **线程数（Number of Threads）** | 并发用户数 |
| **Ramp-Up 时间（秒）** | 启动全部线程的时长 |
| **循环次数** | 每位用户执行几轮；勾选"永远"可持续运行 |

### 推荐梯度策略

| 阶段 | 线程数 | 循环 | 目的 |
|------|--------|------|------|
| 基准 | 10 | 5 | 验证功能正常 |
| 轻压 | 50 | 10 | 快速确认吞吐 |
| 中压 | 200 | 20 | 找到拐点 |
| 高压 | 500 | 50 | 压极限 |

---

## 六、执行压测

### GUI 模式（调试用）

点击绿色 **▶ 启动** 按钮。可在 **察看结果树** 中实时看到请求/响应详情，**聚合报告** 中看统计。

### 命令行模式（正式压测推荐）

```bash
# 清除上次结果，执行测试，生成 HTML 报告
rm -rf report/ result.jtl
jmeter -n -t shortlink-api-test.jmx -l result.jtl -e -o report/

# 只生成 jtl 文件（更快）
jmeter -n -t shortlink-api-test.jmx -l result.jtl
```

参数说明：
- `-n`：非 GUI 模式（省资源）
- `-t`：测试计划文件
- `-l`：结果输出文件
- `-e -o`：生成 HTML 仪表盘报告到指定目录

---

## 七、查看报告

### 实时：聚合报告（GUI）

关注指标：
- **Average**：平均响应时间（ms）
- **Min / Max**：最快/最慢响应时间
- **Throughput**：吞吐量（TPS，每秒请求数）
- **Error%**：错误率

### 离线：HTML Dashboard

打开 `report/index.html`，关键图表：

| 图表 | 关注点 |
|------|--------|
| APDEX | 应用性能指数（满意度） |
| Response Times Over Time | 响应时间是否随压力上升 |
| Transactions Per Second | TPS 是否平稳 |
| Response Time Percentiles | 90/95/99 百分位延迟 |

---

## 八、注意事项

1. **Token 有效期**：默认测试计划在 setUp 阶段登录一次，如果测试时间很长导致 Token 过期，可以在线程组内加登录请求并使用正则/JSON 提取器更新 Token 变量。

2. **登录接口的白名单**：本项目 token 验证在网关层面，白名单路径（`/login`、`/has-username`、`POST /user`）不需要 token 头。

3. **创建短链接的随机参数**：测试计划中 `originUrl` 附加了 `?s=${__Random(...)}` 随机参数，确保每次创建不同链接，避免唯一性冲突。

4. **重定向不走网关**：短链接跳转请求需要直连 project 服务的 8001 端口，且 `Follow Redirects` 设为 `false`（只测 302 跳转，不跟随）。

5. **硬件瓶颈**：如果压测机器本身 CPU/内存不足，会先于服务端成为瓶颈。建议监控压测机器资源。

6. **关闭 GUI 模式压测**：正式压测务必使用 `-n` 命令行模式，GUI 模式本身消耗大量资源，会影响结果准确性。

---

## 九、常见问题

**Q: JMeter 启动后乱码？**  
A: 修改 `bin/jmeter.properties` 中 `sampleresult.default.encoding=UTF-8`。

**Q: 连接被拒绝？**  
A: 确保目标服务已启动，检查端口：`lsof -i :8003` 和 `lsof -i :8001`。

**Q: 响应全是 401/403？**  
A: 检查 `TEST_USERNAME` 和 `TEST_PASSWORD` 是否正确，Redis 中是否存有对应的 `short-link:login:{username}` key。

**Q: 如何模拟真实流量？**  
A: 将多个线程组组合使用，设置不同的线程数和循环数（如 80% 查询 + 20% 创建），加入思考时间（Timer）。
