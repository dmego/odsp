package cn.dmego.odsp.algorithms.controller;

import cn.dmego.odsp.algorithms.service.DynamicService;
import cn.dmego.odsp.algorithms.vo.DynamicVo;
import cn.dmego.odsp.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String dynamic(Model model){
        return "optimization/dynamic.html";
    }

    @RequestMapping("/editForm/{fun}/{item1}/{item2}/{isUpd}")
    public String dynamicForm(@PathVariable("fun") int fun,@PathVariable("item1") int item1,
                              @PathVariable("item2") int item2,@PathVariable("isUpd") String isUpd,
                              Model model){
        model.addAttribute("fun",fun);
        model.addAttribute("item1",item1);
        model.addAttribute("item2",item2);
        model.addAttribute("isUpd",isUpd);
        return "optimization/dynamicForm.html";
    }

    /**
     * 获取前台动态规划数据,进行计算
     * @param dynamicVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/calculate")
    public JsonResult calculate(DynamicVo dynamicVo){

        System.out.println(dynamicVo.toString());
        return null;
    }

}
