package cn.dmego.odsp.algorithms.vo;

import lombok.Data;

/**
 * class_name: DynamicVo
 * package: cn.dmego.odsp.algorithms.vo
 * describe: 动态规划 Vo 对象
 * creat_user: Dmego
 * creat_date: 2018/11/22
 * creat_time: 12:55
 **/
@Data
public class DynamicVo {

    private Integer fun; //选择的方法

    private Integer packFun; //背包问题选择的方法

    private Integer kBreedNum; //物品品种数

    private Integer kVolume;//容器容量

    private Integer rItemNum;//项目数

    private Integer rSumValue;//资源总数量

    private Integer rStrategy;//可选择的投资策略

    private Integer pStage;//生产时期

    private Integer pStorage;//期初存储量 init storage

    private String ratioTableData; //json 字符串格式的表格数据

    private Integer[][] matrix; //矩阵数据数组

    private Integer bestValue; //最佳结果的值

    //背包问题的VO对象
    private String[] packNames; //物品名称数组
    private Integer[] weights; //单位物品重量数组
    private Integer[] values; //单位物品价值数组
    private Integer[] limit; //物品限量数组

    private Integer[] choiceNums; //选择物品的数量
    private Integer[] sumValues; //物品的总价值
    private Integer[] restVolume; //剩余容量

    //资源分配问题VO对象
    private Integer[] strategy; //可选投资策略数组

    //生产与存储问题VO对象
    private Integer[] demand; //需求量 demand
    private Integer[] productPower; //生产能力 product power
    private Double[] unitProductCost; //单位产品生产成本 unit product cost
    private Double[] unitStorageCost; //单位产品存储成本 unit storage cost
    private Double[] fixedProductCost; //固定生产成本 fixed product cost
    private Integer[] maxStorage; //最大存储量 max storage
}
