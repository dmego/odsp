package cn.dmego.odsp.algorithms.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * class_name: TransportVo
 * package: cn.dmego.odsp.algorithms.vo
 * describe: 运输问题 VO 对象
 * creat_user: Dmego
 * creat_date: 2018/12/19
 * creat_time: 16:52
 **/
@Data
public class TransportVo {

    private Integer fun; //选择的方法

    private Integer sType; //类型

    private Integer originNum; //产地个数

    private String[] originNames; //产地名称数组

    private Integer salesNum; //销地个数

    private String[] saleNames; //销地名称数组

    private String ratioTableData; //json 字符串格式的表格数据

    private double[] output; //产量

    private double[] sales; //销量

    private double[][] price;

    private double[][] price2;

    private double[][] plan;

    private Integer bestValue; //最佳结果的值

}
