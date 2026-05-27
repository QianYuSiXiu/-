//package com.qianyu.util;
//
//import cn.hutool.jwt.JWT;
//import cn.hutool.jwt.JWTUtil;
//import cn.hutool.jwt.signers.JWTSigner;
//import cn.hutool.jwt.signers.JWTSignerUtil;
//import jakarta.servlet.http.HttpServletRequest;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 基于 Hutool 的 JWT Token 工具类
// */
//public class TokenUtil {
//
//    // 密钥（生产环境建议配置在配置文件中，且密钥长度不低于 16 位）
//    private static final String SECRET_KEY = "easy";
//    // Token 过期时间（单位：秒），此处设置为 2 小时
//    private static final long EXPIRE_SECONDS = 7200L;
//
//    // 获取签名器（HS256 算法）
//    private static JWTSigner getSigner() {
//        return JWTSignerUtil.hs256(SECRET_KEY.getBytes());
//    }
//
//    /**
//     * 生成 Token
//     * @param userId 用户ID（自定义载荷数据）
//     * @return JWT Token 字符串
//     */
//    public static String generateUserToken(String userId) {
//        // 1. 构建载荷数据
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("userId", userId);
//        // 2. 生成 Token
//        return JWTUtil.createToken(payload, getSigner());
//    }
//
//    /**
//     * 生成 Token
//     * @param adminId 用户ID（自定义载荷数据）
//     * @return JWT Token 字符串
//     */
//    public static String generateAdminToken(String adminId) {
//        // 1. 构建载荷数据
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("adminId", adminId);
//        // 2. 生成 Token
//        return JWTUtil.createToken(payload, getSigner());
//    }
//
//    /**
//     * 校验 Token 有效性
//     * @param token Token 字符串
//     * @return true=有效，false=无效/过期
//     */
//    public static boolean verifyToken(String token) {
//        try {
//            JWT jwt = JWTUtil.parseToken(token).setSigner(getSigner());
//            // 校验签名 + 过期时间
//            return jwt.verify() ;
//        } catch (Exception e) {
//            // 签名错误、Token 格式错误、过期等异常均视为无效
//            return false;
//        }
//    }
//
//    /**
//     * 从 Token 中解析载荷内容
//     * @param token Token 字符串
//     * @return 载荷数据 Map
//     */
//    public static Map<String, Object> parseTokenPayload(String token) {
//        if (!verifyToken(token)) {
//            throw new RuntimeException("Token 无效或已过期");
//        }
//        return JWTUtil.parseToken(token).getPayload().getClaimsJson();
//    }
//
//    /**
//     * 从 Token 中获取指定字段值
//     * @param token Token 字符串
//     * @param field 字段名
//     * @return 字段值
//     */
//    public static Object getFieldFromToken(String token, String field) {
//        return parseTokenPayload(token).get(field);
//    }
//
//    /**
//     * 通过request对象获取token中的登录用户的id
//     * @param request
//     * @return
//     */
//    public static int getLoginUserID(HttpServletRequest request){
//        String token=request.getHeader("Authorization");//token
//        Object obj=getFieldFromToken(token,"userId");
//        return Integer.valueOf(obj+"");
//    }
//
//    /**
//     * 通过request对象获取token中的登录用户的id
//     * @param request
//     * @return
//     */
//    public static int getLoginAdminID(HttpServletRequest request){
//        String token=request.getHeader("Authorization");//token
//        Object obj=getFieldFromToken(token,"adminId");
//        return Integer.valueOf(obj+"");
//    }
//}

package com.qianyu.util;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于 Hutool 的 JWT Token 工具类
 */
public class TokenUtil {

    // 密钥（生产环境建议配置在配置文件中，且密钥长度不低于 16 位）
    private static final String SECRET_KEY = "easy";
    // Token 过期时间（单位：秒），此处设置为 2 小时
    private static final long EXPIRE_SECONDS = 7200L;

    // 获取签名器（HS256 算法）
    private static JWTSigner getSigner() {
        return JWTSignerUtil.hs256(SECRET_KEY.getBytes());
    }

