package com.auth.module.generator.utils;

import com.auth.module.generator.model.enums.FilenameTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
class PathBuilderUtilTest {
    private static final String CONVERT_NAME = "tb_hotel";

    private static final List<String> TABLE_PREFIXES = List.of("t_", "sys_");


    /**
     * 测试路径前后都没有 /
     */
    @Test
    void buildOutputDir_prefixWithNone_suffixWithNone() {
        String baseOutputDir = "src";
        String typeOutputDir = "DirA/DirB";
        String outputSubDir = "view/index";
        String buildOutputDir = PathBuilderUtil.buildOutputDir(baseOutputDir, CONVERT_NAME, typeOutputDir, outputSubDir);

        log.info(buildOutputDir);
        Assertions.assertEquals("src/tb_hotel/DirA/DirB/view/index", buildOutputDir);
    }

    /**
     * 测试路径前有 / 后面没有 /
     */
    @Test
    void buildOutputDir_prefixWitNotNone_suffixWithNone() {
        String baseOutputDir = "/src";
        String typeOutputDir = "/DirA/DirB";
        String outputSubDir = "/view/index";
        String buildOutputDir = PathBuilderUtil.buildOutputDir(baseOutputDir, CONVERT_NAME, typeOutputDir, outputSubDir);

        log.info(buildOutputDir);
        Assertions.assertEquals("/src/tb_hotel/DirA/DirB/view/index", buildOutputDir);
    }

    @Test
    void buildOutputDir_prefixWithNone_suffixWithNotNone() {
        String baseOutputDir = "src/";
        String typeOutputDir = "DirA/DirB/";
        String outputSubDir = "View/index/";
        String buildOutputDir = PathBuilderUtil.buildOutputDir(baseOutputDir, CONVERT_NAME, typeOutputDir, outputSubDir);

        log.info(buildOutputDir);
        Assertions.assertEquals("src/tb_hotel/DirA/DirB/View/index/", buildOutputDir);
    }

    @Test
    void buildOutputDir_prefixWitNotNone_suffixWithNotNone() {
        String baseOutputDir = "/src/";
        String typeOutputDir = "/DirA/DirB/";
        String outputSubDir = "/View/index/";
        String buildOutputDir = PathBuilderUtil.buildOutputDir(baseOutputDir, CONVERT_NAME, typeOutputDir, outputSubDir);

        log.info(buildOutputDir);
        Assertions.assertEquals("/src/tb_hotel/DirA/DirB/View/index/", buildOutputDir);
    }

    /**
     * 测试文件名转换 Case 1
     */
    @Test
    void buildOutputDirFile_case_1() {
        String outputDir = "/src/tb_hotel/DirA/DirB/View/index";
        String prefix = "dialog";
        String formattedTableName = "tbHotel/1";
        String suffix = "VO";
        String fileExtension = ".java";

        String cleanInvalid = PathBuilderUtil.buildOutputDirFile(outputDir, prefix, formattedTableName, suffix, fileExtension, FilenameTypeEnum.LOWER_CAMEL);
        Assertions.assertEquals("/src/tb_hotel/DirA/DirB/View/index/dialogtbHotel1VO.java", cleanInvalid);
    }

    /**
     * 测试文件名转换 Case 2
     */
    @Test
    void buildOutputDirFile_case_2() {
        String outputDir = "/src/tb_hotel/DirA/DirB/View/index/";
        String prefix = "dialog-";
        String formattedTableName = "tbHotel/1";
        String suffix = "VO";
        String fileExtension = ".java";

        String cleanInvalid = PathBuilderUtil.buildOutputDirFile(outputDir, prefix, formattedTableName, suffix, fileExtension, FilenameTypeEnum.LOWER_CAMEL);
        Assertions.assertEquals("/src/tb_hotel/DirA/DirB/View/index/dialog-tbHotel1VO.java", cleanInvalid);
    }

    @Test
    void removeTablePrefix() {
        String removeTablePrefix = PathBuilderUtil.removeTablePrefix(CONVERT_NAME, TABLE_PREFIXES);
        Assertions.assertEquals("name", removeTablePrefix);
    }
}