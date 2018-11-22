package cn.dmego.odsp.common;

import cn.dmego.odsp.system.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * class_name: BaseController
 * package: cn.dmego.odsp.common
 * describe: Controller 基类
 * creat_user: Dmego
 * creat_date: 2018/10/21
 * creat_time: 22:47
 **/
public class BaseController {

    /**
     * 获取当前登陆的用户
     * @return
     */
    public User getLoginUser(){
        Subject subject = SecurityUtils.getSubject();
        if(subject != null && subject.getPrincipal() != null){
            return (User) subject.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前用户登陆的ID
     */
    public Integer getLoginUserId(){
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserId();
    }

    /**
     * 获取当前用户登陆的用户名
     * @return
     */
    public String getLoginUserName(){
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserName();
    }

}
