package cn.dmego.odsp.algorithms.vo;

import lombok.Data;

import java.util.List;

/**
 * class_name: DEAVo
 * package: cn.dmego.odsp.algorithms.vo
 * describe: 数据包络分析 VO 对象
 * creat_user: Dmego
 * creat_date: 2018/12/29
 * creat_time: 20:24
 **/
@Data
public class DEAVo {

    private String[] functions; //方法选择数组

    private String fileName; //选择的文件名称

    private String[] dmuNames; //DMU 决策单元数组

    private String[] variableNames; //变量指标简写数组

    private String[] varCHNames; //变量指标中文数组

    private String[] input; //投入指标集合

    private String[] output; //产出指标集合

    private double[][] matrix; //矩阵数据数组

    public DEAVo(){ }

    public DEAVo(String[] functions,String fileName,String[] dmuNames, String[] varCHNames, String[] variableNames, String[] input,String[] output){
        this.functions = functions;
        this.fileName = fileName;
        this.dmuNames = dmuNames;
        this.varCHNames = varCHNames;
        this.variableNames = variableNames;
        this.input = input;
        this.output = output;
    }


}
