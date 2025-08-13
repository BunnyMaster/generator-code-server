package com.code.generator.controller;

import com.code.generator.model.dto.VmsArgumentDto;
import com.code.generator.model.result.Result;
import com.code.generator.model.vo.GeneratorVo;
import com.code.generator.service.GeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器控制器
 * 提供代码生成和打包下载功能
 */
@Tag(name = "生成模板", description = "生成模板接口")
@RestController
@RequestMapping("/api/generator")
@RequiredArgsConstructor
public class GeneratorController {

    private final GeneratorService generatorService;

    @Operation(summary = "生成代码", description = "根据SQL或数据库表生成代码")
    @PostMapping
    public Result<Map<String, List<GeneratorVo>>> generator(@Valid @RequestBody VmsArgumentDto dto) {
        // 判断当前是使用 SQL语句 生成还是 数据库生成
        String sql = dto.getSql();

        Map<String, List<GeneratorVo>> result = Strings.isEmpty(sql)
                ? generatorService.generateCodeByDatabase(dto)
                : generatorService.generateCodeBySql(dto);
        return Result.success(result);
    }

    @Operation(summary = "打包下载", description = "将生成的代码打包为ZIP文件下载")
    @PostMapping("downloadByZip")
    public ResponseEntity<byte[]> downloadByZip(@Valid @RequestBody VmsArgumentDto dto) {
        // 判断当前是使用 SQL语句 生成还是 数据库生成
        String sql = dto.getSql();

        return Strings.isEmpty(sql)
                ? generatorService.downloadByZipByDatabase(dto)
                : generatorService.downloadByZipBySqL(dto);
    }
}