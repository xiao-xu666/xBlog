package com.xiaoxu.xBlog.Controller;

import com.xiaoxu.xBlog.Entities.FileDTO;
import com.xiaoxu.xBlog.Service.FileService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import com.xiaoxu.xBlog.Utils.UploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@Slf4j
public class UploadController {

    @Value("D:\\桌面\\java\\IntelliJ IDEA\\xBlog\\fileUpload\\")
    private String basePath;

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ReturnResults<String> upload(MultipartFile file) throws IOException {
        String fileUrl = UploadUtils.uploadImage(file);
        return ReturnResults.success(fileUrl);
    }

    @PostMapping("/fileUpload")
    public ReturnResults<String> fileUpload(MultipartFile file,
                                            String suffix,
                                            Long shardIndex,
                                            Long shardSize,
                                            Long shardTotal,
                                            Long size,
                                            String key) throws IOException, InterruptedException {

        log.info("上传文件开始");
        //文件的名称
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        //设置图片新的名字
        String fileName = new StringBuffer().append(key).append(".").append(suffix).toString();
        //这个是分片的名字
        String localfileName = new StringBuffer(fileName).append(".").append(shardIndex).toString();
        // 以绝对路径保存重名命后的图片
        File targeFile=new File(basePath,localfileName);
        //上传这个图片
        file.transferTo(targeFile);
        //数据库持久化这个数据
        FileDTO file1=new FileDTO();
        file1.setPath(basePath+localfileName);
        file1.setSuffix(suffix);
        file1.setName(name);
        file1.setSize(size);
        file1.setCreatedAt(LocalDateTime.now());
        file1.setUpdatedAt(LocalDateTime.now());
        file1.setShardIndex(shardIndex);
        file1.setShardSize(shardSize);
        file1.setShardTotal(shardTotal);
        file1.setFileKey(key);
        //插入到数据库中
        //保存的时候 去处理一下 这个逻辑
        fileService.saveOrUpdate(file1);
        //判断当前是不是最后一个分页 如果不是就继续等待其他分页  合并分页
        if(shardIndex .equals(shardTotal) ){
            file1.setPath(basePath+fileName);
            this.merge(file1);
            String path = fileService.mergeFileData(key);
            return ReturnResults.success(path);
        }
            return ReturnResults.success("上传分片成功!");
        }


    /**
     * @author fengxinglie
     * 合并分页
     */
    private void merge(FileDTO fileDTO) throws IOException, InterruptedException {
        //合并分片开始
        log.info("分片合并开始");
        String path = fileDTO.getPath(); //获取到的路径 没有.1 .2 这样的东西
        //截取视频所在的路径
        path = path.replace(basePath,"");
        Long shardTotal= fileDTO.getShardTotal();
        File newFile = new File(basePath + path);
        FileOutputStream outputStream = new FileOutputStream(newFile,true); // 文件追加写入
        FileInputStream fileInputStream = null; //分片文件
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;
        try {
            for (int i = 0; i < shardTotal; i++) {
                // 读取第i个分片
                fileInputStream = new FileInputStream(new File(basePath + path + "." + (i + 1)));
                while ((len = fileInputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);
                }
            }
        } catch (IOException e) {
            log.error("分片合并异常", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.close();
                log.info("IO流关闭");
            } catch (Exception e) {
                log.error("IO流关闭", e);
            }
        }
        log.info("分片结束了");
        //告诉java虚拟机去回收垃圾 至于什么时候回收  这个取决于 虚拟机的决定
        System.gc();
        //等待100毫秒 等待垃圾回收去 回收完垃圾
        Thread.sleep(100);
        log.info("删除分片开始");
        for (int i = 0; i < shardTotal; i++) {
            String filePath = basePath + path + "." + (i + 1);
            File file = new File(filePath);
            boolean result = file.delete();
            log.info("删除{}，{}", filePath, result ? "成功" : "失败");
        }
        log.info("删除分片结束");
    }

    @GetMapping("/check")
    public ReturnResults check(String key){
        List<FileDTO> check = fileService.check(key);
        //如果这个key存在的话 那么就获取上一个分片去继续上传
        if(check.size()!=0){
            FileDTO fileDTO = check.get(check.size() - 1);
            if(fileDTO.getShardIndex() == fileDTO.getShardTotal()){
                return ReturnResults.success("path_"+fileDTO.getPath());
            }else{
                return ReturnResults.success("index_"+fileDTO.getShardIndex());
            }
        }
        return ReturnResults.error("查询失败,可以添加");
    }



    @GetMapping("/download/{name}")
    public void download(@PathVariable String name, HttpServletResponse response) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
        ServletOutputStream outputStream = response.getOutputStream();
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
            outputStream.flush();
        }
        outputStream.close();
        fileInputStream.close();
    }
}
