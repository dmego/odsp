package cn.dmego.odsp.algorithms.controller;


import cn.dmego.odsp.algorithms.service.StrategySerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * class_name: StrategyController
 * package: cn.dmego.odsp.algorithms.controller
 * describe: TODO
 * creat_user: Dmego
 * creat_date: 2018/12/17
 * creat_time: 3:17
 **/

@Controller
@RequestMapping("/optimization/strategy")
public class StrategyController {

    @Autowired
    private StrategySerice strategySerice;

    @RequestMapping
    public String strategy(Model model){
        return "optimization/strategy.html";
    }

    @RequestMapping("/editForm/{vari}/{cons}/{isUpd}")
    public String strategyForm( @PathVariable("vari") int vari, @PathVariable("cons") int cons, @PathVariable("isUpd") String isUpd, Model model){
        model.addAttribute("vari",vari);
        model.addAttribute("cons",cons);
        model.addAttribute("isUpd",isUpd);
        return "optimization/strategyForm.html";
    }


}
