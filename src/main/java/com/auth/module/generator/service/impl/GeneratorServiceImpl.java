package com.auth.module.generator.service.impl;

import com.auth.module.generator.core.mode.ModeStrategy;
import com.auth.module.generator.core.mode.ModeStrategyContext;
import com.auth.module.generator.core.mode.impl.DownloadZipModeStrategy;
import com.auth.module.generator.core.provider.metadata.MetadataProvider;
import com.auth.module.generator.core.provider.metadata.MetadataProviderContext;
import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.dto.GenerationStrategyDTO;
import com.auth.module.generator.model.vo.GenerationResultVO;
import com.auth.module.generator.service.GeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GeneratorServiceImpl implements GeneratorService {

    private final ModeStrategyContext modeStrategyContext;

    private final MetadataProviderContext metadataProviderContext;

    private final DownloadZipModeStrategy downloadZipModeStrategy;

    public GeneratorServiceImpl(ModeStrategyContext modeStrategyContext, MetadataProviderContext metadataProviderContext, DownloadZipModeStrategy downloadZipModeStrategy) {
        this.modeStrategyContext = modeStrategyContext;
        this.metadataProviderContext = metadataProviderContext;
        this.downloadZipModeStrategy = downloadZipModeStrategy;
    }

    /**
     * 生成代码
     *
     * @param generationConfig 生成器配置
     * @param strategyDTO      生成策略
     * @return 生成结果
     */
    @Override
    public Object generate(GenerationConfigDTO<Object> generationConfig, GenerationStrategyDTO strategyDTO) {
        String provider = strategyDTO.getProvider();
        MetadataProvider abstractDatabaseMetadataProvider = metadataProviderContext.getProvider(provider);
        List<GenerationResultVO> resultList = abstractDatabaseMetadataProvider.getResultList(generationConfig);

        String mode = strategyDTO.getMode();
        ModeStrategy<?> modeStrategy = modeStrategyContext.getStrategy(mode);
        return modeStrategy.operation(resultList);
    }

    /**
     * 下载代码
     *
     * @param generationConfig 配置
     * @param strategyDTO      生成策略
     * @return 下载结果
     */
    @Override
    public ResponseEntity<byte[]> download(GenerationConfigDTO<Object> generationConfig, GenerationStrategyDTO strategyDTO) {
        String provider = strategyDTO.getProvider();
        MetadataProvider abstractDatabaseMetadataProvider = metadataProviderContext.getProvider(provider);
        List<GenerationResultVO> resultList = abstractDatabaseMetadataProvider.getResultList(generationConfig);

        return downloadZipModeStrategy.operation(resultList);
    }
}
