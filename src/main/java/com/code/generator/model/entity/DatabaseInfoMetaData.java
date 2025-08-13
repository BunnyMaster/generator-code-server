package com.code.generator.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DatabaseInfoMetaData", description = "数据库信息")
public class DatabaseInfoMetaData {

    @Schema(name = "databaseList", description = "数据库所有的数据库")
    List<TableMetaData> databaseList;

    @Schema(name = "databaseProductName", description = "数据库产品名称")
    private String databaseProductName;

    @Schema(name = "databaseProductVersion", description = "数据库产品版本")
    private String databaseProductVersion;

    @Schema(name = "driverName", description = "驱动名称")
    private String driverName;

    @Schema(name = "driverVersion", description = "数据库驱动版本")
    private String driverVersion;

    @Schema(name = "url", description = "数据链接url")
    private String url;

    @Schema(name = "username", description = "数据库用户")
    private String username;

    @Schema(name = "currentDatabase", description = "当前数据库名称")
    private String currentDatabase;

}