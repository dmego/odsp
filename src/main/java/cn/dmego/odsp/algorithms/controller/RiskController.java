package cn.dmego.odsp.algorithms.controller;

import cn.dmego.odsp.common.JsonResult;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.service.RiskService;
import cn.dmego.odsp.algorithms.vo.DecisionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * class_name: RiskController
 * package: cn.dmego.odsp.decision.controller
 * describe: 风险决策法 Controller
 * creat_user: Dmego
 * creat_date: 2018/11/12
 * creat_time: 13:49
 **/
@Controller
@RequestMapping("/decision/risk")
public class RiskController {

    @Autowired
    private RiskService riskService;

    @RequestMapping
    public String risk(Model model){
        return "decision/risk.html";
    }

    /**
     * 编辑系数
     * @param action
     * @param state
     * @param model
     * @return
     */
    @RequestMapping("/editForm/{action}/{state}/{isUpd}")
    public String editForm(@PathVariable("action") String action, @PathVariable("state")
            String state, @PathVariable("isUpd") String isUpd, Model model){
        model.addAttribute("action",action);
        model.addAttribute("state",state);
        model.addAttribute("isUpd",isUpd);
        return "decision/riskForm.html";
    }

    /**
     * 获取前台提交的风险决策法数据,进行处理计算
     * @param funArr
     * @param riskVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/calculate")
    public JsonResult calculate(@RequestParam("funArr[]") List<Integer> funArr, DecisionVo riskVo){

        Double tableData[][] = CommonUtil.jsonToArray(riskVo); //将JSON字符串转成矩阵数据数组

        riskVo.setFunctions(funArr); //将选择的计算方法保存到 VO 对象中
        riskVo.setMatrix(tableData); //将转换的二维数组对象保存到 VO 对象中

        JsonResult calculate = riskService.calculate(riskVo); //计算并返回计算结果

        CommonUtil.retState(calculate); //根据计算结果大小设置返回 code 参数

        return calculate;

    }

}
