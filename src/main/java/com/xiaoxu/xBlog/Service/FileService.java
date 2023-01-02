package com.xiaoxu.xBlog.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxu.xBlog.Entities.FileDTO;

import java.util.List;

public interface FileService extends IService<FileDTO> {
    List<FileDTO> check(String key);

    String mergeFileData(String key);
}
