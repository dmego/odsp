package cn.dmego.odsp.system.service.impl;

import cn.dmego.odsp.common.PageResult;
import cn.dmego.odsp.system.dao.LogMapper;
import cn.dmego.odsp.system.model.Log;
import cn.dmego.odsp.system.service.LogService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * class_name: LogServiceImpl
 * package: cn.dmego.odsp.system.service.impl
 * describe: 日志管理 Service 接口实现类
 * creat_user: Dmego
 * creat_date: 2018/10/29
 * creat_time: 21:35
 **/
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public PageResult<Log> list(Integer pageNum, Integer pageSize, String startDate, String endDate, String account) {
        Page<Log> page = new Page<>(pageNum,pageSize);
        List<Log> logs = logMapper.listFull(page,startDate,endDate,account);
        return new PageResult<>(page.getTotal(),logs);
    }

    @Override
    public boolean add(Log log) {
        log.setCreateDate(new Date());
        return logMapper.insert(log) > 0;
    }
}
