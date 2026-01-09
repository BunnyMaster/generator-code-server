package com.auth.module.generator.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.auth.module.generator.model.enums.FilenameTypeEnum;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PathBuilderUtil {

    /**
     * 构建输出目录
     *
     * @param baseOutputDir 基础输出目录
     * @param convertName   转换后的名称
     * @param typeOutputDir 类型输出目录
     * @param outputSubDir  子目录
     * @return 完整的输出目录路径
     */
    public String buildOutputDir(String baseOutputDir, String convertName,
                                 String typeOutputDir, String outputSubDir) {
        String path = baseOutputDir + FileNameUtil.UNIX_SEPARATOR +
                convertName + FileNameUtil.UNIX_SEPARATOR +
                typeOutputDir + FileNameUtil.UNIX_SEPARATOR +
                outputSubDir;

        return FileUtil.normalize(path);
    }

    /**
     * 构建输出文件路径
     */
    public String buildOutputDirFile(String outputDir,
                                     String prefix, String formattedTableName, String suffix, String fileExtension,
                                     FilenameTypeEnum filenameType) {
        // 根据文件名类型格式化表名
        String filename = prefix +
                FilenameTypeEnum.convertFilename(formattedTableName, filenameType) +
                suffix +
                fileExtension;
        String cleanInvalidFilename = FileNameUtil.cleanInvalid(filename);

        return FileUtil.file(outputDir, cleanInvalidFilename).getPath();
    }

    /**
     * 去除表前缀
     */
    public String removeTablePrefix(String convertName, List<String> ignoreTablePrefixes) {
        if (ignoreTablePrefixes == null || ignoreTablePrefixes.isEmpty()) {
            return convertName;
        }

        String result = convertName;
        for (String prefix : ignoreTablePrefixes) {
            if (result.startsWith(prefix)) {
                result = result.substring(prefix.length());
                break;
            }
        }

        return result;
    }
}