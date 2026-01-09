package com.auth.module.generator.core.provider.metadata.impl;

import com.alibaba.fastjson2.JSON;
import com.auth.module.generator.core.provider.builder.TemplateDataModelBuilder;
import com.auth.module.generator.core.provider.dialect.GeneratorCategoryDialect;
import com.auth.module.generator.core.provider.metadata.AbstractDatabaseMetadataProvider;
import com.auth.module.generator.core.provider.validate.ValidationService;
import com.auth.module.generator.exception.GeneratorCodeException;
import com.auth.module.generator.exception.MetadataNotFoundException;
import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.entity.generator.extra.DatabaseConfigEntity;
import com.auth.module.generator.model.entity.meta.ColumnMetaData;
import com.auth.module.generator.model.entity.meta.MetaData;
import com.auth.module.generator.model.value.TemplateDataModel;
import com.auth.module.generator.model.vo.ElementOptionVO;
import com.auth.module.generator.utils.NameConvertUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class MySQLDatabaseMetadataProvider extends AbstractDatabaseMetadataProvider {

    /**
     * 备注信息字段
     */
    public static final String REMARKS = "REMARKS";

    /**
     * 表目录字段
     */
    public static final String TABLE_CAT = "TABLE_CAT";

    /**
     * 表类型字段
     */
    public static final String TABLE_TYPE = "TABLE_TYPE";

    /**
     * 列名字段
     */
    public static final String COLUMN_NAME = "COLUMN_NAME";

    /**
     * 类型名称字段
     */
    public static final String TYPE_NAME = "TYPE_NAME";

    /**
     * 表标识字段
     */
    public static final String TABLE = "TABLE";

    private final GeneratorCategoryDialect mySqlDialect;

    private final HikariDataSource dataSource;

    private final TemplateDataModelBuilder<DatabaseConfigEntity> templateDataModelBuilder;

    public MySQLDatabaseMetadataProvider(FreeMarkerConfigurer freeMarkerConfigurer,
                                         @Qualifier("mySQLDialect") GeneratorCategoryDialect mySqlDialect,
                                         HikariDataSource dataSource,
                                         TemplateDataModelBuilder<DatabaseConfigEntity> templateDataModelBuilder,
                                         ValidationService validationService) {
        super(freeMarkerConfigurer, validationService);

        this.mySqlDialect = mySqlDialect;
        this.dataSource = dataSource;
        this.templateDataModelBuilder = templateDataModelBuilder;
    }

    /**
     * 获取元数据提供器的类型
     *
     * @return supportProvider 元数据提供器的类型
     */
    @Override

    public ElementOptionVO getProviderType() {
        return ElementOptionVO.builder().value("MySQL").label("MySQL 数据库").build();
    }

    /**
     * 根据表名标识符获取单个表的元数据信息
     *
     * @param tableName 要查询的表名（大小写敏感度取决于数据库实现）
     * @return TableMetaData 包含表元数据的对象，包括：
     */
    @Override
    public MetaData getMetadata(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, new String[]{TABLE});

            if (tables.next()) {
                String comment = tables.getString(REMARKS);
                return MetaData.builder()
                        .name(tableName)
                        // 防止没有注释导致报错
                        .comment(comment)
                        .category(tables.getString(TABLE_CAT))
                        .type(tables.getString(TABLE_TYPE))
                        .build();
            }
            throw new MetadataNotFoundException("Table not found: " + tableName);
        } catch (SQLException e) {
            throw new GeneratorCodeException("Failed to get metadata for table: " + tableName, e);
        }
    }

    /**
     * 获取指定表的所有列信息列表
     *
     * @param tableName 要查询的表名（大小写敏感度取决于数据库实现）
     * @return 包含所有列元数据的列表，每个列的信息封装在ColumnMetaData对象中
     */
    @Override
    public List<ColumnMetaData> getColumnInfoList(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            Set<String> primaryKeys = getPrimaryKeys(tableName);
            DatabaseMetaData metaData = connection.getMetaData();

            return getColumnMetaData(metaData, tableName, primaryKeys);
        } catch (SQLException e) {
            throw new GeneratorCodeException("Failed to get column info for table: " + tableName, e);
        }
    }

    /**
     * 获取表的所有主键列名 获取表的主键列名集合
     *
     * @param tableName 表名
     * @return 主键列名的集合
     */
    @NotNull
    private Set<String> getPrimaryKeys(String tableName) {
        Set<String> primaryKeys = new HashSet<>();
        try (Connection connection = dataSource.getConnection()) {
            ResultSet pkResultSet = connection.getMetaData().getPrimaryKeys(null, null, tableName);
            while (pkResultSet.next()) {
                primaryKeys.add(pkResultSet.getString(COLUMN_NAME).toLowerCase());
            }
        } catch (SQLException e) {
            throw new GeneratorCodeException("Get primary key error: " + e.getMessage());
        }
        return primaryKeys;
    }

    /**
     * 获取列元数据列表
     */
    private List<ColumnMetaData> getColumnMetaData(DatabaseMetaData metaData, String tableName, Set<String> primaryKeys) throws SQLException {
        Map<String, ColumnMetaData> columnMap = new LinkedHashMap<>();
        try (ResultSet columnsRs = metaData.getColumns(null, null, tableName, null)) {
            while (columnsRs.next()) {
                ColumnMetaData column = buildColumnMetaData(columnsRs, primaryKeys);
                columnMap.putIfAbsent(column.getColumnName(), column);
            }
        }
        return columnMap.values().stream().distinct().toList();
    }

    /**
     * 构建列元数据对象
     */
    @NotNull
    private ColumnMetaData buildColumnMetaData(@NotNull ResultSet columnsRs, @NotNull Set<String> primaryKeys) throws SQLException {
        String columnName = columnsRs.getString(COLUMN_NAME);
        String typeName = columnsRs.getString(TYPE_NAME);

        ColumnMetaData column = new ColumnMetaData();
        column.setColumnName(columnName);
        column.setLowercaseName(NameConvertUtil.lowerCamelCase(columnName));
        column.setUppercaseName(NameConvertUtil.upperCamelCase(columnName));
        column.setJdbcType(typeName);
        column.setJavaType(mySqlDialect.convertToJavaType(typeName));
        column.setJavascriptType(mySqlDialect.convertToJavaScriptType(column.getJavaType()));
        column.setComment(columnsRs.getString(REMARKS));
        column.setIsPrimaryKey(primaryKeys.contains(columnName));

        return column;
    }

    /**
     * 根据生成配置获取模板数据模型列表
     *
     * @param dto 生成配置数据传输对象，包含数据库配置信息
     * @return 模板数据模型列表
     */
    @Override
    public List<TemplateDataModel> getTemplateDataModels(GenerationConfigDTO<Object> dto) {
        // 解析额外配置信息，转换为数据库配置实体
        DatabaseConfigEntity extra = JSON.parseObject(JSON.toJSONString(dto.getExtra()), DatabaseConfigEntity.class);
        GenerationConfigDTO<DatabaseConfigEntity> configDTO = new GenerationConfigDTO<>();
        BeanUtils.copyProperties(dto, configDTO);

        // 遍历表名列表，为每个表生成对应的模板数据模型
        return extra.getTableNames().stream()
                .map(tableName -> templateDataModelBuilder.templateDataModelList(configDTO, getMetadata(tableName), getColumnInfoList(tableName)))
                .flatMap(Collection::stream)
                .toList();
    }
}