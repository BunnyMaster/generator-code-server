package com.auth.module.generator.core.mode.impl;

import com.alibaba.fastjson2.JSON;
import com.auth.module.generator.core.mode.ModeStrategy;
import com.auth.module.generator.model.vo.ElementOptionVO;
import com.auth.module.generator.model.vo.GenerationResultVO;
import com.auth.module.generator.model.vo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PrintModeStrategy implements ModeStrategy<Result<List<GenerationResultVO>>> {
    /**
     * 获取模式名称
     *
     * @return 模式名称
     */
    @Override
    public ElementOptionVO getModeName() {
        return ElementOptionVO.builder().value("print").label("打印").build();
    }

    /**
     * 生成的扣件
     *
     * @param resultList 生成器配置
     * @return 生成结果
     */
    @Override
    public Result<List<GenerationResultVO>> operation(List<GenerationResultVO> resultList) {
        log.info("打印结果：{}", JSON.toJSONString(resultList));

        return Result.success(resultList);
    }
}
