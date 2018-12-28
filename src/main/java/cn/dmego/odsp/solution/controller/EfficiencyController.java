package cn.dmego.odsp.solution.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * class_name: EfficiencyController
 * package: cn.dmego.odsp.solution.controller
 * describe: 效率评价 Controller
 * creat_user: Dmego
 * creat_date: 2018/12/24
 * creat_time: 4:13
 **/
@Controller
@RequestMapping("/solution/efficiency")
public class EfficiencyController {

    /**
     * 跳转到数据采集页面
     */
    @RequestMapping("/efCollect")
    public String efCollect(Model model){
        return "solution/efficiency/efCollect.html";
    }

    /**
     * 跳转到数据处理页面
     */
    @RequestMapping("/efProcess")
    public String efProcess(Model model){
        return "solution/efficiency/efProcess.html";
    }

    /**
     * 跳转到结果分析页面
     */
    @RequestMapping("/efAnalysis")
    public String efAnalysis(Model model){
        return "solution/efficiency/efAnalysis.html";
    }

}
