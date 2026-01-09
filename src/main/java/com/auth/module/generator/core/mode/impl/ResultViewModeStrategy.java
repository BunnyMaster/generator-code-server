package com.auth.module.generator.core.mode.impl;

import com.auth.module.generator.core.mode.ModeStrategy;
import com.auth.module.generator.model.vo.ElementOptionVO;
import com.auth.module.generator.model.vo.GenerationResultVO;
import com.auth.module.generator.model.vo.result.Result;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResultViewModeStrategy implements ModeStrategy<Result<List<GenerationResultVO>>> {
    /**
     * 获取模式名称
     *
     * @return 模式名称
     */
    @Override
    public ElementOptionVO getModeName() {
        return ElementOptionVO.builder().value("result_view").label("结果视图").build();
    }

    /**
     * 获取结果视图
     *
     * @param resultList 配置
     * @return 结果视图
     */
    @Override
    public Result<List<GenerationResultVO>> operation(List<GenerationResultVO> resultList) {
        return Result.success(resultList);
    }
}
