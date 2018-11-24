package cn.dmego.odsp.algorithms.service;

import cn.dmego.odsp.algorithms.vo.DynamicVo;
import cn.dmego.odsp.common.JsonResult;

/**
 * class_name: DynamicService
 * package: cn.dmego.odsp.algorithms.service
 * describe: 动态规划Service 接口
 * creat_user: Dmego
 * creat_date: 2018/11/22
 * creat_time: 12:55
 **/
public interface DynamicService {

    JsonResult calculate(DynamicVo dynamicVo);
}
