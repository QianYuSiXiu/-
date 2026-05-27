package com.qianyu.controller;

import com.qianyu.util.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UploadController {

    @Value("${upload.path:D:/upload/}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("=== 开始文件上传 ===");

            // 1. 检查文件是否为空
            if (file == null || file.isEmpty()) {
                return Result.fail("上传文件不能为空");
            }

            // 2. 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.fail("只能上传图片文件");
            }

            // 3. 检查文件大小（2MB限制）
            long size = file.getSize();
            if (size > 2 * 1024 * 1024) {
                return Result.fail("文件大小不能超过2MB");
            }

            // 4. 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            // 5. 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 6. 保存文件
            File dest = new File(uploadPath + newFilename);
            file.transferTo(dest);

            // 7. 返回正确的相对路径（只返回文件名，前端拼接）
            String fileUrl = newFilename; // 只返回文件名

            System.out.println("文件上传成功: " + dest.getAbsolutePath());
            System.out.println("返回的文件名: " + fileUrl);
            System.out.println("前端访问地址: http://localhost:8088/upload/" + fileUrl);
            System.out.println("=== 文件上传结束 ===");

            return Result.success("上传成功", fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }
}