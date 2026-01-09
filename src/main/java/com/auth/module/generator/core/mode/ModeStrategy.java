package com.auth.module.generator.core.mode;

import com.auth.module.generator.model.vo.ElementOptionVO;
import com.auth.module.generator.model.vo.GenerationResultVO;

import java.util.List;

public interface ModeStrategy<T> {

    /**
     * 获取模式名称
     *
     * @return 模式名称
     */
    ElementOptionVO getModeName();

    /**
     * 生成的扣件
     *
     * @param resultVOS 生成器配置
     * @return 生成结果
     */
    T operation(List<GenerationResultVO> resultVOS);
}
