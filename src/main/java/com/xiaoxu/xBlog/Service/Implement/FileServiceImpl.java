package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.FileDAO;
import com.xiaoxu.xBlog.Entities.FileDTO;
import com.xiaoxu.xBlog.Service.FileService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl extends ServiceImpl<FileDAO,FileDTO> implements FileService {

    //检查文件
    public List<FileDTO> check(String key){
        LambdaQueryWrapper<FileDTO> qw = new LambdaQueryWrapper<>();
        qw.eq(FileDTO::getFileKey,key);
        List<FileDTO> dtos = this.list(qw);
        return dtos;
    }

    @Override
    public String mergeFileData(String key) {
        LambdaQueryWrapper<FileDTO> qw = new LambdaQueryWrapper<>();
        qw.eq(FileDTO::getFileKey,key);
        List<FileDTO> fileDTOList = this.list(qw);
        List<Integer> idList = fileDTOList.stream().map(item->item.getId()).collect(Collectors.toList());
        this.removeByIds(idList);
        //合并数据文件
        FileDTO fileDTO = fileDTOList.get(fileDTOList.size() - 1);
        fileDTO.setName(fileDTO.getFileKey());
        String path = fileDTO.getPath();
        path = path.substring(path.lastIndexOf("\\")+1,path.lastIndexOf("."));
        fileDTO.setPath(path);
        fileDTO.setUpdatedAt(LocalDateTime.now());
        fileDTO.setShardSize(fileDTO.getSize());
        fileDTO.setShardTotal(1l);
        fileDTO.setShardIndex(1l);
        this.save(fileDTO);
        return path;
    }
}
