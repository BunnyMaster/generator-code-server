package com.auth.module.generator.controller;

import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.entity.generator.extra.DatabaseConfigEntity;
import com.auth.module.generator.model.vo.ElementOptionVO;
import com.auth.module.generator.model.vo.SupportEnumsVO;
import com.auth.module.generator.model.vo.result.Result;
import com.auth.module.generator.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "配置接口")
@RequestMapping("/api/config")
@RestController
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @Operation(summary = "获取通用配置")
    @GetMapping("generator-config")
    public Result<GenerationConfigDTO<DatabaseConfigEntity>> getDatabaseConfig() {
        GenerationConfigDTO<DatabaseConfigEntity> config = configService.getDatabaseConfig();

        return Result.success(config);
    }

    @Operation(summary = "支持哪些生成内容")
    @GetMapping("support")
    public Result<SupportEnumsVO> getSupportEnums() {
        SupportEnumsVO vo = configService.getSupportEnums();
        return Result.success(vo);
    }

    @Operation(summary = "获取 FilenameTypeEnums")
    @GetMapping("filename-type-enums")
    public Result<List<ElementOptionVO>> getFilenameTypeEnums() {
        List<ElementOptionVO> list = configService.getFilenameTypeEnums();
        return Result.success(list);
    }
}
