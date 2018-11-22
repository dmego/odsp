package cn.dmego.odsp.system.dao;

import cn.dmego.odsp.system.model.Log;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * class_name: LogMapper
 * package: cn.dmego.odsp.system.dao
 * describe: 日志 Mapper 接口
 * creat_user: Dmego
 * creat_date: 2018/10/29
 * creat_time: 21:36
 **/
public interface LogMapper extends BaseMapper<Log> {

    List<Log> listFull(Page<Log> page, @Param("startDate") String startDate,@Param("endDate") String endDate,@Param("account") String account);

}
