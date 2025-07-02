# 🚀 Bunny Code Generator 代码生成器系统文档

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)[![Java Version](https://img.shields.io/badge/JDK-17-green.svg)]()[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-6DB33F.svg)]()

## 1. 系统架构 🏗️

### 1.1 架构图

```mermaid
graph TD
    A[🖥️ 前端界面] -->|HTTP请求| B[🌐 WebController]
    A -->|API调用| C[⚙️ GeneratorController]
    A -->|SQL解析| D[🔍 SqlParserController]
    A -->|元数据查询| E[🗃️ TableController]
    A -->|模板管理| F[📂 VmsController]
    
    B -->|页面跳转| G[📝 Thymeleaf模板]
    C -->|生成请求| H[🏭 GeneratorService]
    D -->|SQL解析| I[✂️ SqlMetadataProvider]
    E -->|数据库查询| J[🔗 DatabaseMetadataProvider]
    F -->|模板扫描| K[🗃️ VmsService]
    
    H -->|模板渲染| L[🌀 Velocity引擎]
    H -->|打包下载| M[📦 ZipFileUtil]
    I & J -->|数据源| N[💾 MySQL数据库]
    J -->|连接池| O[🛟 HikariCP]
```

### 1.2 核心分层 🔍

| 层级         | 组件            | 技术实现             |
| ------------ | --------------- | -------------------- |
| **接入层** 🚪 | Controllers     | Spring Web, Swagger  |
| **业务层** ⚙️ | Services        | 并行流, 设计模式     |
| **核心层** 🧠 | 模板生成/元数据 | Velocity, JSqlParser |
| **数据层** 💾 | 数据源/连接池   | HikariCP, JDBC       |

---

## 2. 核心控制器详解 🎯

### 2.1 GeneratorController ⚡

**核心业务流：**

```mermaid
flowchart TB
    A[接收VmsArgumentDto] --> B{判断生成方式}
    B -->|SQL生成| C[调用generateCodeBySql]
    B -->|数据库生成| D[调用generateCodeByDatabase]
    C/D --> E[模板渲染]
    E --> F{是否打包下载}
    F -->|是| G[生成ZIP响应]
    F -->|否| H[返回代码预览]
```

**关键方法说明：**

1. `generator()` 方法：

   - 🔄 根据`sql`参数判断生成方式

   - 📊 使用`VmsArgumentDto`接收模板选择、包名等参数

   - 🌟 核心逻辑：

     ```java
     Strings.isEmpty(sql) 
         ? generatorService.generateCodeByDatabase(dto)
         : generatorService.generateCodeBySql(dto);
     ```

2. `downloadByZip()` 方法：

   - 🗜️ 使用`ZipFileUtil`进行内存压缩

   - ⚡ 响应头设置：

     ```java
     headers.add("Content-Disposition", "attachment; filename=" + zipFilename);
     ```

### 2.2 TableController 🗃️

**元数据查询体系：**

```mermaid
classDiagram
    TableController --> TableService : 依赖
    TableService --> DatabaseMetadataProvider : 调用
    DatabaseMetadataProvider --> HikariDataSource : 使用
    
    class TableController{
        +databaseInfoMetaData() 数据库基础信息
        +databaseTableList() 表清单
        +tableMetaData() 单表结构
        +tableColumnInfo() 列详情
    }
```

**典型调用链：**

1. 获取数据库信息：

   ```java
   DatabaseMetaData metaData = connection.getMetaData();
   return DatabaseInfoMetaData.builder()
       .databaseProductName(metaData.getDatabaseProductName())
       .driverVersion(metaData.getDriverVersion())
       .build();
   ```

2. 查询表结构：

   ```java
   ResultSet tables = metaData.getTables(dbName, null, "%", new String[]{"TABLE"});
   while(tables.next()){
       // 构建TableMetaData对象
   }
   ```

## 3. 关键技术实现 🔧

### 3.1 模板生成流程 🌀

**抽象模板方法模式：**

```java
public abstract class AbstractTemplateGenerator {
    // 模板方法
    public final StringWriter generateCode() {
        prepareContext();  // 准备数据
        return mergeTemplate(); // 渲染模板
    }
    protected abstract void addContext(); // 子类实现
}
```

**Velocity上下文示例：**

```java
context.put("classLowercaseName", "userInfo"); 
context.put("columns", columnList);
context.put("date", "2023-01-01");
```

### 3.2 类型转换策略 🔄

**MySQL类型映射：**

| 数据库类型 | Java类型        | 转换方法                                   |
| ---------- | --------------- | ------------------------------------------ |
| `varchar`  | `String`        | `MysqlTypeConvertUtil.convertToJavaType()` |
| `datetime` | `LocalDateTime` | 同上                                       |
| `tinyint`  | `Integer`       | 同上                                       |

**命名转换示例：**

```java
// 下划线转驼峰
convertToCamelCase("user_name", true);  // → "UserName"
convertToCamelCase("user_name", false); // → "userName"
```

## 4. 核心业务逻辑说明

### 4.1 代码生成流程

#### 4.1.1 简洁流畅

```mermaid
sequenceDiagram
    participant F as 前端
    participant C as Controller
    participant S as Service
    participant T as Template
    
    F->>C: 提交生成请求(VmsArgumentDto)
    C->>S: 调用generateCodeBy[Sql/Database]
    S->>T: 准备Velocity上下文
    T->>S: 返回渲染结果
    S->>C: 返回Map<String, List<GeneratorVo>>
    C->>F: 返回结果或ZIP包
```

### 4.1.2 详细流程

```mermaid
sequenceDiagram
    participant 前端
    participant GeneratorController
    participant GeneratorService
    participant MetadataProvider
    participant TemplateEngine
    
    前端->>GeneratorController: 提交生成请求
    GeneratorController->>GeneratorService: 调用生成方法
    GeneratorService->>MetadataProvider: 获取表/列元数据
    MetadataProvider-->>GeneratorService: 返回元数据
    GeneratorService->>TemplateEngine: 填充模板
    TemplateEngine-->>GeneratorService: 生成代码
    GeneratorService-->>GeneratorController: 返回生成结果
    GeneratorController-->>前端: 返回代码/ZIP包
```

### 4.2 关键类协作

```mermaid
classDiagram
    class GeneratorController {
        +generateCode()
        +downloadByZip()
    }
    
    class GeneratorService {
        +generateCodeByDatabase()
        +generateCodeBySql()
    }
    
    class AbstractTemplateGenerator {
        +generateCode()
        #addContext()
        #templateMerge()
    }
    
    class VmsTBaseTemplateGenerator {
        +addContext()
        +templateMerge()
    }
    
    GeneratorController --> GeneratorService
    GeneratorService --> AbstractTemplateGenerator
    AbstractTemplateGenerator <|-- VmsTBaseTemplateGenerator
```

## 5. 扩展指南 🛠️

### 5.1 添加新数据库支持

1. 实现`DatabaseDialect`接口 ✏️

2. 配置新的`IMetadataProvider` ⚙️

3. 示例代码结构：

   ```java
   @Component
   public class OracleDialect implements DatabaseDialect {
       // 实现提取注释等方法
   }
   ```

### 5.2 依赖管理 📦

**关键依赖说明：**

```xml
<!-- SQL解析 -->
<dependency>
    <groupId>com.github.jsqlparser</groupId>
    <artifactId>jsqlparser</artifactId>
    <version>4.9</version>
</dependency>

<!-- 模板引擎 -->
<dependency>
    <groupId>org.apache.velocity</groupId>
    <artifactId>velocity-engine-core</artifactId>
    <version>2.2</version>
</dependency>
```

## 6. 注意事项 ⚠️

### 6.1 性能优化点 🚀

- `parallelStream()` 用于批量生成

- `HikariCP` 配置：

  ```yaml
  hikari:
    maximum-pool-size: 20
    connection-timeout: 30000
  ```

### 6.2 版本兼容性 🔗

| 组件        | 已验证版本 |
| ----------- | ---------- |
| MySQL       | 8.0+       |
| JDK         | 17+        |
| Spring Boot | 3.4.3      |

### 6.3 文件名重复问题解决方案⚠️

```mermaid
graph TD
    A[用户选择模板] --> B{是否包含$className}
    B -->|是| C[替换为表名的小驼峰格式]
    B -->|否| D[保持原文件名]
    C --> E[生成唯一文件路径]
    D --> E
```

**核心逻辑实现**（位于`VmsGeneratorPathHelper.java`）：

```java
public static String processVmPath(VmsArgumentDto dto, String path, String tableName) {
    String className = removeTablePrefixes(dto, tableName);
    String lowerCamelCase = MysqlTypeConvertUtil.convertToCamelCase(tableName, false);
    // 关键替换逻辑👇
    String[] pathParts = path.replace("$className", lowerCamelCase).split("/");
    ...
}
```

**必须遵守的规则**：

> [!Note]
>
> 如果不想使用`$className`可自己修改源码，进行更改。

1. 前端文件必须使用`$className`作为动态目录名（如：`web/$className/api.ts`）
2. 相同基础名称的文件必须放在不同目录下
3. Java/XML文件会自动添加类型后缀（如`UserController.java`）

### 6.4 文件命名冲突场景示例

| 错误案例                | 正确方案                   | 原因                       |
| ----------------------- | -------------------------- | -------------------------- |
| `web/index.vue`         | `web/$className/index.vue` | 多表生成时会冲突           |
| `mapper/UserMapper.xml` | 自动处理                   | 系统会自动添加表名前缀     |
| `service/Service.java`  | 自动处理                   | 会转换为`UserService.java` |

## 7. 代码质量评估 🔍

### 7.1 后端代码评估（85/100）

**优势**：

- ✅ 清晰的层次划分（Controller/Service/Provider）
- ✅ 合理使用设计模式（模板方法模式）
- ✅ 完善的异常处理体系
- ✅ 类型转换工具类封装良好

**待改进**：

- ⚠️ 部分SQL解析逻辑可抽取为策略模式
- ⚠️ 元数据提供者接口可进一步抽象
- ⚠️ ZIP打包逻辑与业务耦合稍紧

**坏味道检测**：

```mermaid
pie
    title 后端代码坏味道分布
    "重复代码" : 15
    "过长方法" : 10
    "过度耦合" : 5
    "其他" : 70
```

### 7.2 前端代码评估（78/100）

**亮点**：

- ✅ 组件化设计合理（表单/表格分离）
- ✅ 响应式状态管理有效
- ✅ 良好的用户交互反馈
- ✅ 类型提示完善

**问题点**：

- ⚠️ 部分表单验证逻辑重复
- ⚠️ 表格分页逻辑可抽取为独立组件

**复杂度分析**：

| 文件                | 方法数 | 平均行数 | 复杂度 |
| ------------------- | ------ | -------- | ------ |
| DatabaseTable.js    | 12     | 18       | 中等   |
| DatabaseForm.js     | 25     | 12       | 较高   |
| AppGeneratorPage.js | 8      | 15       | 低     |

### 7.3 总结评价

**整体评分**：82/100  

**可维护性**：⭐️⭐️⭐️⭐️  

**扩展性**：⭐️⭐️⭐️⭐️  

**代码规范**：⭐️⭐️⭐️⭐️⭐️  

## 8. 支持项目☕ 

如果这个项目对您有帮助，可以考虑支持我们：

![WeChat & Alipay](D:\Project\FullStack\GeneratorCode\images\wx_alipay.png)

**Happy Coding!** 🎉