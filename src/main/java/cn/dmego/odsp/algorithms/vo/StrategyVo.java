package cn.dmego.odsp.algorithms.vo;

import lombok.Data;

/**
 * class_name: StrategyVo
 * package: cn.dmego.odsp.algorithms.vo
 * describe: TODO
 * creat_user: Dmego
 * creat_date: 2018/12/17
 * creat_time: 3:23
 **/
@Data
public class StrategyVo {

    private Integer fun; //选择的方法

    private Integer variable; //决策变量个数

    private Integer constraint; //约束条件个数

    private Integer sType; //目标类型

    private double[] increments; //允许增量数组

    private double[] extremum; //极值数组

    private String ratioTableData; //json 字符串格式的表格数据

    private Integer[][] matrix; //矩阵数据数组

    private Integer bestValue; //最佳结果的值

}
