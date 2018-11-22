package cn.dmego.odsp.system.controller;

import cn.dmego.odsp.common.BaseController;
import cn.dmego.odsp.common.JsonResult;
import cn.dmego.odsp.common.PageResult;
import cn.dmego.odsp.common.exception.ParameterException;
import cn.dmego.odsp.common.shiro.EndecryptUtil;
import cn.dmego.odsp.common.utils.StringUtil;
import cn.dmego.odsp.system.model.Role;
import cn.dmego.odsp.system.model.User;
import cn.dmego.odsp.system.service.RoleService;
import cn.dmego.odsp.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * class_name: UserController
 * package: cn.dmego.odsp.system.controller
 * describe: 用户管理 Controller
 * creat_user: Dmego
 * creat_date: 2018/10/21
 * creat_time: 22:45
 **/
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 获取角色集合
     * @param roleStr
     * @return
     */
    private List<Role> getRoles(String roleStr){
        List<Role> roles = new ArrayList<>();
        String[] split = roleStr.split(",");
        for (String t:split) {
            if(t.equals("1")){
                throw new ParameterException("不能添加超级管理员");
            }
            roles.add(new Role(Integer.parseInt(t)));
        }
        return roles;
    }

    @RequestMapping
    public String user(Model model){
        List<Role> roles = roleService.list(false);
        model.addAttribute("roles",roles);
        return "system/user.html";
    }

    /**
     * 跳转到修改用户的页面,带上所有角色信息
     * @param model
     * @return
     */
    @RequestMapping("/editForm")
    public String addUser(Model model){
        List<Role> roles = roleService.list(false);
        model.addAttribute("roles",roles);
        return "system/userForm.html";
    }

    /**
     * 用户列表
     * @param page
     * @param limit
     * @param serachKey
     * @param searchValue
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<User> list(Integer page,Integer limit, String serachKey,String searchValue){
        //如果是第一次调用,初始化 page = 0, limit = 0
        if(page == null){
            page = 0;
            limit = 0;
        }
        //如果 查询的值为 null,则将查询 key 也设置为 null
        if(StringUtil.isBlank(searchValue)){
            serachKey = null;
        }
        return userService.list(page,limit,true,serachKey,searchValue);
    }

    /**
     * 添加用户,并为用户分配角色
     * @param user
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(User user,String roleId){
        user.setRoles(getRoles(roleId));
        user.setPassword("123456");
        if(userService.add(user)){
            return JsonResult.ok("添加成功");
        }else {
            return JsonResult.error("添加失败");
        }
    }

    /**
     * 修改用户,并修改用户的角色
     * @param user
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(User user,String roleId){
        user.setRoles(getRoles(roleId));
        if(userService.update(user)){
            return JsonResult.ok("修改成功");
        }else {
            return JsonResult.error("修改失败");
        }
    }

    /**
     * 修改自己的密码
     * @param oldPsw
     * @param newPsw
     * @return
     */
    @ResponseBody
    @RequestMapping("/updPsw")
    public JsonResult updPsw(String oldPsw,String newPsw){
        //旧密码的hash值
        String finalSecret = EndecryptUtil.encrytMd5(oldPsw, getLoginUserName(), 3);
        if(!finalSecret.equals(getLoginUser().getPassword())){
            return JsonResult.error("原密码输入不正确");
        }
        //更新密码
        if(userService.updPsw(getLoginUserId(),getLoginUserName(),newPsw)){
            return JsonResult.ok("修改密码成功");
        }else {
            return JsonResult.error("修改密码失败");
        }
    }

    /**
     * 修改用户状态
     * @param userId
     * @param state
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateState")
    public JsonResult updateState(Integer userId,Integer state){
        if(userService.updateState(userId,state)){
            return JsonResult.ok();
        }else{
            return JsonResult.error();
        }
    }

    /**
     * 重置用户的密码为 123456
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping("/resetPsw")
    public JsonResult resetPsw(Integer userId){
       User user = userService.getById(userId);
       if(userService.updPsw(userId,user.getUserName(),"123456")){
           return JsonResult.ok("重置密码成功");
       }else {
           return JsonResult.error("重置密码失败");
       }
    }
}
