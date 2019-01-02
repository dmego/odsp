package cn.dmego.odsp.system.controller;

import cn.dmego.odsp.common.BaseController;
import cn.dmego.odsp.common.JsonResult;
import cn.dmego.odsp.common.shiro.EndecryptUtil;
import cn.dmego.odsp.common.utils.StringUtil;
import cn.dmego.odsp.common.utils.UserAgentGetter;
import cn.dmego.odsp.system.model.Log;
import cn.dmego.odsp.system.model.Permission;
import cn.dmego.odsp.system.model.User;
import cn.dmego.odsp.system.service.LogService;
import cn.dmego.odsp.system.service.PermissionService;
import cn.dmego.odsp.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class_name: MainController
 * package: cn.dmego.odsp.system.controller
 * describe: 主 Controller
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:38
 **/
@Controller
public class MainController extends BaseController implements ErrorController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 登陆页
     * @return
     */
    @GetMapping("/login")
    public String login(){
        //如果已经登陆,则请求重定向到首页
        if(getLoginUser() != null){
            return "redirect:index";
        }
        return "login.html";
    }

    /**
     * 登录处理
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public JsonResult doLogin(String username, String password, HttpServletRequest request) {
        //如果账号密码有空值
        if (StringUtil.isBlank(username, password)) {
            return JsonResult.error("账号密码不能为空");
        }

//        User user = userService.getByUserName(username);
//        String encrytMd5 = EndecryptUtil.encrytMd5(password, username, 3);
//        if (user != null && user.getPassword().equals(encrytMd5)) {
//            return JsonResult.ok("登录成功");
//        }

        try{
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);
            SecurityUtils.getSubject().login(token);
            addLog(getLoginUserId(),request);
            return JsonResult.ok("登录成功");
        }catch (IncorrectCredentialsException ice){
            return JsonResult.error("密码错误");
        }catch (UnknownAccountException uce){
            return JsonResult.error("账号不存在");
        }catch (LockedAccountException lae){
            return JsonResult.error("账号已被锁");
        }catch (ExcessiveAttemptsException eae){
            return JsonResult.error("频繁操作,请稍后重试");
        }
//        return JsonResult.error("登录失败");
    }

    /**
     * 主页
     * @param model
     * @return
     */
    @RequestMapping({"/","/index"})
    public String index(Model model){
        List<Permission> permissions = permissionService.listByUserId(getLoginUserId());
        List<Map<String, Object>> menuTree = getMenuTree(permissions, -1);
        model.addAttribute("menus",menuTree);
        model.addAttribute("loginUser",getLoginUser());
        return "index.html";
    }


    /**
     *递归转化树形菜单
     * @param permissions
     * @param parentId
     * @return
     */
    private List<Map<String,Object>> getMenuTree(List<Permission> permissions, Integer parentId){
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i < permissions.size(); i++) {
            Permission permission = permissions.get(i);
            if(permission.getIsMenu() == 0 && parentId == permission.getParentId()){
                Map<String,Object> map = new HashMap<>();
                map.put("mark",permission.getMark());
                map.put("menuName",permission.getPermissionName());
                map.put("menuIcon",permission.getMenuIcon());
                map.put("menuUrl",permission.getMenuUrl());
                map.put("subMenus",getMenuTree(permissions,permissions.get(i).getPermissionId()));
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 添加登录日志
     * @param userId
     * @param request
     */
    private void addLog(Integer userId,HttpServletRequest request){
        UserAgentGetter agentGetter = new UserAgentGetter(request);
        //添加到登录日志
        Log log = new Log();
        log.setUserId(userId);
        log.setOsName(agentGetter.getOS());
        log.setDevice(agentGetter.getDevice());
        log.setBrowserType(agentGetter.getBrowser());
        log.setIpAddress(agentGetter.getIpAddr());
        logService.add(log);
    }


    /**
     * 欢迎页
     * @return
     */
    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome.html";
    }

    /**
     * 控制台2
     * @return
     */
    @RequestMapping("/console1")
    public String console1(){
        return "console1.html";
    }

    /**
     * 控制台
     * @return
     */
    @RequestMapping("/console")
    public String console(){
        return "console.html";
    }

    /**
     * 消息中心
     * @return
     */
    @RequestMapping("/tpl/message")
    public String message(){
        return "tpl/message.html";
    }

    /**
     * 修改密码弹窗
     * @return
     */
    @RequestMapping("/tpl/password")
    public String password(){
        return "tpl/password.html";
    }

    /**
     * 主题设置弹窗
     * @return
     */
    @RequestMapping("/tpl/theme")
    public String theme(){
        return "tpl/theme.html";
    }

    /**
     * 错误页
     * @return
     */
    @RequestMapping("/error")
    public String error(String code){
        if("403".equals(code)){
            return "error/403.html";
        }
        return "error/404.html";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
