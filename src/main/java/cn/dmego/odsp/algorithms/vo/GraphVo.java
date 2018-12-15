package cn.dmego.odsp.algorithms.vo;

import cn.dmego.odsp.algorithms.model.EData;
import cn.dmego.odsp.algorithms.model.Graph;
import lombok.Data;

/**
 * class_name: GraphVo
 * package: cn.dmego.odsp.algorithms.vo
 * describe: 图与网络分析VO对象
 * creat_user: Dmego
 * creat_date: 2018/12/12
 * creat_time: 21:35
 **/
@Data
public class GraphVo {

    private Integer fun; //选择的方法

    private Integer gType; //选择的图类型

    private Integer vexNum; //顶点数

    private Integer arcNum; //边,弧的条数

    private String start; //起点

    private String end; //终点

    private String ratioTableData; //json 字符串格式的表格数据

    private Graph graph;

    private Integer[][] matrix; //矩阵数据数组

    private Integer bestValue; //最佳结果的值

}
