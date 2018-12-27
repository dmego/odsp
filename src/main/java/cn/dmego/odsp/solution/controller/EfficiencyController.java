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

    @RequestMapping
    public String efficiency(Model model){
        return "solution/efficiency.html";
    }
}
