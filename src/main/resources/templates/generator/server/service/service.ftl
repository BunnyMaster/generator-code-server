<#-- @ftlvariable name="date" type="java.lang.String" -->
<#-- @ftlvariable name="author" type="java.lang.String" -->
<#-- @ftlvariable name="tableName" type="java.lang.String" -->
<#-- @ftlvariable name="packagePath" type="java.lang.String" -->
<#-- @ftlvariable name="commentTime" type="java.lang.String" -->
<#-- @ftlvariable name="requestPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="tableNameByUppercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="tableNameByLowercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByUppercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByLowercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="metaData" type="com.auth.module.generator.model.entity.meta.MetaData" -->
<#-- @ftlvariable name="columnInfoList" type="java.util.List<com.auth.module.generator.model.entity.ColumnMetaData>" -->
<#-- @ftlvariable name="field" type="com.auth.module.generator.model.entity.meta.ColumnMetaData" -->
<#-- @ftlvariable name="tableMetaDataList" type="com.auth.module.generator.model.entity.meta.MetaData" -->

<#-- 引入包含javaComment宏的模板 -->
<#include "../common/common-comment.ftl">
<#include "../common/common-import.ftl">

<@importPackage/>

<@javaComment "${metaData.comment}", "${commentTime}", "${author}", "服务接口类"/>
public interface ${formatterTableNameByUppercaseName}Service extends IService<${formatterTableNameByUppercaseName}Entity> {

    /**
     * 分页查询${metaData.comment}
     *
     * @param pageParams ${metaData.comment}分页查询page对象
     * @param query ${metaData.comment}分页查询对象
     * @return 查询分页${metaData.comment}返回对象
     */
    PageResult<${formatterTableNameByUppercaseName}PageVO> get${formatterTableNameByUppercaseName}Page(IPage<${formatterTableNameByUppercaseName}Entity> pageParams, ${formatterTableNameByUppercaseName}Query query);

    /**
     * 根据id查询${metaData.comment}详情
     *
     * @param id 主键
     * @return ${metaData.comment}详情
     */
    ${formatterTableNameByUppercaseName}VO getById(Long id);

    /**
     * 添加${metaData.comment}
     *
     * @param dto 添加表单
     */
    void create(${formatterTableNameByUppercaseName}DTO dto);

    /**
     * 更新${metaData.comment}
     *
     * @param dto 更新表单
     */
    void update(${formatterTableNameByUppercaseName}DTO dto);

    /**
     * 删除|批量删除${metaData.comment}类型
     *
     * @param ids 删除id列表
     */
    void deleteList(List<Long> ids);

}
