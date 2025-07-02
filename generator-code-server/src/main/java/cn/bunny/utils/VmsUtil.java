package cn.bunny.utils;

import cn.bunny.domain.dto.VmsArgumentDto;
import com.google.common.base.CaseFormat;

import java.util.Map;

/**
 * 代码生成工具类
 */
public class VmsUtil {
    private static final Map<String, String> FILE_TYPE_SUFFIXES = Map.of(
            "controller", "Controller",
            "service", "Service",
            "serviceImpl", "ServiceImpl",
            "mapper", "Mapper",
            "resourceMapper", "Mapper",
            "dto", "Dto",
            "vo", "Vo"
    );

    /**
     * 处理模板文件路径和命名
     *
     * @param dto       生成参数
     * @param path      原始模板路径
     * @param tableName 数据库表名
     * @return 处理后的文件路径
     */
    public static String processVmPath(VmsArgumentDto dto, String path, String tableName) {
        String className = removeTablePrefixes(dto, tableName);
        String lowerCamelCase = MysqlTypeConvertUtil.convertToCamelCase(tableName, false);
        String[] pathParts = path.replace("$className", lowerCamelCase).split("/");

        // 处理文件名
        pathParts[pathParts.length - 1] = processFilename(
                pathParts[pathParts.length - 1],
                className
        );

        return String.join("/", pathParts);
    }

    /**
     * 移除表前缀
     */
    private static String removeTablePrefixes(VmsArgumentDto dto, String tableName) {
        String[] prefixes = dto.getTablePrefixes().split("[,，]");
        for (String prefix : prefixes) {
            if (tableName.startsWith(prefix)) {
                return tableName.substring(prefix.length());
            }
        }
        return tableName;
    }

    /**
     * 处理文件名生成
     */
    private static String processFilename(String filename, String tableName) {
        filename = filename.replace(".vm", "");
        String[] parts = filename.split("\\.");
        String baseName = parts[0];
        String extension = parts.length > 1 ? parts[1] : "";

        String upperCamelCase = MysqlTypeConvertUtil.convertToCamelCase(tableName, true);
        String lowerCamelCase = MysqlTypeConvertUtil.convertToCamelCase(tableName, false);

        // 如果包含Java和xml需要进行处理
        if (filename.contains("java") || filename.contains("xml")) {
            return upperCamelCase + FILE_TYPE_SUFFIXES.getOrDefault(baseName, "") + "." + extension;
        }

        if (filename.equals("api.ts") || filename.equals("store.ts")) {
            return lowerCamelCase + ".ts";
        }

        if (filename.equals("dialog.vue")) {
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, lowerCamelCase) + "-dialog.vue";
        }

        return filename;
    }
}