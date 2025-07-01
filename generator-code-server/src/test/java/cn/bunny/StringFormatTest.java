package cn.bunny;

import cn.bunny.utils.TypeConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.introspection.CaseFormatUtils;
import org.junit.jupiter.api.Test;

public class StringFormatTest {

    @Test
    void test1() {
        System.out.println(CaseFormatUtils.toCamelCase("user_login"));
        System.out.println(CaseFormatUtils.toCamelCase("userLogin"));
        System.out.println(CaseFormatUtils.toCamelCase("UserLogin"));

        System.out.println("--------------------------------");

        System.out.println(StringUtils.lowerCase("user_login"));
        System.out.println(StringUtils.lowerCase("userLogin"));
        System.out.println(StringUtils.lowerCase("UserLogin"));

        System.out.println("--------------------------------");

        System.out.println(StringUtils.upperCase("user_login"));
        System.out.println(StringUtils.upperCase("userLogin"));
        System.out.println(StringUtils.upperCase("UserLogin"));
    }

    @Test
    void test2() {
        System.out.println(TypeConvertUtil.convertToCamelCase("user_login_A"));
        System.out.println(TypeConvertUtil.convertToCamelCase("User_Login_A"));
        System.out.println(TypeConvertUtil.convertToCamelCase("userLoginA"));
        System.out.println(TypeConvertUtil.convertToCamelCase("UserLoginA"));

        System.out.println("--------------------------------");

        System.out.println(TypeConvertUtil.convertToCamelCase("i18n_type_A", true));
        System.out.println(TypeConvertUtil.convertToCamelCase("User_Login_A", true));
        System.out.println(TypeConvertUtil.convertToCamelCase("userLoginA", true));
        System.out.println(TypeConvertUtil.convertToCamelCase("UserLoginA", true));
    }
}
