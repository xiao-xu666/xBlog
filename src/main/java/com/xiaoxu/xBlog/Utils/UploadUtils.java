package com.xiaoxu.xBlog.Utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UploadUtils {
    //阿里oss域名
    private static final String ALI_DOMAIN = "";


    public static String uploadImage(MultipartFile file) throws IOException {
        //生成文件名
        String fileName = getNewFileName(file);

        //仓库名
        String bucketName = "";
        //地域节点
        String endPoint = "";
        //AccessKey ID
        String accessKeyID = "";
        //AccessKey Secret
        String accessKeySecret = "";

        //OSS客户端对象
        OSS ossClient = new OSSClientBuilder().build(endPoint,accessKeyID,accessKeySecret);
        ossClient.putObject(bucketName,fileName,file.getInputStream());
        ossClient.shutdown();
        return ALI_DOMAIN+'/'+fileName;
    }
    public static String getNewFileName(MultipartFile file){
        String originalFilename = file.getOriginalFilename(); //原文件名和拓展名
        String ext = FilenameUtils.getExtension(originalFilename);  //拓展名
        String filename = UUID.randomUUID().toString();  //生成的文件名
        filename = filename +'.'+ ext;
        return filename;
    }



}
