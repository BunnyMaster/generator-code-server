package com.auth.module.generator.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class NameConvertUtilTest {

    /**
     * 转换为蛇形命名
     */
    @Test
    void toSnakeCase() {
        String snakeCase_1 = NameConvertUtil.toSnakeCase("AppName");
        Assertions.assertEquals("/app_name", snakeCase_1);
        System.out.println(snakeCase_1);

        String snakeCase_2 = NameConvertUtil.toSnakeCase("appName");
        Assertions.assertEquals("app_name", snakeCase_2);
        System.out.println(snakeCase_2);

        String snakeCase_3 = NameConvertUtil.toSnakeCase("appname");
        Assertions.assertEquals("appname", snakeCase_3);

        String snakeCase_4 = NameConvertUtil.toSnakeCase("app-name");
        Assertions.assertEquals("app_name", snakeCase_4);
        System.out.println(snakeCase_4);

        String snakeCase_5 = NameConvertUtil.toSnakeCase("App-Name");
        Assertions.assertEquals("app_name", snakeCase_5);
        System.out.println(snakeCase_5);

        String snakeCase_6 = NameConvertUtil.toSnakeCase("App-name");
        System.out.println(snakeCase_6);
    }

    @Test
    void toKebabCaseTest() {
        String case_1 = NameConvertUtil.toKebabCase("appName");
        Assertions.assertEquals("app-name", case_1);
        System.out.println(case_1);

        String case_2 = NameConvertUtil.toKebabCase("AppName");
        Assertions.assertEquals("app-name", case_2);
        System.out.println(case_2);

        String case_3 = NameConvertUtil.toKebabCase("appname");
        Assertions.assertEquals("appname", case_3);
        System.out.println(case_3);

        String case_4 = NameConvertUtil.toKebabCase("app-name");
        Assertions.assertEquals("app-name", case_4);
        System.out.println(case_4);

        String case_5 = NameConvertUtil.toKebabCase("App-Name");
        Assertions.assertEquals("app-name", case_5);
        System.out.println(case_5);

        String case_6 = NameConvertUtil.toKebabCase("App-name");
        Assertions.assertEquals("app-name", case_6);
        System.out.println(case_6);
    }
}