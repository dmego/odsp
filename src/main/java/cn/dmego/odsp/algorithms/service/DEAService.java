package cn.dmego.odsp.algorithms.service;

import cn.dmego.odsp.algorithms.vo.DEAVo;
import cn.dmego.odsp.common.JsonResult;

/**
 * class_name: DEAService
 * package: cn.dmego.odsp.algorithms.service
 * describe: 数据包络分析 Service 接口类
 * creat_user: Dmego
 * creat_date: 2018/12/29
 * creat_time: 22:46
 **/
public interface DEAService {

    JsonResult calFromSo(DEAVo deaVo);

}
