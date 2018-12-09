package cn.dmego.odsp.algorithms.controller;

import cn.dmego.odsp.algorithms.service.GraphService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * class_name: GraphController
 * package: cn.dmego.odsp.algorithms.controller
 * describe: 图与网络分析 Controller
 * creat_user: Dmego
 * creat_date: 2018/12/6
 * creat_time: 23:34
 **/
@Controller
@RequestMapping("/optimization/graph")
public class GraphController {

    @Autowired
    private GraphService graphService;

    @RequestMapping
    public String graph(Model model){
        return "optimization/graph.html";
    }


}
