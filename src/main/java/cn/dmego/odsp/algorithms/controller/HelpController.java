package cn.dmego.odsp.algorithms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * class_name: HelpController
 * package: cn.dmego.odsp.algorithms.controller
 * describe: 模型说明
 * creat_user: Dmego
 * creat_date: 2019/1/9
 * creat_time: 6:53
 **/
@Controller
@RequestMapping("/help")
public class HelpController {

    @RequestMapping
    public String help(){
        return "help/index.html";
    }
}
