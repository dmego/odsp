package cn.dmego.odsp.system.dao;

import cn.dmego.odsp.system.model.UserRole;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * class_name: UserRoleMapper
 * package: cn.dmego.odsp.system.dao
 * describe: 用户角色表 Mapper
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:33
 **/
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<UserRole> selectByUserIds(@Param("userIds") List<Integer> userIds);

    int insertBatch(@Param("userId") Integer userId, @Param("roleIds") List<Integer> roleIds);

}
