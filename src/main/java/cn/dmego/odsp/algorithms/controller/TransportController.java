package cn.dmego.odsp.algorithms.controller;

import cn.dmego.odsp.algorithms.service.TransportService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.TransportVo;
import cn.dmego.odsp.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * class_name: TransportController
 * package: cn.dmego.odsp.algorithms.controller
 * describe: 运输问题 Controller
 * creat_user: Dmego
 * creat_date: 2018/12/19
 * creat_time: 16:51
 **/
@Controller
@RequestMapping("/optimization/transport")
public class TransportController {

    @Autowired
    private TransportService transportService;

    @RequestMapping
    public String transport(Model model){
        return "optimization/transport.html";
    }

    @RequestMapping("/editForm/{origin}/{sales}/{isUpd}")
    public String transportForm(@PathVariable("origin") int origin, @PathVariable("sales") int sales, @PathVariable("isUpd") String isUpd, Model model){
        model.addAttribute("vari",origin);
        model.addAttribute("cons",sales);
        model.addAttribute("isUpd",isUpd);
        return "optimization/transportForm.html";
    }

    @ResponseBody
    @RequestMapping("calculate")
    public JsonResult calculate(TransportVo transportVo){
        CommonUtil.jsonToArray(transportVo);
        JsonResult calculate = transportService.calculate(transportVo);
        return calculate;
    }

}
