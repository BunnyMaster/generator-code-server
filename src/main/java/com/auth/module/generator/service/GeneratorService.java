package com.auth.module.generator.service;

import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.dto.GenerationStrategyDTO;
import org.springframework.http.ResponseEntity;

public interface GeneratorService {

    /**
     * 生成代码
     *
     * @param generationConfig 生成器配置
     * @param strategyDTO      生成策略
     * @return 生成结果
     */
    Object generate(GenerationConfigDTO<Object> generationConfig, GenerationStrategyDTO strategyDTO);

    /**
     * 下载代码
     *
     * @param generationConfig 配置
     * @param strategyDTO      生成策略
     * @return 下载结果
     */
    ResponseEntity<byte[]> download(GenerationConfigDTO<Object> generationConfig, GenerationStrategyDTO strategyDTO);
}

