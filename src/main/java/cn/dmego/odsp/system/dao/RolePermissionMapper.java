package cn.dmego.odsp.system.dao;

import cn.dmego.odsp.system.model.RolePermission;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * class_name: RolePermissionMapper
 * package: cn.dmego.odsp.system.dao
 * describe: 角色权限表 Mapper
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:34
 **/
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    int insertRoleAuths(@Param("roleId") Integer roleId, @Param("authIds") List<Integer> authIds);


}
