package com.auth.module.generator.core.mode.validate;

import com.auth.module.generator.model.vo.ElementOptionVO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ModeStrategyContextValidationService {
    /**
     * 校验数据库类型
     *
     * @param providerType 数据库类型
     */
    public void validateSupportProviderValue(@Valid ElementOptionVO providerType) {
        // 这里会自动进行校验
    }
}
