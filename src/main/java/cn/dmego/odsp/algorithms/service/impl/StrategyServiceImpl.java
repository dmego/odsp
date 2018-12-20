package cn.dmego.odsp.algorithms.service.impl;

import cn.dmego.odsp.algorithms.calculateModel.LinearProgram;
import cn.dmego.odsp.algorithms.calculateModel.ZeroOneProgram;
import cn.dmego.odsp.algorithms.service.StrategyService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.StrategyVo;
import cn.dmego.odsp.common.JsonResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * class_name: StrategySericeImpl
 * package: cn.dmego.odsp.algorithms.service.impl
 * describe: 决策规划 Service 实现类
 * creat_user: Dmego
 * creat_date: 2018/12/17
 * creat_time: 3:16
 **/
@Service
public class StrategyServiceImpl implements StrategyService {

    @Override
    public JsonResult calculate(StrategyVo strategyVo) {
        Integer fun = strategyVo.getFun();

        JsonResult jsonResult = new JsonResult();
        List<Map<String, String>> mapList = new ArrayList<>();

        LinearProgram linearProgram = new LinearProgram();
        ZeroOneProgram zeroOneProgram = new ZeroOneProgram();
        if(fun == 1){ //线性规划
            if(linearProgram.linearProgramCalculate(strategyVo)){
                mapList = linearProgram.resultData;
            }else{
                jsonResult = JsonResult.error(500, "线性规划问题计算错误!"+linearProgram.log);
                return jsonResult;
            }

        }else if(fun == 2){ //整数规划
            if(linearProgram.IntegerProgramCalculate(strategyVo)){
                mapList = linearProgram.resultData;
            }else{
                jsonResult = JsonResult.error(500, "整数规划问题计算错误!msg="+linearProgram.log);
                return jsonResult;
            }

        }else if(fun == 3){ //0-1规划
            if(zeroOneProgram.zeroOneProgramCalculate(strategyVo)){
                mapList = zeroOneProgram.resultData;
                List<Map<String, String>> calculateResultData = zeroOneProgram.calculateResultData;
                jsonResult.put("calculateResult",calculateResultData);
            }else {
                jsonResult = JsonResult.error(500, "0-1规划问题计算错误!msg="+zeroOneProgram.log);
                return jsonResult;
            }
        }

        jsonResult.put("result",mapList);
        CommonUtil.retState(jsonResult,200);
        return jsonResult;
    }


}
