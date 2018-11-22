package cn.dmego.odsp.system.service.impl;

import cn.dmego.odsp.common.exception.ParameterException;
import cn.dmego.odsp.system.dao.RoleMapper;
import cn.dmego.odsp.system.dao.RolePermissionMapper;
import cn.dmego.odsp.system.model.Role;
import cn.dmego.odsp.system.service.RoleService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * class_name: RoleServiceImpl
 * package: cn.dmego.odsp.system.service.impl
 * describe: 角色 Service 实现类
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:46
 **/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Role> getByUserId(Integer userId) {
        return roleMapper.selectByUserId(userId);
    }

    @Override
    public List<Role> list(boolean showDelete) {
        Wrapper wrapper = new EntityWrapper();
        if(!showDelete){
            wrapper.eq("status",0);
        }
        return roleMapper.selectList(wrapper.orderBy("create_date",true));
    }

    @Override
    public boolean add(Role role) {
        role.setCreateDate(new Date());
        return roleMapper.insert(role) > 0;
    }

    @Override
    public boolean update(Role role) {
        return baseMapper.updateById(role) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateStatus(Integer roleId, int status) {
        if(status != 0 && status != 1){
            throw  new ParameterException("status 值为 0 或 1");
        }
        Role role = new Role();
        role.setRoleId(roleId);
        role.setStatus(status);
        boolean b = baseMapper.updateById(role) > 0;
        if(b){
           //删除角色的权限
            rolePermissionMapper.delete(new EntityWrapper().eq("role_id",roleId));
        }
        return b;
    }
}
