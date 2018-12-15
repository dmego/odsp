package cn.dmego.odsp.algorithms.vo;

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

    private Integer mpArcNum;
    private String mpStart;
    private String mpEnd;

    private Integer mfArcNum;
    private String mfStart;
    private String mfEnd;

    private Integer mtArcNum;
    private String mtStart;
    private String mtEnd;

    private Integer mcArcNum;
    private String mcStart;
    private String mcEnd;

    private String ratioTableData; //json 字符串格式的表格数据

    private Integer[][] matrix; //矩阵数据数组

    private Integer bestValue; //最佳结果的值

}
