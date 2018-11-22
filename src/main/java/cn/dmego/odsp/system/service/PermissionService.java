package cn.dmego.odsp.system.service;

import cn.dmego.odsp.system.model.Permission;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * class_name: PermissionService
 * package: cn.dmego.odsp.system.service
 * describe: 权限 Service 接口类
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:43
 **/
public interface PermissionService extends IService<Permission> {

    List<Permission> getByUserId(Integer userId);

    List<Permission> listByRoleId(Integer roleId);

    List<Permission> list();

    boolean updRoleAuth(Integer roleId, List<Integer> parseArray);

    List<Permission> listMenu();

    boolean add(Permission permission);

    boolean update(Permission permission);

    boolean delete(Integer permissionId);

    List<Permission> listByUserId(Integer loginUserId);

}
