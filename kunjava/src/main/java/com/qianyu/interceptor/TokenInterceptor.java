//package com.qianyu.interceptor;
//
//import com.qianyu.util.TokenUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Component
//public class TokenInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 1. 允许跨域预检请求直接放行
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            return true;
//        }
//
//        // 2. 检查请求头中的Authorization字段
//        String token = request.getHeader("Authorization");
//
//        // 如果没有token，则拒绝访问
//        if (token == null || token.isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Unauthorized: Missing token");
//            return false;
//        }
//
//        // 验证token的有效性
//        if (!TokenUtil.verifyToken(token)) {
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            response.getWriter().write("Forbidden: Invalid or expired token");
//            return false;
//        }
//
//        // token有效，允许访问
//        return true;
//    }
//}

package com.qianyu.interceptor;

import com.qianyu.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 允许跨域预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 2. 添加详细日志
        System.out.println("\n=== TokenInterceptor 开始 ===");
        System.out.println("请求URI: " + request.getRequestURI());
        System.out.println("请求方法: " + request.getMethod());

        // 3. 检查请求头中的Authorization字段
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization头: " + authHeader);

        // 4. 如果没有Authorization头，则拒绝访问
        if (authHeader == null || authHeader.isEmpty()) {
            System.out.println("错误: 缺少Authorization头");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Missing token");
            return false;
        }

        // 5. 检查并提取token
        String token;
        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("提取的token (Bearer格式): " + (token.length() > 50 ? token.substring(0, 50) + "..." : token));
        } else {
            // 兼容旧格式，直接使用整个Authorization头作为token
            token = authHeader;
            System.out.println("提取的token (直接格式): " + token);
        }

        // 6. 验证token的有效性
        boolean isValid = false;
        try {
            System.out.println("开始验证token...");

            // 判断token类型并进行验证
            if (token.startsWith("admin_")) {
                // 旧格式token验证（临时兼容）
                System.out.println("检测到旧格式token");
                isValid = verifyOldToken(token);
            } else {
                // JWT格式token验证
                System.out.println("检测到JWT格式token");
                isValid = TokenUtil.verifyToken(token);
            }

            System.out.println("Token验证结果: " + isValid);

        } catch (Exception e) {
            System.out.println("Token验证异常: " + e.getMessage());
            e.printStackTrace();
        }

        if (!isValid) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: Invalid or expired token");
            System.out.println("=== TokenInterceptor 结束 (拒绝访问) ===\n");
            return false;
        }

        // 7. 从token中提取用户信息并设置到请求属性
        try {
            if (token.startsWith("admin_")) {
                // 旧格式：提取adminId（从token中解析或使用默认值）
                String adminId = extractAdminIdFromOldToken(token);
                request.setAttribute("adminId", adminId);
                request.setAttribute("username", "admin");
                System.out.println("旧格式token验证通过，adminId: " + adminId);
            } else {
                // JWT格式：从payload中提取信息
                Object adminId = TokenUtil.getFieldFromToken(token, "adminId");
                if (adminId != null) {
                    request.setAttribute("adminId", adminId.toString());
                    System.out.println("JWT token验证通过，adminId: " + adminId);
                } else {
                    // 也可能是用户token
                    Object userId = TokenUtil.getFieldFromToken(token, "userId");
                    if (userId != null) {
                        request.setAttribute("userId", userId.toString());
                        System.out.println("JWT token验证通过，userId: " + userId);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("提取用户信息失败: " + e.getMessage());
        }

        System.out.println("=== TokenInterceptor 结束 (允许访问) ===\n");
        return true;
    }

    /**
     * 验证旧格式token (admin_ + 时间戳)
     */
    private boolean verifyOldToken(String token) {
        try {
            System.out.println("验证旧格式token: " + token);

            // 检查格式
            if (!token.startsWith("admin_")) {
                System.out.println("旧格式token必须以 'admin_'开头");
                return false;
            }

            // 提取时间戳
            String timestampStr = token.substring(6);
            System.out.println("token时间戳字符串: " + timestampStr);

            long timestamp = Long.parseLong(timestampStr);
            long currentTime = System.currentTimeMillis();
            long tokenAge = currentTime - timestamp;
 
            System.out.println("token生成时间: " + new java.util.Date(timestamp));
            System.out.println("当前时间: " + new java.util.Date(currentTime));
            System.out.println("token年龄: " + tokenAge + "ms (" + (tokenAge/1000/60) + "分钟)");

            // 检查是否过期（24小时）
            long maxAge = 24 * 60 * 60 * 1000; // 24小时
            if (tokenAge > maxAge) {
                System.out.println("旧格式token已过期（超过24小时）");
                return false;
            }

            System.out.println("旧格式token验证成功");
            return true;

        } catch (NumberFormatException e) {
            System.out.println("旧格式token时间戳格式错误: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("验证旧格式token时发生异常: " + e.getMessage());
            return false;
        }
    }

    /**
     * 从旧格式token中提取adminId（简化版，实际应根据业务逻辑）
     */
    private String extractAdminIdFromOldToken(String token) {
        // 这里简化处理，返回默认值
        // 实际项目中，可能需要从数据库或缓存中根据token查找对应的adminId
        return "1"; // 默认返回第一个管理员
    }
}