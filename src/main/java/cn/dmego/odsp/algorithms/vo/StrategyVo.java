package cn.dmego.odsp.algorithms.vo;

import lombok.Data;

import java.util.Map;

/**
 * class_name: StrategyVo
 * package: cn.dmego.odsp.algorithms.vo
 * describe: 决策规划 Vo 对象
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

    private Map<Integer,String> direction; //方向Map

    private double[] increments; //允许增量数组

    private double[] extremums; //极值数组

    private String ratioTableData; //json 字符串格式的表格数据

    private double[][] matrix; //矩阵数据数组

    private Integer bestValue; //最佳结果的值

}
