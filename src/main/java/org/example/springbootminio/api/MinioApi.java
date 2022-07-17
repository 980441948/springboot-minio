package org.example.springbootminio.api;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootminio.config.MinioProp;
import org.example.springbootminio.result.ResultVO;
import org.example.springbootminio.service.MinioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@Slf4j
public class MinioApi {

    @Resource
    private MinioService minioService;

    @Resource
    private MinioProp minioProperties;

    @GetMapping("/list")
    public List<Object> list() {

        List<Object> items = minioService.listObjects(minioProperties.getBucketName());

        return items;
    }

    @PostMapping("/upload")
    public ResultVO<String> uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            assert fileName != null;

            // 根据业务设计，设置存储路径：按天创建目录
            String objectName = new SimpleDateFormat("yyyy-MM-dd/").format(new Date())
                    + UUID.randomUUID().toString()
                    + fileName.substring(fileName.lastIndexOf("."));

            minioService.upload(file);
            log.info("文件格式为:{}", file.getContentType());
            log.info("文件原名称为:{}", fileName);
            log.info("文件对象路径为:{}", objectName);
            return ResultVO.success(minioService.getFileUrl(fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error("上传失败");
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public ResultVO<String> deleteFile(@PathVariable("fileName") String fileName) {

        boolean status = minioService.delFile(minioProperties.getBucketName(), fileName);
        return status ? ResultVO.success("删除成功") : ResultVO.error("删除失败");
    }
}