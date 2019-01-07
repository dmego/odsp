package cn.dmego.odsp.algorithms.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * class_name: Uncertain
 * package: cn.dmego.odsp.decision.pojo
 * describe: 决策模型 Vo 对象
 * creat_user: Dmego
 * creat_date: 2018/11/7
 * creat_time: 20:54
 **/

/**
 方法选择数组 不确定型决策法:{1:悲观,2:乐观,3:最小机会,4:等概率,5:折中}
*/
@Data
public class DecisionVo {

    private List<Integer> functions; //方法选择数组

    private Integer action; //行动方案 列

    private Integer state; //自然状态 行

    private Double hopeful; //乐观系数

    private Double utility; //效用曲率系数

    private Double[] chance; //自然概率数组

    private String ratioTableData; //json 字符串格式的表格数据

    private Double[][] matrix; //矩阵数据数组

    private Double[] bestArr; //根据方法计算出来的结果数组

    private Integer bestFlag; //最佳结果的下标

    private Double bestValue; //最佳结果的值

    private Double expect; //全情报期望收益

    private Double value; //全情报价值（EVPI）
}
