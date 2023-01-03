package com.xiaoxu.xBlog.Utils;


import com.xiaoxu.xBlog.Exception.CustomerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class} )
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ReturnResults<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //如果异常信息带有Duplicate entry字样，则被认为用户名重复
        if (ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2]+"已被抢走了-_- ,请更换用户名!";
            return ReturnResults.error(msg);
        }
        return ReturnResults.error("未知错误!");
    }

    @ExceptionHandler(Exception.class)
    public ReturnResults<String> exceptionHandler(Exception ex){
        if (ex.getClass() == CustomerException.class){
            return ReturnResults.error("操作异常，请正规操作");
        }else {
            return ReturnResults.error(ex.getMessage());
        }
    }
}
