package com.auth.module.generator.core.provider.metadata;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class MetadataProviderContext {

    private final Map<String, MetadataProvider> metadataProviderMap = new ConcurrentHashMap<>();

    public MetadataProviderContext(@Valid List<MetadataProvider> metadataProviderList) {
        // 再转换 Map
        Map<String, MetadataProvider> providerMap = metadataProviderList.stream()
                .collect(Collectors.toMap(metadataProvider -> metadataProvider.getProviderType().getValue(), provider -> provider));

        metadataProviderMap.putAll(providerMap);
    }

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
}
