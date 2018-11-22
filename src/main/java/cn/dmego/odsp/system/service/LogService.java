package cn.dmego.odsp.system.service;

import cn.dmego.odsp.common.PageResult;
import cn.dmego.odsp.system.model.Log;

/**
 * class_name: LogService
 * package: cn.dmego.odsp.system.service
 * describe: 日志管理 Service 接口类
 * creat_user: Dmego
 * creat_date: 2018/10/29
 * creat_time: 21:34
 **/
public interface LogService {

    PageResult<Log> list(Integer page, Integer limit, String startDate, String endDate, String account);

    boolean add(Log log);

}
