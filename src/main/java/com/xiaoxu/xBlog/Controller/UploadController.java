package com.xiaoxu.xBlog.Controller;

import com.xiaoxu.xBlog.Utils.ReturnResults;
import com.xiaoxu.xBlog.Utils.UploadUtils;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

@RestController
public class UploadController {


    @PostMapping("/upload")
    public ReturnResults<String> upload(MultipartFile file) throws IOException {
        String fileUrl = UploadUtils.uploadImage(file);
        return ReturnResults.success(fileUrl);
    }

//    @PostMapping("/fileUpload")
//    public ReturnResults<String> fileUpload(HttpServletRequest request,HttpServletResponse response) throws IOException {
//        response.setCharacterEncoding("UTF-8");
//        Integer schunk = null;
//        Integer schunks = null;
//        String name = null;
//        BufferedOutputStream os = null;
//        try{
//            DiskFileItemFactory factory = new DiskFileItemFactory();
//            factory.setSizeThreshold(1024);
//            factory.setRepository(new File(basePath));
//            ServletFileUpload upload = new ServletFileUpload(factory);
//            upload.setFileSizeMax(10l*1024l*1024l);
//            upload.setSizeMax(10l*1024l*1024l*1024l);
//            List<FileItem> fileItems = upload.parseRequest(new ServletRequestContext(request));
//            for (FileItem file : fileItems){
//                if(file.isFormField()){
//                    if("chunk".equals(file.getFieldName())){
//                        schunk = Integer.parseInt(file.getString("utf8"));
//                    }
//                    if("chunks".equals(file.getFieldName())){
//                        schunks = Integer.parseInt(file.getString("utf8"));
//                    }
//                    if("name".equals(file.getFieldName())){
//                        name = file.getString("utf8");
//                    }
//                }
//            }
//
//            for (FileItem file : fileItems) {
//                if(!file.isFormField()){
//                    String tempFileName = name;
//                    if (name != null) {
//                        if(schunk != null){
//                            tempFileName = schunk +"_"+name;
//                        }
//                        File tempFile = new File(basePath,tempFileName);
//                        if(!tempFile.exists()){
//                            file.write(tempFile);
//                        }
//                    }
//                }
//            }
//            if (schunk != null && schunk.intValue() == schunks.intValue()-1) {
//                File file = new File(basePath,name);
//                os = new BufferedOutputStream(new FileOutputStream(file));
//
//                for (int i=0 ;i<schunks;i++){
//                    File tempFile = new File(basePath,i+"_"+name);
//                    while(!tempFile.exists()){
//                        Thread.sleep(100);
//                    }
//                    byte[] bytes = FileUtils.readFileToByteArray(tempFile);
//                    os.write(bytes);
//                    os.flush();
//                    tempFile.delete();
//                }
//                os.flush();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                if (os != null) {
//                    os.close();
//                }
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//        return ReturnResults.success("成功");
//    }

//    @GetMapping("/download/{name}")
//    public void download(@PathVariable String name, HttpServletResponse response) throws IOException {
//        FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
//        ServletOutputStream outputStream = response.getOutputStream();
//        int len = 0;
//        byte[] bytes = new byte[1024];
//        while ((len = fileInputStream.read(bytes)) != -1) {
//            outputStream.write(bytes, 0, len);
//            outputStream.flush();
//        }
//        outputStream.close();
//        fileInputStream.close();
//    }
}
