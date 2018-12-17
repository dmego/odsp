package cn.dmego.odsp.algorithms.controller;

import cn.dmego.odsp.algorithms.service.GraphService;

import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.GraphVo;
import cn.dmego.odsp.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/editForm/{fun}/{item}/{isUpd}")
    public String graphForm(@PathVariable("fun") int fun,@PathVariable("item") int item,@PathVariable("isUpd") String isUpd,Model model){
        model.addAttribute("fun",fun);
        model.addAttribute("item",item);
        model.addAttribute("isUpd",isUpd);
        return "optimization/graphForm.html";
    }

    @ResponseBody
    @RequestMapping("/calculate")
    public JsonResult calculate(@RequestParam("vexsArr[]") String[] vexsArr, GraphVo graphVo){
        graphVo.setVexNum(vexsArr.length); //顶点数
        CommonUtil.jsonToGraph(vexsArr,graphVo);

        JsonResult calculate = graphService.calculate(graphVo);

        return calculate;
    }


}
