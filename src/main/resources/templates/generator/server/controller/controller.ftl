<#-- @ftlvariable name="date" type="java.lang.String" -->
<#-- @ftlvariable name="author" type="java.lang.String" -->
<#-- @ftlvariable name="tableName" type="java.lang.String" -->
<#-- @ftlvariable name="requestPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="packagePath" type="java.lang.String" -->
<#-- @ftlvariable name="commentTime" type="java.lang.String" -->
<#-- @ftlvariable name="requestPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="tableNameByUppercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="tableNameByLowercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByKebabCase" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByUppercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByLowercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="metaData" type="com.auth.module.generator.model.entity.meta.MetaData" -->
<#-- @ftlvariable name="columnInfoList" type="java.util.List<com.auth.module.generator.model.entity.ColumnMetaData>" -->
<#-- @ftlvariable name="tableMetaDataList" type="com.auth.module.generator.model.entity.meta.MetaData" -->


<#-- 引入包含javaComment宏的模板 -->
<#include "../common/common-comment.ftl">
<#include "../common/common-import.ftl">

<@importPackage/>

<@javaComment "${metaData.comment}", "${commentTime}", "${author}", "接口控制器"/>
@Tag(name = "${metaData.comment}", description = "${metaData.comment}接口")
@RestController
@RequestMapping("${requestPrefix}")
@RequiredArgsConstructor
public class ${formatterTableNameByUppercaseName}Controller {

    private final ${formatterTableNameByUppercaseName}Service ${tableNameByLowercaseName}Service;

    @PreAuthorize("@auth.decide('${formatterTableNameByKebabCase}:query')")
    @Operation(summary = "分页查询${metaData.comment}", description = "分页查询"${metaData.comment}")
    @GetMapping()
    public Result<PageResult<${formatterTableNameByUppercaseName}PageVO>> get${formatterTableNameByUppercaseName}
    Page(${formatterTableNameByUppercaseName}Query query) {
        IPage<${formatterTableNameByUppercaseName}Entity> pageParams = new Page<>(query.getPageIndex(), query.getPageSize());
        PageResult<${formatterTableNameByUppercaseName}PageVO> pageResult = ${tableNameByLowercaseName} Service.get${formatterTableNameByUppercaseName}Page(pageParams, query);

        return Result.success(pageResult);
    }

    @PreAuthorize("@auth.decide('${formatterTableNameByKebabCase}:query')")
    @Operation(summary = "根据id查询${metaData.comment}详情", description = "根据id查询${metaData.comment}详情")
    @GetMapping("{id}")
    public Result<${formatterTableNameByUppercaseName}VO> getById(@PathVariable("id") Long id) {
        ${formatterTableNameByUppercaseName}VO ${tableNameByLowercaseName}VO = ${tableNameByLowercaseName} Service.getById(id);

        return Result.success(${tableNameByLowercaseName}VO);
    }

    @PreAuthorize("@auth.decide('${formatterTableNameByKebabCase}:create')")
    @Operation(summary = "添加${metaData.comment}", description = "添加${metaData.comment}")
    @PostMapping()
    public Result<String> create(@Valid @RequestBody ${formatterTableNameByUppercaseName}DTO dto) {
        ${tableNameByLowercaseName}Service.create(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @PreAuthorize("@auth.decide('${formatterTableNameByKebabCase}:update')")
    @Operation(summary = "更新${metaData.comment}", description = "更新${metaData.comment}")
    @PutMapping()
    public Result<String> update(@Valid @RequestBody ${formatterTableNameByUppercaseName}DTO dto) {
        ${tableNameByLowercaseName}Service.update(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @PreAuthorize("@auth.decide('${formatterTableNameByKebabCase}:delete')")
    @Operation(summary = "删除${metaData.comment}", description = "删除${metaData.comment}    ")
    @DeleteMapping()
    public Result<String> deleteList(@RequestBody List<Long> ids) {
        ${tableNameByLowercaseName}Service.deleteList(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}