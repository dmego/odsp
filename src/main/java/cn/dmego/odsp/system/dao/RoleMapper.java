package cn.dmego.odsp.system.dao;

import cn.dmego.odsp.system.model.Role;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * class_name: RoleMapper
 * package: cn.dmego.odsp.system.dao
 * describe: 权限表 Mapper
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:32
 **/
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectByUserId(Integer userId);
}
