package cn.dmego.odsp.system.dao;

import cn.dmego.odsp.system.model.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * class_name: UserMapper
 * package: cn.dmego.odsp.system.dao
 * describe: 用户表 Mapper
 * creat_user: Dmego
 * creat_date: 2018/10/21
 * creat_time: 22:32
 **/
public interface UserMapper extends BaseMapper<User> {

    User selectByUserName(String userName);
}
