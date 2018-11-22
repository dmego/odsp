package cn.dmego.odsp.system.controller;

import cn.dmego.odsp.common.BaseController;
import cn.dmego.odsp.common.JsonResult;
import cn.dmego.odsp.common.PageResult;
import cn.dmego.odsp.common.utils.JSONUtil;
import cn.dmego.odsp.common.utils.StringUtil;
import cn.dmego.odsp.system.model.Permission;
import cn.dmego.odsp.system.model.Role;
import cn.dmego.odsp.system.service.PermissionService;
import cn.dmego.odsp.system.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * class_name: RoleController
 * package: cn.dmego.odsp.system.controller
 * describe: 角色管理 Controller
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:37
 **/
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping()
    public String role(){
        return "system/role.html";
    }

    /**
     * 跳转到为角色分配权限页面
     * @param roleId
     * @param model
     * @return
     */
    @RequestMapping("/auth")
    public String roleAuth(String roleId, Model model){
        model.addAttribute("roleId",roleId);
        return "system/roleAuth.html";
    }

    /**
     * 跳转到添加/修改角色页面
     * @param model
     * @return
     */
    @RequestMapping("/editForm")
    public String addRole(Model model){
        return "system/roleForm.html";
    }

    /**
     * 查询出所有角色
     * @param keyword
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<Role> list(String keyword){
        List<Role> list = roleService.list(false);
        //如果搜索的关键词不为空.则通过迭代将不包含关键词的角色去除掉
        if(StringUtil.isNotBlank(keyword)){
            keyword = keyword.trim();
            Iterator<Role> iterator = list.iterator();
            while (iterator.hasNext()){
                Role next = iterator.next();
                //角色ID,角色名称,角色描述中只要有一个不包含该关键词,则将该角色从集合在中去除
                boolean b = String.valueOf(next.getRoleId()).contains(keyword) || next.getRoleName().contains(keyword) || next.getDescribes().contains(keyword);
                if(b){
                    iterator.remove();
                }
            }
        }
        return new PageResult<>(list);
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(Role role){
        if(roleService.add(role)){
            return JsonResult.ok("添加角色成功");
        }else {
            return JsonResult.error("添加角色失败");
        }
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Role role){
        if(roleService.update(role)){
            return JsonResult.ok("修改角色成功");
        }else {
            return JsonResult.error("修改角色失败");
        }
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(Integer roleId){
        if(roleService.updateStatus(roleId,1)){
            return JsonResult.ok("删除角色成功");
        }
        return JsonResult.ok("删除角色失败");
    }

    /**
     * 角色权限树
     * @param roleId
     * @return
     */
    @ResponseBody
    @GetMapping("/authTree")
    public List<Map<String,Object>> authTree(Integer roleId){
        List<Permission> roleAuths = permissionService.listByRoleId(roleId);
        List<Permission> allAuths = permissionService.list();
        List<Map<String,Object>> authTrees = new ArrayList<>();
        for (Permission one:allAuths) {
            Map<String,Object> authtree = new HashMap<>();
            authtree.put("id",one.getPermissionId());
            authtree.put("name",one.getPermissionName());
            authtree.put("pId",one.getParentId()); //父Id
            authtree.put("open",true); //是否打开
            authtree.put("checked",false); //是否选中
            for (Permission temp:roleAuths) {
                //遍历该角色所以的权限,如果有这个权限,则在权限树中选中
                if(temp.getPermissionId().equals(one.getPermissionId())){
                    authtree.put("checked",true);
                }
            }
            authTrees.add(authtree);
        }
        return authTrees;
    }

    /**
     * 修改角色权限
     * @return
     */
    @RequiresPermissions("role:edit")
    @ResponseBody
    @PostMapping("/updRoleAuth")
    public JsonResult updRoleAuth(Integer roleId, String authIds){
        if(permissionService.updRoleAuth(roleId, JSONUtil.parseArray(authIds,Integer.class))){
            return JsonResult.ok("修改角色权限成功");
        }else {
            return JsonResult.error("修改角色权限失败");
        }
    }

}
