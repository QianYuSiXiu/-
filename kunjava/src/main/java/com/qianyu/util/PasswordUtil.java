package com.qianyu.util;

import org.springframework.util.DigestUtils;

/**
 * 密码加密/验证工具类
 */
public class PasswordUtil {
    // 默认初始密码
    public static final String DEFAULT_PASSWORD = "123456";
    // 盐值（生产环境建议配置在配置文件）
    private static final String SALT = "qianyu_2024";

    /**
     * 密码加密（MD5 + 盐）
     */
    public static String encodePassword(String password) {
        if (password == null) {
            password = DEFAULT_PASSWORD;
        }
        // 拼接盐值后加密
        return DigestUtils.md5DigestAsHex((password + SALT).getBytes());
    }

    /**
     * 验证密码（明文 vs 加密后密码）
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encodePassword(rawPassword).equals(encodedPassword);
    }
}