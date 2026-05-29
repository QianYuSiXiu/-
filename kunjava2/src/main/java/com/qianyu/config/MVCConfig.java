//package com.qianyu.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class MVCConfig implements WebMvcConfigurer {
//
//    // 跨域配置
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // 所有接口允许跨域
//                .allowedOriginPatterns("*") // 允许所有域名（生产环境需指定具体域名）
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
//                .allowedHeaders("*") // 允许的请求头
//                .allowCredentials(true) // 允许携带Cookie
//                .maxAge(3600); // 预检请求缓存时间
//    }
//
//    // 静态资源映射（原有逻辑保留）
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/img/**")
//                .addResourceLocations("file:D:/image/");
//        // 扩展：增加前端静态资源映射（可选）
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/");
//    }
//}

/*
package com.qianyu.config;
import com.qianyu.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    // 1. 配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/admin/login",  // 管理员登录
                        "/user/login",   // 用户登录
                        "/user/register", // 用户注册
                        "/img/**",       // 静态资源
                        "/static/**",
                        "/error",        // 错误页面
                        "/project/upload" // 图片上传接口
                );
    }

    // 2. 跨域配置（保持不变）
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    // 3. 静态资源映射（保持不变）
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:E:/javaKuangJia/shixun/kunvue/kun_vue/img/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
*/


package com.qianyu.config;

import com.qianyu.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Value("${upload.path:D:/upload/}")
    private String uploadPath;

    // 1. 配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/admin/login",    // 管理员登录
                        "/user/login",     // 用户登录
                        "/user/register",  // 用户注册
                        "/api/upload",     // 文件上传接口（需要token）
                        "/upload/**",      // 上传文件访问路径
                        "/img/**",         // 静态资源
                        "/static/**",      // 静态资源
                        "/error"           // 错误页面
                );
    }

    // 2. 跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    // 3. 静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 原有映射
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:E:/javaKuangJia/shixun/kunvue/kun_vue/img/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // 修改：映射上传文件访问路径 - 确保路径正确
        System.out.println("上传文件路径配置: " + uploadPath);
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + uploadPath)
                .setCachePeriod(0); // 不缓存
    }
}