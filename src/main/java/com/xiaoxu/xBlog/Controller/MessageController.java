package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxu.xBlog.Entities.Message;
import com.xiaoxu.xBlog.Service.MessageService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/pageShow")
    public ReturnResults showPageMessages(Integer currentPage,Integer pageSize){
        Page<Message> pageInfo = null;
        LambdaQueryWrapper<Message> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(Message::getMessageDate);
        if (currentPage == null &&pageSize == null) {
            pageInfo = new Page<>();
        }else {
            pageInfo = new Page<>(currentPage, pageSize);
        }
        messageService.page(pageInfo,qw);
        return ReturnResults.success(pageInfo);
    }

    @PostMapping("/insert")
    @Transactional
    public ReturnResults insertMessage(@RequestBody Message msg){
        boolean b = messageService.save(msg);
        return b?ReturnResults.success("成功"):ReturnResults.error("失败");
    }

    @GetMapping("/updateMessageReply")
    @Transactional
    public ReturnResults updateMessageReply(Message message){
        LambdaQueryWrapper<Message> qw = new LambdaQueryWrapper<>();
        qw.eq(Message::getMessageId,message.getMessageId());
        messageService.update(message,qw);
        return ReturnResults.success("更新成功");
    }

    @DeleteMapping("/delete")
    public ReturnResults delete(Integer messageId){
        LambdaQueryWrapper<Message> qw = new LambdaQueryWrapper<>();
        qw.eq(Message::getMessageId,messageId);
        messageService.remove(qw);
        return ReturnResults.success("删除成功");
    }
}
