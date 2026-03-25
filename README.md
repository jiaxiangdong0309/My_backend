# 花生产品溯源系统 - 后端服务

## 项目简介

本项目是一个基于微服务架构的花生产品溯源系统，覆盖从种植、加工、仓储到销售的全链条追溯，确保产品质量安全和信息透明。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18 | 主框架 |
| Java | 18 | 开发语言 |
| MyBatis Plus | 3.5.5 | ORM框架 |
| MySQL | 8.0.33 | 数据库 |
| Redis | 2.7.18 | 缓存 |
| JWT | 0.11.5 | 身份认证 |
| Spring Security | - | 安全框架 |
| Knife4j | 4.4.0 | API文档 |
| Hutool | 5.8.25 | 工具库 |
| EasyExcel | 3.3.3 | Excel处理 |
| iText7 | 7.2.5 | PDF生成 |
| Lombok | - | 代码简化 |

## 项目结构

本项目采用 **Maven 多模块架构**，模块之间职责清晰，便于维护和扩展。

```
backend/
├── pom.xml                          # 父工程（统一管理依赖版本）
├── peanut-common/                   # 公共模块
│   └── src/
│       ├── main/java/com/peanut/common/
│       │   ├── config/              # 通用配置
│       │   ├── entity/              # 基础实体类
│       │   ├── result/              # 统一响应结果
│       │   ├── exception/           # 全局异常处理
│       │   └── utils/               # 工具类
│       └── test/java/
├── peanut-auth/                     # 认证授权模块
│   └── src/main/java/com/peanut/auth/
│       ├── controller/              # 登录、注册、权限接口
│       ├── service/                 # 用户认证逻辑
│       └── security/                # Security配置
├── peanut-product/                  # 产品信息模块
│   └── src/main/java/com/peanut/product/
│       ├── controller/              # 产品CRUD接口
│       ├── service/                 # 产品业务逻辑
│       └── mapper/                  # 数据访问层
├── peanut-trace/                    # 溯源追踪模块
│   └── src/main/java/com/peanut/trace/
│       ├── controller/              # 溯源查询接口
│       ├── service/                 # 溯源链构建逻辑
│       └── entity/                  # 追溯记录实体
├── peanut-stats/                    # 统计分析模块
│   └── src/main/java/com/peanut/stats/
│       ├── controller/              # 统计报表接口
│       └── service/                 # 数据分析逻辑
└── peanut-gateway/                  # 网关模块（服务入口）
    └── src/main/java/com/peanut/gateway/
        ├── config/                  # 网关配置
        └── filter/                  # 过滤器（认证、限流等）
```

## 模块职责说明

| 模块 | 职责 | 端口建议 |
|------|------|----------|
| `peanut-common` | 存放公共实体、工具类、配置，被其他模块依赖 | 无 |
| `peanut-auth` | 用户登录、注册、JWT令牌生成与验证、权限管理 | 8081 |
| `peanut-product` | 产品基础信息管理（品种、批次、产地等） | 8082 |
| `peanut-trace` | 溯源链管理（种植→加工→仓储→运输→销售） | 8083 |
| `peanut-stats` | 数据统计、报表生成、大屏展示数据接口 | 8084 |
| `peanut-gateway` | 统一入口、路由转发、鉴权、限流、跨域处理 | 8080 |

## 快速开始

本地开发启动建议优先看：`docs/后端启动说明.md`

### 1. 环境要求

- JDK 18+
- Maven 3.6+
- MySQL 8.0+
- Redis 5.0+

### 2. 数据库初始化

```sql
-- 创建数据库
create database peanut_trace default charset utf8mb4 collate utf8mb4_unicode_ci;

-- 执行各模块的 SQL 脚本（位于各模块 src/main/resources/sql/ 目录）
```

### 3. 配置修改

各模块的 `application.yml` 需要修改以下配置：

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/peanut_trace?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password

  # Redis配置
  redis:
    host: localhost
    port: 6379
    password:

# JWT密钥
jwt:
  secret: your-secret-key-here
  expiration: 86400000  # 24小时
```

### 4. 编译打包

```bash
# 在项目根目录执行
mvn clean install

# 或者跳过测试
mvn clean install -DskipTests
```

### 5. 启动服务

**方式一：IDEA 启动**

1. 打开每个模块的 `XxxApplication.java`
2. 右键 → Run

**方式二：命令行启动**

```bash
# 先启动基础服务
java -jar peanut-common/target/peanut-common-1.0.0.jar

# 再启动业务服务
cd peanut-auth && mvn spring-boot:run
cd peanut-product && mvn spring-boot:run
cd peanut-trace && mvn spring-boot:run
cd peanut-stats && mvn spring-boot:run

# 最后启动网关
cd peanut-gateway && mvn spring-boot:run
```

## 开发规范

### 包命名规范
```
com.peanut.{模块名}.{层}
# 例如：
com.peanut.product.controller
com.peanut.product.service
com.peanut.product.mapper
com.peanut.product.entity
```

### API 接口规范

- 统一前缀：`/api/{模块名}`
- 版本控制：`/api/v1/{模块名}`
- 响应格式统一：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1711276800000
}
```

### 接口文档

项目集成 Knife4j，启动后访问：

```
http://localhost:8080/doc.html
```

## Docker 部署

```bash
# 构建镜像
docker build -t peanut-trace-backend .

# 运行容器
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/peanut_trace \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  peanut-trace-backend
```

## 目录结构说明

为什么每个模块都有 `src`？

这是 **Maven 多模块项目** 的标准结构：

1. **父工程**（根 `pom.xml`）：只负责版本管理，不打包成 jar
2. **子模块**：每个都是独立的 Spring Boot 应用，都有自己的 `src/main/java` 和 `src/test/java`
3. **独立部署**：每个模块可以单独打包、单独部署
4. **依赖关系**：`peanut-common` 是基础，其他模块都依赖它；`peanut-gateway` 依赖所有业务模块

相比单模块项目，多模块的优势：
- 职责分离，代码更清晰
- 团队并行开发，互不干扰
- 独立部署，按需扩展
- 可复用的公共模块

## 联系方式

如有问题，请联系项目负责人或提交 Issue。

---

**版本**: 1.0.0
**最后更新**: 2024-03
