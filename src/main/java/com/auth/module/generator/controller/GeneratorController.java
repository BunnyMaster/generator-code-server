package com.auth.module.generator.controller;

import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.dto.GenerationStrategyDTO;
import com.auth.module.generator.service.GeneratorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "生成接口")
@RequestMapping("/api/generator")
@RestController
public class GeneratorController {
    private final GeneratorService generatorService;

    public GeneratorController(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @PostMapping("/generate")
    public Object generate(@RequestBody GenerationConfigDTO<Object> generationConfig,
                           GenerationStrategyDTO strategyDTO) {
        return generatorService.generate(generationConfig, strategyDTO);
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> download(@RequestBody GenerationConfigDTO<Object> generationConfig,
                                           GenerationStrategyDTO strategyDTO) {
        strategyDTO.setMode("download_zip");
        return generatorService.download(generationConfig, strategyDTO);
    }
}