    /**
     * 生成 Token
     * @param userId 用户ID（自定义载荷数据）
     * @return JWT Token 字符串
     */
    public static String generateUserToken(String userId) {
        // 1. 构建载荷数据
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        // 设置过期时间
        payload.put("exp", System.currentTimeMillis() / 1000 + EXPIRE_SECONDS);
        // 2. 生成 Token
        String token = JWTUtil.createToken(payload, getSigner());
        System.out.println("生成的用户JWT token: " + token);
        return token;
    }

    /**
     * 生成 Token
     * @param adminId 用户ID（自定义载荷数据）
     * @return JWT Token 字符串
     */
    public static String generateAdminToken(String adminId) {
        // 1. 构建载荷数据
        Map<String, Object> payload = new HashMap<>();
        payload.put("adminId", adminId);
        // 设置过期时间
        payload.put("exp", System.currentTimeMillis() / 1000 + EXPIRE_SECONDS);
        // 2. 生成 Token
        String token = JWTUtil.createToken(payload, getSigner());
        System.out.println("生成的管理员JWT token: " + token);
        return token;
    }

    /**
     * 校验 Token 有效性
     * @param token Token 字符串
     * @return true=有效，false=无效/过期
     */
    public static boolean verifyToken(String token) {
        try {
            System.out.println("开始验证JWT token...");
            System.out.println("待验证的token: " + (token.length() > 50 ? token.substring(0, 50) + "..." : token));

            JWT jwt = JWTUtil.parseToken(token).setSigner(getSigner());

            // 检查是否过期
            Object expObj = jwt.getPayload("exp");
            if (expObj != null) {
                long exp = Long.parseLong(expObj.toString());
                long current = System.currentTimeMillis() / 1000;
                System.out.println("token过期时间: " + exp + ", 当前时间: " + current);
                if (exp < current) {
                    System.out.println("token已过期");
                    return false;
                }
            }

            // 校验签名
            boolean verified = jwt.verify();
            System.out.println("JWT token验证结果: " + verified);
            return verified;

        } catch (Exception e) {
            // 签名错误、Token 格式错误、过期等异常均视为无效
            System.out.println("JWT token验证异常: " + e.getMessage());
            return false;
        }
    }

    /**
     * 从 Token 中解析载荷内容
     * @param token Token 字符串
     * @return 载荷数据 Map
     */
    public static Map<String, Object> parseTokenPayload(String token) {
        if (!verifyToken(token)) {
            throw new RuntimeException("Token 无效或已过期");
        }
        return JWTUtil.parseToken(token).getPayload().getClaimsJson();
    }

    /**
     * 从 Token 中获取指定字段值
     * @param token Token 字符串
     * @param field 字段名
     * @return 字段值
     */
    public static Object getFieldFromToken(String token, String field) {
        return parseTokenPayload(token).get(field);
    }

    /**
     * 通过request对象获取token中的登录用户的id
     * @param request
     * @return
     */
    public static int getLoginUserID(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization头格式错误");
        }
        String token = authHeader.substring(7);
        Object obj = getFieldFromToken(token, "userId");
        if (obj == null) {
            throw new RuntimeException("Token中未找到用户ID");
        }
        return Integer.valueOf(obj.toString());
    }

    /**
     * 通过request对象获取token中的登录用户的id
     * @param request
     * @return
     */
    public static int getLoginAdminID(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        System.out.println("getLoginAdminID接收的Authorization头: " + authHeader);

        if (authHeader == null) {
            throw new RuntimeException("缺少Authorization头");
        }

        // 处理Bearer前缀
        String token = authHeader;
        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        System.out.println("提取的token: " + (token.length() > 50 ? token.substring(0, 50) + "..." : token));

        Object obj = getFieldFromToken(token, "adminId");
        if (obj == null) {
            throw new RuntimeException("Token中未找到管理员ID");
        }
        return Integer.valueOf(obj.toString());
    }
}