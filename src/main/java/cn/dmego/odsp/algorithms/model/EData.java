package cn.dmego.odsp.algorithms.model;

import lombok.Data;

/**
 * class_name: EData
 * package: cn.dmego.odsp.algorithms.model
 * describe: 边的结构体
 * creat_user: Dmego
 * creat_date: 2018/12/13
 * creat_time: 4:19
 **/

@Data
public class EData {
    private String start; //起点
    private String end; //终点
    private double weight; //边的权重

    public EData(String start, String end, double weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }
}
