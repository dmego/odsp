package cn.dmego.odsp.system.service;

import cn.dmego.odsp.system.model.Role;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * class_name: RoleService
 * package: cn.dmego.odsp.system.service
 * describe:  权限 Service 接口类
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:41
 **/
public interface RoleService extends IService<Role> {

    List<Role> getByUserId(Integer userId);

    List<Role> list(boolean showDelete);

    boolean add(Role role);

    boolean update(Role role);

    boolean updateStatus(Integer roleId, int status); //逻辑删除角色
}

