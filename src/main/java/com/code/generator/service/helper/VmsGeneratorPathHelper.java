package com.code.generator.service.helper;

import com.code.generator.model.dto.VmsArgumentDto;
import com.code.generator.utils.CamelCaseNameConvertUtil;
import org.apache.velocity.shaded.commons.io.FilenameUtils;

import java.nio.file.Paths;

/**
 * 代码生成工具类
 */
public class VmsGeneratorPathHelper {

    /**
     * 处理模板文件路径和命名
     *
     * @param path      原始模板路径
     * @param tableName 数据库表名
     * @param dto       生成代码请求参数
     * @return 处理后的文件路径
     */
    public static String processVmPath(String path, String tableName, VmsArgumentDto dto) {
        String lowerCamelCase = CamelCaseNameConvertUtil.convertToCamelCase(tableName, false);
        String webBasePath = dto.getWebBasePath();

        if (lowerCamelCase != null && path.contains("$className")) {
            // 💡 替换文件中的 特殊名称
            path = path.replace("$className", lowerCamelCase);
        }

        if (webBasePath != null && path.contains("$webBasePath")) {
            webBasePath = webBasePath.replace("/", "");
            // 💡 替换文件中的 特殊名称
            path = path.replace("$webBasePath", webBasePath);
        }

        // 获取当前路径的文件名
        String filename = Paths.get(path).getFileName().toString();

        // 处理文件名
        String processedFilename = processFilename(filename, tableName);

        return path.replace(filename, processedFilename);
    }

    /**
     * 处理文件名生成
     */
    private static String processFilename(String filename, String tableName) {
        filename = filename.replace(".vm", "");
        String baseName = FilenameUtils.getBaseName(filename);
        String extension = FilenameUtils.getExtension(filename);

        String upperCamelCase = CamelCaseNameConvertUtil.convertToCamelCase(tableName, true);
        String lowerCamelCase = CamelCaseNameConvertUtil.convertToCamelCase(tableName, false);

        // 💡含Java和xml需要进行处理
        if (filename.contains(".java") || filename.contains(".xml")) {
            String toUpperCamelCase = CamelCaseNameConvertUtil.convertToCamelCase(baseName, true);
            return upperCamelCase + toUpperCamelCase + "." + extension;
        }

        // 💡前端配置
        return switch (filename) {
            case "api.ts", "store.ts" -> lowerCamelCase + ".ts";
            case "types.ts" -> lowerCamelCase + "DataType.ts";
            case "dialog.vue" -> upperCamelCase + "Dialog.vue";
            case "columns.tsx" -> lowerCamelCase + "-columns.tsx";
            default -> filename;
        };

    }
}