package cn.dmego.odsp.system.dao;

import cn.dmego.odsp.system.model.Permission;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * class_name: PermissionMapper
 * package: cn.dmego.odsp.system.dao
 * describe: 权限表 Mapper
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:33
 **/
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> listByRoleId(Integer roleId);

    List<Permission> listByRoleIds(@Param("roleIds") List<Integer> roleIds);

    List<Permission> listByUserId(Integer loginUserId);
}
