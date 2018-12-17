package cn.dmego.odsp.algorithms.service.impl;

import cn.dmego.odsp.algorithms.calculateModel.LinearProgram;
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
 * describe: TODO
 * creat_user: Dmego
 * creat_date: 2018/12/17
 * creat_time: 3:16
 **/
@Service
public class StrategyServiceImpl implements StrategyService {

    @Override
    public JsonResult calculate(StrategyVo strategyVo) {
        Integer fun = strategyVo.getFun();

//        Integer vari = strategyVo.getVariable();
//        Integer cons = strategyVo.getConstraint();
//        Integer sType = strategyVo.getSType(); //目标类型(1:极大,2:极小)
//        Map<Integer, String> direction = strategyVo.getDirection();
//        double[] extremums = strategyVo.getExtremums();
//        double[] increments = strategyVo.getIncrements();
//        double[][] matrix = strategyVo.getMatrix();

        JsonResult jsonResult = new JsonResult();
        List<Map<String, String>> mapList = new ArrayList<>();
        LinearProgram linearProgram = new LinearProgram();
        if(fun == 1){ //线性规划
            if(linearProgram.linearProgramCalculate(strategyVo)){
                mapList = linearProgram.resultData;
            }else{
                jsonResult = JsonResult.error(500, "线性规划问题计算错误!"+linearProgram.log);
                return jsonResult;
            }

        }else if(fun == 2){ //整数规划

        }else if(fun == 3){ //0-1规划

        }

        jsonResult.put("result",mapList);
        CommonUtil.retState(jsonResult,200);
        return jsonResult;
    }


}
