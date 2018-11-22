package cn.dmego.odsp.system.controller;

import cn.dmego.odsp.common.PageResult;
import cn.dmego.odsp.common.utils.StringUtil;
import cn.dmego.odsp.system.model.Log;
import cn.dmego.odsp.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * class_name: LogController
 * package: cn.dmego.odsp.system.controller
 * describe: 日志管理 Controler
 * creat_user: Dmego
 * creat_date: 2018/10/29
 * creat_time: 21:37
 **/
@Controller
@RequestMapping("system/log")
public class LogController {

    @Autowired
    private LogService logService;

    @RequestMapping()
    public String log(){
        return "system/log.html";
    }

    /**
     * 查询错所有日志
     * @param page
     * @param limit
     * @param startDate
     * @param endDate
     * @param account
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<Log> list(Integer page,Integer limit, String startDate,String endDate,String account){
        if(StringUtil.isBlank(startDate)){
            startDate = null;
        }else {
            startDate += " 00:00:00"; //否则在后面添加时分秒,方便后面转格式
        }
        if(StringUtil.isBlank(endDate)){
            endDate = null;
        }else {
            endDate += " 23:59:59";
        }
        if(StringUtil.isBlank(account)){
            account = null;
        }
        return logService.list(page,limit,startDate,endDate,account);

    }

}
