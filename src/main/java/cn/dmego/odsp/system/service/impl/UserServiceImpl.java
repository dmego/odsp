package cn.dmego.odsp.system.service.impl;

import cn.dmego.odsp.common.PageResult;
import cn.dmego.odsp.common.exception.BusinessException;
import cn.dmego.odsp.common.exception.ParameterException;
import cn.dmego.odsp.common.shiro.EndecryptUtil;
import cn.dmego.odsp.common.utils.StringUtil;
import cn.dmego.odsp.system.dao.UserMapper;
import cn.dmego.odsp.system.dao.UserRoleMapper;
import cn.dmego.odsp.system.model.Role;
import cn.dmego.odsp.system.model.User;
import cn.dmego.odsp.system.model.UserRole;
import cn.dmego.odsp.system.service.UserService;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class_name: UserServiceImpl
 * package: cn.dmego.odsp.system.service.impl
 * describe: 用户 Service 实现类
 * creat_user: Dmego
 * creat_date: 2018/10/21
 * creat_time: 22:42
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 获取角色的ID列表
     * @param roles
     * @return
     */
    private List<Integer> getRoleIds(List<Role> roles){
        List<Integer> rs = new ArrayList<>();
        if(roles != null && roles.size() >0 ){
            for (Role role: roles) {
                rs.add(role.getRoleId());
            }
        }
        return rs;
    }

    /**
     * 获取用户的ID列表
     * @param users
     * @return
     */
    private List<Integer> getUserIds(List<User> users){
        List<Integer> us = new ArrayList<>();
        if(users != null && users.size() >0 ){
            for (User user: users) {
                us.add(user.getUserId());
            }
        }
        return us;
    }

    @Override
    public User getByUserName(String username) {
        return baseMapper.selectByUserName(username);
    }

    @Override
    public PageResult<User> list(Integer pageNum, Integer pageSize,boolean showDelete, String column, String value) {
        Wrapper<User> wrapper = new EntityWrapper<>();
        //如果 column 不为空,添加一个  like 查询条件
        if(StringUtil.isNotBlank(column)){
            wrapper.like(column,value);
        }
        //如果不显示被锁定的用户
        if(!showDelete){
            wrapper.eq("state",0);
        }

        Page<User> userPage = new Page<>(pageNum, pageSize);
        List<User> userList = baseMapper.selectPage(userPage, wrapper.orderBy("create_date", true));
        if(userList != null && userList.size() > 0){
            //查询 user 的角色
            List<UserRole> userRoles = userRoleMapper.selectByUserIds(getUserIds(userList));
            for (User one:userList) {
                List<Role> tempURs = new ArrayList<>();
                for (UserRole ur: userRoles) {
                    if(one.getUserId().equals(ur.getUserId())){
                        tempURs.add(new Role(ur.getRoleId(),ur.getRoleName()));
                    }
                }
                one.setRoles(tempURs);
            }
        }
        return new PageResult<>(userPage.getTotal(),userList);
    }

    @Override
    public boolean updateState(Integer userId, Integer state) {
        if(state != 0 && state != 1){
            throw new ParameterException("state 值需要在 0或 1");
        }
        User user = new User();
        user.setUserId(userId);
        user.setState(state);
        // > 0 说明成功 为 true ; < 0 不成功 为 false
        return baseMapper.updateById(user) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(User user) throws BusinessException {
        if(baseMapper.selectByUserName(user.getUserName()) !=  null){
            throw new BusinessException("账号已存在");
        }
        user.setPassword(EndecryptUtil.encrytMd5(user.getPassword(),user.getUserName(),3));
        user.setState(0); //默认启用用户
        user.setCreateDate(new Date());
        boolean bs = baseMapper.insert(user) > 0; //如果返回值大于 0 ,说明新增成功
        if(bs){//为用户分配权限,先用户权限表中写入数据
            //先获取新用户所有角色的Id集合
            List<Integer> roleIds = getRoleIds(user.getRoles());
            //向用户权限表中批量添加,返回添加成功的数量,如果没有全部成功,返回添加失败
            if(userRoleMapper.insertBatch(user.getUserId(),roleIds) < roleIds.size()){
                throw new BusinessException("添加失败,请重试");
            }
        }
        return bs;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(User user) throws BusinessException {
        user.setUserName(null); //userName 是唯一的,不能修改,这里设置为 null,更新时不修改用户名
        boolean bs = baseMapper.updateById(user) > 0;
        if(bs){ //如果更新成功,则更新用户的权限
            //先将用户原有的权限从用户权限表中删除
            userRoleMapper.delete(new EntityWrapper().eq("user_id",user.getUserId()));
            List<Integer> roleIds = getRoleIds(user.getRoles());
            //然后再重新向用户权限表中添加权限数据
            if(userRoleMapper.insertBatch(user.getUserId(),roleIds) < roleIds.size()){
                throw new BusinessException("修改失败,请重试");
            }
        }
        return bs;
    }

    @Override
    public boolean updPsw(Integer loginUserId, String loginUserName, String newPsw) {
        User user = new User();
        user.setUserId(loginUserId);
        //重新设置密码
        user.setPassword(EndecryptUtil.encrytMd5(newPsw,loginUserName,3));
        return baseMapper.updateById(user) > 0;
    }

    @Override
    public User getById(Integer userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public boolean delete(Integer userId) {
        return baseMapper.deleteById(userId) > 0;
    }
}
