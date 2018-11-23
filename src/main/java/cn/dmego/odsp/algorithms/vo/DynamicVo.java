package cn.dmego.odsp.algorithms.vo;

import io.swagger.models.auth.In;
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

    private Integer fun;

    private Integer packFun;

    private Integer kBreedNum;

    private Integer kVolume;

    private Integer rItemNum;

    private Integer rSumValue;

    private Integer rStrategy;

    private Integer pStage;

    private Integer pStorage;

    private String ratioTableData; //json 字符串格式的表格数据

}
