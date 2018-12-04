package cn.dmego.odsp.algorithms.controller;

import cn.dmego.odsp.common.JsonResult;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.service.UncertainService;
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
 * class_name: UncertainController
 * package: cn.dmego.odsp.decisionModels.controller
 * describe: 不确定型风险决策法 controller
 * creat_user: Dmego
 * creat_date: 2018/11/4
 * creat_time: 17:45
 **/
@Controller
@RequestMapping("/decision/uncertain")
public class UncertainController {


    @Autowired
    private UncertainService uncertainService;

    @RequestMapping
    public String uncertain(Model model){
        return "decision/uncertain.html";
    }

    /**
     * 编辑系数
     * @param action
     * @param state
     * @param model
     * @return
     */
    @RequestMapping("/editForm/{action}/{state}/{isUpd}")
    public String editForm(@PathVariable("action") String action,@PathVariable("state")
            String state,@PathVariable("isUpd") String isUpd,Model model){
        model.addAttribute("action",action);
        model.addAttribute("state",state);
        model.addAttribute("isUpd",isUpd);
        return "decision/uncertainForm.html";
    }

    /**
     * 获取前台提交的数据,进行处理计算
     * @param uncertainVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/calculate")
    public JsonResult calculate(@RequestParam("funArr[]") List<Integer> funArr, DecisionVo uncertainVo){

        Double tableData[][] = CommonUtil.jsonToArray(uncertainVo);

        uncertainVo.setFunctions(funArr);
        uncertainVo.setMatrix(tableData);

        JsonResult calculate = uncertainService.calculate(uncertainVo);

        return calculate;
    }


}
