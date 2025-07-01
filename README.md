# Bunny Code Generator 🚀

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)[![Java Version](https://img.shields.io/badge/JDK-17-green.svg)]()[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-6DB33F.svg)]()

Bunny Code Generator 是一个高效的数据库表结构到代码的生成工具，帮助开发者快速生成高质量的服务端代码，显著提升开发效率。

## ✨ 核心特性

- **多数据源支持** - 支持通过数据库连接或直接解析SQL语句生成代码
- **模板引擎自由** - 基于Velocity模板引擎，支持完全自定义代码模板
- **灵活输出方式** - 支持即时代码预览和ZIP打包下载两种输出模式
- **智能命名转换** - 自动将表名转换为大/小驼峰命名的类名
- **动态模板选择** - 前端可交互式选择需要生成的模板文件组合
- **注释保留** - 自动将表字段注释带入生成的代码中

## 🚀 快速入门

### 前置要求

- JDK 1.8 或更高版本
- MySQL 5.7+ (或其他支持的数据库)
- Spring Boot 2.7+

### 安装步骤

1. 克隆仓库：
   ```bash
   git clone https://github.com/yourusername/bunny-code-generator.git
   ```

2. 配置数据库连接 (`application.yml`)：
   ```yaml
   bunny:
     master:
       database: your_database
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/your_database
       username: your_username
       password: your_password
       driver-class-name: com.mysql.cj.jdbc.Driver
   ```

3. 启动应用：
   ```bash
   mvn spring-boot:run
   ```

4. 访问 `http://localhost:8080` 开始使用

## 🛠️ 模板变量参考

模板中可使用以下预定义变量：

| 变量名               | 描述               | 示例值             |
| -------------------- | ------------------ | ------------------ |
| `tableName`          | 原始表名           | `user_info`        |
| `classLowercaseName` | 小驼峰类名         | `userInfo`         |
| `classUppercaseName` | 大驼峰类名         | `UserInfo`         |
| `columnInfoList`     | 列信息列表         | `List<ColumnInfo>` |
| `baseColumnList`     | SQL列名字符串      | `id,name,age`      |
| `package`            | 包名               | `com.example.demo` |
| `author`             | 作者名             | `YourName`         |
| `date`               | 当前日期           | `2023-07-20`       |
| `comment`            | 表注释             | `用户信息表`       |
| `requestMapping`     | Controller请求前缀 | `/api/user`        |

## 🤝 参与贡献

我们欢迎各种形式的贡献！请阅读 [贡献指南](CONTRIBUTING.md) 了解如何参与项目开发。

1. Fork 项目仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 发起 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件。

## ☕ 支持项目

如果这个项目对您有帮助，可以考虑支持我们：

![WeChat & Alipay](images/wx_alipay.png)

---

**Happy Coding!** 🎉