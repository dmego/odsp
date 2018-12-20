package cn.dmego.odsp.algorithms.service;

import cn.dmego.odsp.algorithms.vo.TransportVo;
import cn.dmego.odsp.common.JsonResult;

/**
 * class_name: TransportService
 * package: cn.dmego.odsp.algorithms.service
 * describe: 运输问题 Service 类
 * creat_user: Dmego
 * creat_date: 2018/12/19
 * creat_time: 16:53
 **/
public interface TransportService {

    JsonResult calculate(TransportVo transportVo);
}
