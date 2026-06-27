# 📁 项目名称：个人卡片信息管理平台

## 🚀 项目简介
这是一个基于 Web 的个人信息卡片管理系统，提供用户注册、登录功能，登录后可创建、查看和管理个人卡片信息。前端界面简洁友好，后端采用 Maven 进行依赖管理与 API 构建。

## 🛠 技术栈
### 前端（Frontend）
- **Vue** (26.0%)
- **TypeScript** (13.8%)
- **CSS** (1.7%)
- **HTML** (0.5%)

### 后端（Backend）
- **Java** (58.0%)  
  - Spring Boot
  - Spring Security（用于登录鉴权）
  - JPA / MyBatis（数据持久化）

### 构建与依赖管理
- **Maven**（用于后端依赖管理与打包）
- Node.js + npm（用于前端构建）

### 数据库
- MySQL / PostgreSQL（可选）

## 📦 项目结构
```
project/
├── frontend/           # Vue + TypeScript 前端项目
├── backend/            # Spring Boot 后端项目
├── pom.xml             # Maven 配置文件
├── README.md
└── ...（其他配置文件）
```

## 🚦 快速开始

### 1. 克隆项目
```bash
git clone <项目地址>
cd project
```

### 2. 后端启动
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 3. 前端启动
```bash
cd frontend
npm install
npm run serve
```

### 4. 访问系统
打开浏览器，访问：  
`http://localhost:8080`（后端接口）  
`http://localhost:3000`（前端界面，默认端口可能不同）

## 📌 功能特性
- ✅ 用户注册与登录
- ✅ JWT 鉴权支持
- ✅ 卡片信息的增删改查
- ✅ 响应式界面设计
- ✅ 前后端分离架构

## 🔗 API 说明
后端使用 Maven 管理依赖，主要接口包括：
- `POST /api/auth/register` – 注册
- `POST /api/auth/login` – 登录
- `GET /api/cards` – 获取卡片列表
- `POST /api/cards` – 新增卡片
- `PUT /api/cards/{id}` – 更新卡片
- `DELETE /api/cards/{id}` – 删除卡片

## 📁 数据库配置
在 `backend/src/main/resources/application.yml` 中配置数据库连接：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/card_db
    username: root
    password: yourpassword
```

## 📸 截图预览
（可在此处插入登录、注册、卡片管理界面截图）

## 🧠 注意事项
- 确保已安装 Java 11+、Node.js 14+、Maven 3.6+
- 首次启动前需创建数据库并执行建表脚本（如有）
- 前端接口地址需在 `frontend/.env` 中配置后端地址

## 📄 许可证
本项目仅供学习使用，遵循 MIT 协议。

---

如果对项目有任何疑问或建议，欢迎提交 Issue 或联系维护者！
