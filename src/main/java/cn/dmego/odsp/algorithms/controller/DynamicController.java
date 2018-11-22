package cn.dmego.odsp.algorithms.controller;

import cn.dmego.odsp.algorithms.service.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * class_name: DynamicController
 * package: cn.dmego.odsp.algorithms.controller
 * describe: 动态规划 Controller
 * creat_user: Dmego
 * creat_date: 2018/11/22
 * creat_time: 12:53
 **/
@Controller
@RequestMapping("/optimization/dynamic")
public class DynamicController {

    @Autowired
    private DynamicService dynamicService;

    @RequestMapping
    public String risk(Model model){
        return "optimization/dynamic.html";
    }
}
