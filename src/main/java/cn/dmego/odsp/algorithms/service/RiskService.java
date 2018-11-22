package cn.dmego.odsp.algorithms.service;

import cn.dmego.odsp.common.JsonResult;
import cn.dmego.odsp.algorithms.vo.DecisionVo;

/**
 * class_name: RiskService
 * package: cn.dmego.odsp.decision.service
 * describe: 风险决策法 Service 接口类
 * creat_user: Dmego
 * creat_date: 2018/11/14
 * creat_time: 18:26
 **/
public interface RiskService {

    JsonResult calculate(DecisionVo riskVo);
}
