package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.MessageDAO;
import com.xiaoxu.xBlog.Entities.Message;
import com.xiaoxu.xBlog.Service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageDAO, Message> implements MessageService {
}
