package cn.dmego.odsp.algorithms.service;

import cn.dmego.odsp.algorithms.vo.StrategyVo;
import cn.dmego.odsp.common.JsonResult;

/**
 * class_name: StrategyService
 * package: cn.dmego.odsp.algorithms.service
 * describe: TODO
 * creat_user: Dmego
 * creat_date: 2018/12/17
 * creat_time: 3:17
 **/
public interface StrategyService {

    JsonResult calculate(StrategyVo strategyVo);

}
