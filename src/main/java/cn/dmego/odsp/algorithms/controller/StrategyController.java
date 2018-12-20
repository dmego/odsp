package cn.dmego.odsp.algorithms.controller;


import cn.dmego.odsp.algorithms.service.StrategyService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.StrategyVo;
import cn.dmego.odsp.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * class_name: StrategyController
 * package: cn.dmego.odsp.algorithms.controller
 * describe: 决策规划 Controller 类
 * creat_user: Dmego
 * creat_date: 2018/12/17
 * creat_time: 3:17
 **/

@Controller
@RequestMapping("/optimization/strategy")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @RequestMapping
    public String strategy(Model model){
        return "optimization/strategy.html";
    }

    @RequestMapping("/editForm/{vari}/{cons}/{isUpd}")
    public String strategyForm(@PathVariable("vari") int vari, @PathVariable("cons") int cons, @PathVariable("isUpd") String isUpd, Model model){
        model.addAttribute("vari",vari);
        model.addAttribute("cons",cons);
        model.addAttribute("isUpd",isUpd);
        return "optimization/strategyForm.html";
    }

    /**
     * 获取前台决策规划数据,进行计算
     * @param strategyVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/calculate")
    public JsonResult calculate(StrategyVo strategyVo){

        CommonUtil.jsonToArray(strategyVo);
        JsonResult calculate = strategyService.calculate(strategyVo);
        return calculate;
    }


}
