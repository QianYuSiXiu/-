package com.qianyu.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ImageUploadUtil {

    // 图片上传的根目录
    public static final String UPLOAD_DIR = "E:/javaKuangJia/shixun/kunvue/kun_vue/img/";

    /**
     * 上传图片文件
     * @param file 上传的文件
     * @return 图片访问路径
     */
    public static String uploadImage(MultipartFile file) throws IOException {
        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件不能为空");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只允许上传图片文件");
        }

        // 创建日期目录
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uploadPath = UPLOAD_DIR + dateDir + "/";
        
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 创建目录
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + fileExtension;

        // 完整的文件路径
        String fullPath = uploadPath + uniqueFileName;

        // 保存文件
        File destFile = new File(fullPath);
        file.transferTo(destFile);

        // 返回可以访问的路径（相对于img目录）
        return "/img/" + dateDir + "/" + uniqueFileName;
    }
}