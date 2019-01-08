package cn.dmego.odsp.algorithms.controller;

import cn.dmego.odsp.algorithms.service.DEAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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


}
