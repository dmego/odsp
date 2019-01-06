package cn.dmego.odsp.algorithms.service.impl;

import cn.dmego.odsp.algorithms.calculateModel.TransportProblem;
import cn.dmego.odsp.algorithms.service.TransportService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.TransportVo;
import cn.dmego.odsp.common.JsonResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * class_name: TransportServiceImpl
 * package: cn.dmego.odsp.algorithms.service.impl
 * describe: 运输问题 Service 实现类
 * creat_user: Dmego
 * creat_date: 2018/12/19
 * creat_time: 16:53
 **/
@Service
public class TransportServiceImpl implements TransportService {


    @Override
    public JsonResult calculate(TransportVo transportVo) {
        JsonResult jsonResult = new JsonResult();
        List<Map<String, String>> mapList = new ArrayList<>();
        TransportProblem transportProblem = new TransportProblem();

        if(transportProblem.TransportationProblemCalculate(transportVo)){
            mapList = transportProblem.resultData;
        }else {
            jsonResult = JsonResult.error(500, "运输问题计算错误!"+transportProblem.log);
            return jsonResult;
        }

        jsonResult.put("result",mapList);
        CommonUtil.retState(jsonResult,200);
        return jsonResult;
    }


    /**
     * 计算解决方案中的运输优化问题
     * @param transportVo
     */
    @Override
    public TransportProblem calculateSolution(TransportVo transportVo) {
        TransportProblem transportProblem = new TransportProblem();
        if(transportProblem.TransportationProblemCalculate(transportVo)){
            return transportProblem;
        }else {
            return null;
        }
    }
}
