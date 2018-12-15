package cn.dmego.odsp.algorithms.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * class_name: TargetVo
 * package: cn.dmego.odsp.algorithms.vo
 * describe: 目标规划Vo对象
 * creat_user: Dmego
 * creat_date: 2018/12/13
 * creat_time: 3:22
 **/
@Data
public class TargetVo {
    private double m_dM = 10000.0;
    private List<String> m_pListAddVariable = new ArrayList<>();
    private final String m_sXSWS3 = "f3";
    private int m_nDieDai;
    private int m_nResultType;

    private int m_nVariable; //决策变量个数
    private int m_nTargetConstraint; //目标约束个数
    private int m_nAbsoluteConstraint; //绝对约束个数
    private int m_nPriority;//优先级数


}
