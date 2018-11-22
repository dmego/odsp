package cn.dmego.odsp.common.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * class_name: MyExceptionHandler
 * package: cn.dmego.odsp.common.exception
 * describe: 全局异常处理器
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 17:13
 **/
public class MyExceptionHandler {
    private Logger logger = LoggerFactory.getLogger("MyExceptionHandler");

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Map<String, Object> errorHandler(Exception ex){
        Map<String,Object> map = new HashMap<>();
        //根据不同错误获取错误信息
        if(ex instanceof IException){
            map.put("code",((IException) ex).getCode());
            map.put("msg",ex.getMessage());
        }else if(ex instanceof UnauthorizedException){
            map.put("code",403);
            map.put("msg","没有权限访问");
        }else{
            String message = ex.getMessage();
            map.put("code",500);
            map.put("msg","系统繁忙");
            // TODO: 2018/10/23: 这是测试阶段的错误处理方式

        }

        return map;
    }
}
