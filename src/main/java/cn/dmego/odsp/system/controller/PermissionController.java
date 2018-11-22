package cn.dmego.odsp.system.controller;

import cn.dmego.odsp.common.BaseController;
import cn.dmego.odsp.common.JsonResult;
import cn.dmego.odsp.common.PageResult;
import cn.dmego.odsp.common.utils.ReflectUtil;
import cn.dmego.odsp.system.model.Permission;
import cn.dmego.odsp.system.model.Role;
import cn.dmego.odsp.system.service.PermissionService;
import cn.dmego.odsp.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * class_name: PermissionController
 * package: cn.dmego.odsp.system.controller
 * describe: 权限管理 Controller
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:37
 **/
@Controller
@RequestMapping("/system/permission")
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    /**
     * 跳转到权限首页
     * @param model
     * @return
     */
    @RequestMapping()
    public String permission(Model model){
        List<Role> roles = roleService.list(false);
        model.addAttribute("roles",roles);
        return "system/permission.html";
    }

    /**
     * 跳转到修改/增加权限页面
     * @param model
     * @return
     */
    @RequestMapping("/editForm")
    public String editFrom(Model model){
        //查询出所有菜单页
        List<Permission> permissions = permissionService.listMenu();
        model.addAttribute("permissions",permissions);
        return "system/permissionForm.html";
    }

    /**
     * 查询出所有的权限
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageResult<Map<String,Object>> list(Integer roleId){
        List<Map<String,Object>> maps = new ArrayList<>();
        List<Permission> allAuths = permissionService.list();
        List<Permission> roleAuths = permissionService.listByRoleId(roleId);
        for (Permission one: allAuths) {
            Map<String,Object> map = ReflectUtil.objectToMap(one);
            map.put("checked",0);
            for (Permission roleAuth: roleAuths) {
                if(one.getPermissionId().equals(roleAuth.getPermissionId())){
                    map.put("checked",1);
                    break;
                }
            }
            maps.add(map);
        }
        return new PageResult<>(maps);
    }

    /**
     * 添加权限
     * @param permission
     * @return
     */
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(Permission permission){
        if(permissionService.add(permission)){
            return JsonResult.ok("添加成功");
        }else {
            return JsonResult.error("添加失败");
        }
    }

    /**
     * 修改权限
     * @param permission
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Permission permission){
        if(permissionService.update(permission)){
            return JsonResult.ok("修改权限成功");
        }else {
            return JsonResult.error("修改权限失败");
        }
    }

    /**
     * 删除权限
     * @param permissionId
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(Integer permissionId){
        if(permissionService.delete(permissionId)){
            return JsonResult.ok("删除权限成功");
        }else {
            return JsonResult.error("删除权限失败");
        }
    }




}
