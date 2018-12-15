package cn.dmego.odsp.algorithms.service;

import cn.dmego.odsp.algorithms.vo.GraphVo;
import cn.dmego.odsp.common.JsonResult;

/**
 * class_name: GraphService
 * package: cn.dmego.odsp.algorithms.service
 * describe: 图与网络分析 Service 接口类
 * creat_user: Dmego
 * creat_date: 2018/12/6
 * creat_time: 23:34
 **/
public interface GraphService {

    JsonResult calculate(GraphVo graphVo);

}
