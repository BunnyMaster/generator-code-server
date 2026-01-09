package com.auth.module.generator.core.mode.impl;

import com.auth.module.generator.core.mode.ModeStrategy;
import com.auth.module.generator.exception.GeneratorCodeException;
import com.auth.module.generator.model.vo.ElementOptionVO;
import com.auth.module.generator.model.vo.GeneratedFileItemVO;
import com.auth.module.generator.model.vo.GenerationResultVO;
import com.auth.module.generator.model.vo.result.Result;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Component
public class WriteModeStrategy implements ModeStrategy<Result<String>> {

    /**
     * 获取模式名称
     *
     * @return 模式名称
     */
    @Override
    public ElementOptionVO getModeName() {
        return ElementOptionVO.builder().label("写入").value("write").build();
    }

    /**
     * 写入
     *
     * @param resultList 生成器配置
     * @return 生成结果
     */
    @Override
    public Result<String> operation(List<GenerationResultVO> resultList) {
        for (GenerationResultVO resultVO : resultList) {
            List<GeneratedFileItemVO> children = resultVO.getChildren();
            for (GeneratedFileItemVO itemVO : children) {
                write(itemVO.getOutputDirFile(), itemVO.getOverwrite(), itemVO.getCode());
            }
        }

        return Result.success("写入成功");
    }

    /**
     * 根据模板配置将数据模型渲染到文件中
     *
     * @param outputFilePath 输出文件路径
     * @param overwrite      是否覆盖已存在的文件
     * @param content        模板内容
     */
    public void write(String outputFilePath, boolean overwrite, String content) {
        try {
            // 准备文件路径和写入选项
            Path witeFilePath = Path.of(outputFilePath);

            // 创建目录并写入文件（单次IO操作）
            Files.createDirectories(witeFilePath.getParent());

            // 判断是否要覆盖已存在的文件
            StandardOpenOption[] options = overwrite
                    ? new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING}
                    : new StandardOpenOption[]{StandardOpenOption.CREATE_NEW};

            Files.writeString(witeFilePath, content, StandardCharsets.UTF_8, options);
        } catch (IOException exception) {
            throw new GeneratorCodeException("文件写入失败: " + exception.getMessage(), exception);
        }
    }
}
