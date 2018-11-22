package cn.dmego.odsp.algorithms.service;


import cn.dmego.odsp.common.JsonResult;
import cn.dmego.odsp.algorithms.vo.DecisionVo;

/**
 * class_name: UncertainService
 * package: cn.dmego.odsp.decisionModels.service
 * describe: 不确定型决策法 Service 接
 * creat_user: Dmego
 * creat_date: 2018/11/4
 * creat_time: 17:05
 **/
public interface UncertainService {

    JsonResult calculate(DecisionVo uncertainVo);
}
