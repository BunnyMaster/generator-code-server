## 文件说明

| 目录                                         | 说明             |
| -------------------------------------------- | ---------------- |
| resources/templates/web-pages                | 用于网页显示页面 |
| auth/module/generator/core/mode              | 获取方式         |
| auth/module/generator/core/provider/metadata | 支持模式         |

### 如果有SQL方言

在目录`auth/module/generator/core/provider/dialect`中可以写对应实现之后使用。

并不是强制的，只是我这样设计目录结构。。

## 使用说明

### 添加获取模式

目前获取模式有几种，分别都在对应的`ModeStrategy`实现下。所有的获取模式都是根据`ModeStrategy`接口进行实现的，最后使用策略进行匹配。

如果要自定义，需要实现`ModeStrategy`接口并且放入到IOC容器中。

#### 原理说明

策略查找机制基于Spring IOC容器，

```java
private final Map<String, ModeStrategy<?>> strategies = new ConcurrentHashMap<>();

public ModeStrategyContext(List<ModeStrategy<?>> strategyList, ModeStrategyContextValidationService validationService) {
    // 先校验所有提供器
    strategyList.forEach(provider -> {
        ElementOptionVO providerType = provider.getModeName();
        validationService.validateSupportProviderValue(providerType);
    });

    // 将策略列表转换为以策略类型为key 的map集合
    Map<String, ModeStrategy<?>> strategyMap = strategyList.stream()
            .collect(Collectors.toMap(modeStrategy -> modeStrategy.getModeName().getValue(), strategy -> strategy));
    strategies.putAll(strategyMap);
}
```

判断当前是否支持

```java
/**
 * 根据策略类型获取对应的生成策略
 *
 * @param mode 策略类型
 * @return 对应的生成策略对象
 */
@SuppressWarnings("java:S1452")
public ModeStrategy<?> getStrategy(String mode) {
    ModeStrategy<?> strategy = strategies.get(mode);

    if (strategy == null) {
        throw new IllegalArgumentException("不支持的生成策略: " + mode);
    }

    return strategy;
}
```

### 添加生成模式

> [!NOTE]
>
> 目前是有两种方式进行生成的：数据库和SQL语句，两种都是MySQL

如果要自定义，需要实现`MetadataProvider`并且放入到IOC容器中。

#### 实现方式


1. 需要实现`MetadataProvider`接口，如果有很多相同的内容可以做成抽象
2. 前端渲染根据`ElementOptionVO`进行的，label就是选项内容，value是传给后端的值，这个是必须的;会在`ValidationService`进行校验
3. 可以参考实现：`MySQLDatabaseMetadataProvider`、`SQLParserMetadataProvider`
4. 无论是抽象还有直接实现，一定要有对`MetadataProvider`，因为在`MetadataProviderContext`中会使用Spring IOC去查找对应的实现。

#### 原理说明

在下面的实现中就说明了是怎么知道当前有哪些生成模式的

```java
private final Map<String, MetadataProvider> metadataProviderMap = new ConcurrentHashMap<>();

public MetadataProviderContext(@Valid List<MetadataProvider> metadataProviderList, ValidationService validationService) {
    // 先校验所有提供器
    metadataProviderList.forEach(provider -> {
        ElementOptionVO providerType = provider.getProviderType();
        validationService.validateSupportProviderValue(providerType);
    });

    // 再转换 Map
    Map<String, MetadataProvider> providerMap = metadataProviderList.stream()
            .collect(Collectors.toMap(metadataProvider -> metadataProvider.getProviderType().getValue(), provider -> provider));

    metadataProviderMap.putAll(providerMap);
}
```

进一步的，会用策略模式去匹配当前有哪些生成内容

```java
/**
 * 根据数据库类型获取对应的元数据提供器
 *
 * @param providerType 数据库类型
 * @return 对应的元数据提供器对象
 */
public MetadataProvider getProvider(String providerType) {
    MetadataProvider provider = metadataProviderMap.get(providerType);
    if (provider == null) {
        throw new IllegalArgumentException("不支持的数据库类型: " + providerType);
    }
    return provider;
}
```

### 添加页面

1. 在`resources/templates/web-pages`页面中进行添加即可。
2. 在`IndexController`添加访问路径

## 设计想法

我想的是职责分离，所以每个“生成模式”、”获取方式“都是单独写成一个类中的

### 1. 策略模式 (Strategy Pattern)

在多个地方运用了策略模式：

- 不同的**生成模式**(下载、打印、写入等)通过`ModeStrategy`接口统一处理
- 不同的**元数据来源**(数据库连接、SQL解析)通过`MetadataProvider`接口统一处理

这种设计使得新增一种生成模式或者元数据源变得非常容易，只需要实现对应接口即可，符合开闭原则。

### 2. 工厂模式 + 上下文管理

通过`ModeStrategyContext`和`MetadataProviderContext`来管理和获取具体的策略实例。

### 3. 模板方法模式

`AbstractDatabaseMetadataProvider`定义了获取元数据的基本流程框架——模板方法模式

### 4. 数据驱动的设计

整个系统的运作基于配置数据`GenerationConfigDTO`，通过对配置的解析和处理来决定如何生成代码

![](./images/wx_alipay.png)
