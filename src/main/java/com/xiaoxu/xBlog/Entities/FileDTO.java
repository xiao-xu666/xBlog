package com.xiaoxu.xBlog.Entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName(value = "file")
public class FileDTO {
    /**
    * id
    */
    @TableId
    private Integer id;

    /**
    * 相对路径
    */
    private String path;

    /**
    * 文件名
    */
    private String name;

    /**
    * 后缀
    */
    private String suffix;

    /**
    * 大小|字节B
    */
    private Long size;


    /**
    * 创建时间
    */
    private LocalDateTime createdAt;

    /**
    * 修改时间
    */
    private LocalDateTime updatedAt;

    /**
    * 已上传分片
    */
    private Long shardIndex;

    /**
    * 分片大小|B
    */
    private Long shardSize;

    /**
    * 分片总数
    */
    private Long shardTotal;

    /**
    * 文件标识
    */
    private String fileKey;

}