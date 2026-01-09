package com.auth.module.generator.core.mode;

import com.auth.module.generator.core.mode.validate.ModeStrategyContextValidationService;
import com.auth.module.generator.model.vo.ElementOptionVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ModeStrategyContext {

    private final Map<String, ModeStrategy<?>> strategies = new ConcurrentHashMap<>();

    public ModeStrategyContext(List<ModeStrategy<?>> strategyList, ModeStrategyContextValidationService validationService) {
        // 先校验所有提供器
        strategyList.forEach(provider -> {
            ElementOptionVO providerType = provider.getModeName();
            validationService.validateSupportProviderValue(providerType);
        });

        // 将策略列表转换为以策略类型为key 的map集合
        Map<String, ModeStrategy<?>> strategyMap = strategyList.stream()
                .collect(Collectors.toMap(modeStrategy -> modeStrategy.getModeName().getValue(), strategy -> strategy));
        strategies.putAll(strategyMap);
    }

    /**
     * 根据策略类型获取对应的生成策略
     *
     * @param mode 策略类型
     * @return 对应的生成策略对象
     */
    @SuppressWarnings("java:S1452")
    public ModeStrategy<?> getStrategy(String mode) {
        ModeStrategy<?> strategy = strategies.get(mode);

        if (strategy == null) {
            throw new IllegalArgumentException("不支持的生成策略: " + mode);
        }

        return strategy;
    }
}
