package cn.dmego.odsp.algorithms.controller;

import cn.dmego.odsp.algorithms.service.DEAService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.DEAVo;
import cn.dmego.odsp.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * class_name: DEAController
 * package: cn.dmego.odsp.algorithms.controller
 * describe: 数据包络分析 Controller
 * creat_user: Dmego
 * creat_date: 2018/12/29
 * creat_time: 22:47
 **/
@Controller
@RequestMapping("/analysis/deaModel")
public class DEAController {

    @Autowired
    private DEAService deaService;

    @RequestMapping
    public String deaModel(){
        return "analysis/deaModel.html";
    }

    @RequestMapping("/editForm/{dum}/{input}/{output}/{isUpd}")
    public String strategyForm(@PathVariable("dum") int dum, @PathVariable("input") int input,
                               @PathVariable("output") int output, @PathVariable("isUpd") String isUpd, Model model){
        model.addAttribute("dum",dum);
        model.addAttribute("input",input);
        model.addAttribute("output",output);
        model.addAttribute("isUpd",isUpd);
        return "analysis/deaModelForm.html";
    }

    /**
     * 获取前台DEA模型数据,进行计算
     * @param deaVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/calculate")
    public JsonResult calculate(@RequestParam("funArr[]") String[] funArr,  @RequestParam("dmuNamesArr[]") String[] dmuNamesArr,
                                @RequestParam("variableNamesArr[]") String[] variableNamesArr, DEAVo deaVo){
        deaVo.setFunctions(funArr);
        deaVo.setDmuNames(dmuNamesArr);
        deaVo.setVariableNames(variableNamesArr);

        CommonUtil.jsonToArray(deaVo);

        JsonResult calculate = deaService.calculate(deaVo);

        return calculate;
    }


}
