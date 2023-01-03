package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxu.xBlog.Entities.Message;
import com.xiaoxu.xBlog.Service.MessageService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/pageShow")
    public ReturnResults showPageMessages(Integer currentPage, Integer pageSize) {
        Page<Message> pageInfo = null;
        pageInfo = (Page<Message>) redisTemplate.opsForValue().get("message_pageShow_" + currentPage + "_" + pageSize);
        if (pageInfo != null) {
            return ReturnResults.success(pageInfo);
        }
        LambdaQueryWrapper<Message> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(Message::getMessageDate);
        if (currentPage == null && pageSize == null) {
            pageInfo = new Page<>();
        } else {
            pageInfo = new Page<>(currentPage, pageSize);
        }
        messageService.page(pageInfo, qw);
        redisTemplate.opsForValue().set("message_pageShow_" + currentPage + "_" + pageSize, pageInfo, 5, TimeUnit.HOURS);
        return ReturnResults.success(pageInfo);
    }

    @PostMapping("/insert")
    @Transactional
    public ReturnResults insertMessage(@RequestBody Message msg) {
        boolean b = messageService.save(msg);
        if (b) {
            Set<String> keys = redisTemplate.keys("message_" + "*");
            redisTemplate.delete(keys);
        }
        return b ? ReturnResults.success("成功") : ReturnResults.error("失败");
    }

    @GetMapping("/updateMessageReply")
    @Transactional
    public ReturnResults updateMessageReply(Message message) {
        LambdaQueryWrapper<Message> qw = new LambdaQueryWrapper<>();
        qw.eq(Message::getMessageId, message.getMessageId());
        boolean b = messageService.update(message, qw);
        if (b) {
            Set<String> keys = redisTemplate.keys("message_" + "*");
            redisTemplate.delete(keys);
        }
        return b ? ReturnResults.success("成功") : ReturnResults.error("失败");

    }

    @DeleteMapping("/delete")
    public ReturnResults delete(Integer messageId) {
        LambdaQueryWrapper<Message> qw = new LambdaQueryWrapper<>();
        qw.eq(Message::getMessageId, messageId);
        boolean b = messageService.remove(qw);
        if (b) {
            Set<String> keys = redisTemplate.keys("message_" + "*");
            redisTemplate.delete(keys);
        }
        return b ? ReturnResults.success("成功") : ReturnResults.error("失败");
    }
}
