package cn.dmego.odsp.system.service.impl;

import cn.dmego.odsp.common.exception.BusinessException;
import cn.dmego.odsp.system.dao.PermissionMapper;
import cn.dmego.odsp.system.dao.RolePermissionMapper;
import cn.dmego.odsp.system.model.Permission;
import cn.dmego.odsp.system.model.RolePermission;
import cn.dmego.odsp.system.service.PermissionService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * class_name: PermissionServiceImpl
 * package: cn.dmego.odsp.system.service.impl
 * describe: 权限 Service 实现类
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:47
 **/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Permission> getByUserId(Integer userId) {
        return permissionMapper.listByRoleId(userId);
    }

    @Override
    public List<Permission> listByRoleId(Integer roleId) {
        return permissionMapper.listByRoleId(roleId);
    }

    @Override
    public List<Permission> listByUserId(Integer loginUserId) {
        return permissionMapper.listByUserId(loginUserId);
    }

    @Override
    public List<Permission> list() {
        return permissionMapper.selectList(new EntityWrapper<Permission>().orderBy("order_number",true));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updRoleAuth(Integer roleId, List<Integer> authIds) {
        rolePermissionMapper.delete(new EntityWrapper<RolePermission>().eq("role_id",roleId));
        if(authIds != null && authIds.size() > 0){
            if( rolePermissionMapper.insertRoleAuths(roleId, authIds) < authIds.size()){
                throw new BusinessException("更新角色权限失败");
            }
        }
        return true;
    }

    @Override
    public List<Permission> listMenu() {
        return permissionMapper.selectList(new EntityWrapper<Permission>().eq("is_menu",0).orderBy("order_number",true));
    }

    @Override
    public boolean add(Permission permission) {
        permission.setCreateDate(new Date());
        return permissionMapper.insert(permission) > 0;
    }

    @Override
    public boolean update(Permission permission) {
        return permissionMapper.updateById(permission) > 0;
    }

    @Override
    public boolean delete(Integer permissionId) {
        //先查出该权限下的子节点
        List<Permission> childs = permissionMapper.selectList(new EntityWrapper<Permission>().eq("parent_id", permissionId));
        if(childs != null && childs.size() > 0){
            throw new BusinessException("请先删除子节点");
        }
        //删除该角色权限表中所有该权限的字段
        rolePermissionMapper.delete(new EntityWrapper<RolePermission>().eq("Permission_id",permissionId));
        if(permissionMapper.deleteById(permissionId) <= 0){
            throw new BusinessException("删除失败,请重试");
        }
        return true;
    }
}
