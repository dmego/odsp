package cn.dmego.odsp.algorithms.calculateModel;

import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.StrategyVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class_name: LinearProgram
 * package: cn.dmego.odsp.algorithms.calculateModel
 * describe: 线性规划问题
 * creat_user: Dmego
 * creat_date: 2018/12/17
 * creat_time: 19:49
 **/
@Data
public class LinearProgram {

    private double upLine;
    private double downLine;
    public int resultType;

    public int vari;
    public int cons;
    public int sType;
    public double[][] matrix;
    public double[] increments; //允许增量
    public double[] extremums; //极值
    public Map<Integer, Integer> ht;
    public double[][] target;
    public double[] xBest;
    public double zBest;
    public String log;
    public List<Map<String, String>> resultData;



    /**
     * 线性规划问题计算
     * @return
     */
    public boolean linearProgramCalculate(StrategyVo strategyVo){
        SimplexMethod simplexMethod = new SimplexMethod();
        simplexMethod.vari = strategyVo.getVariable(); vari = strategyVo.getVariable();
        simplexMethod.cons = strategyVo.getConstraint(); cons = strategyVo.getConstraint();
        simplexMethod.increments = strategyVo.getIncrements();
        simplexMethod.sType = strategyVo.getSType(); sType = strategyVo.getSType();
        simplexMethod.matrix = strategyVo.getMatrix();
        simplexMethod.extremums = strategyVo.getExtremums(); extremums = strategyVo.getExtremums();
        simplexMethod.direction = strategyVo.getDirection();
        if(simplexMethod.simplexMethodCalculate()) {
            resultType = simplexMethod.resultType;
            ht = simplexMethod.ht;
            increments = simplexMethod.increments;
            target = simplexMethod.target;
            if(resultType != 2){
                resultData = createResultData();//创建返回结果数据
                xBest = simplexMethod.xBest;
                zBest = simplexMethod.zBest;
            }
            return true;
        }
        log = simplexMethod.log;
        return false;
    }

    /**
     * 整数规划问题计算
     * @return
     */
    private boolean IntegerProgramCalculate(StrategyVo strategyVo){
        vari = strategyVo.getVariable();
        cons = strategyVo.getConstraint();
        matrix = strategyVo.getMatrix();
        increments = strategyVo.getIncrements();
        extremums = strategyVo.getExtremums();

        SimplexMethod simplexMethod = new SimplexMethod();
        simplexMethod.vari = strategyVo.getVariable();
        simplexMethod.cons = strategyVo.getConstraint();
        sType = strategyVo.getSType();
        double[] eextremums = new double[vari];
        int type;
        if(sType == 2){ //极小
            type = 1;
            for (int i = 0; i < vari; i++) {
                eextremums[i] = extremums[i]* -1.0;
            }
        }else{
            type = 1;
            for (int i = 0; i < vari; i++) {
                eextremums[i] = extremums[i];
            }
        }
        simplexMethod.extremums = eextremums;
        simplexMethod.sType = type;
        double[][] numArray1 = new double[cons][vari];
        for (int i = 0; i < cons; i++) {
            for (int j = 0; j < vari; j++) {
                numArray1[i][j] = matrix[i][j];
            }
        }
        simplexMethod.matrix = numArray1;
        double[] numArray2 = new double[increments.length];

//        simplexMethod.extremums = strategyVo.getExtremums(); extremums = strategyVo.getExtremums();
//        simplexMethod.direction = strategyVo.getDirection();






        return false;
    }

    /**
     * 线性规划问题返回结果数据
     * @return
     */
    private List<Map<String, String>> createResultData() {
        List<Map<String, String>> mapList = new ArrayList<>();
        double num1 = 0.0;
        //决策变量 最优解 目标系数 贡献
        for (int i = 0; i < vari; i++) {
            Map<String,String> map = new HashMap<>();
            String str1,str2,str3;
            double num2;
            if(ht.containsKey(i)){
                int tmp = ht.get(i);
                try {
                    String strt1 = String.valueOf(target[0][tmp]);
                    String strt2 = String.valueOf(target[0][tmp] * -1.0);
                    str2 = String.valueOf( CommonUtil.retainDecimal(increments[tmp],3));
                    str3 = (sType == 1) ? strt1: strt2;
                    num2 = (sType == 1) ? increments[tmp] * target[0][tmp] : increments[tmp] * target[0][tmp]*-1.0;
                }catch (Exception e){
                    str2 = "0";
                    str3 = String.valueOf(extremums[i]);
                    num2 = 0.0;
                }
            }else {
                str2 = "0";
                str3 = String.valueOf(extremums[i]);
                num2 = 0.0;
            }
            map.put("variable","X"+(i+1));
            map.put("best",str2);
            map.put("ratio",str3);
            map.put("de",CommonUtil.retainDecimal(num2,3)+"");
            mapList.add(map);
            num1 += num2;
        }
        Map<String,String> map = new HashMap<>();
        String strt = (sType == 1) ? "[Max.]=" : "[Min.]=";
        map.put("variable",strt);
        map.put("best","");
        map.put("ratio","");
        map.put("de",CommonUtil.retainDecimal(num1,3)+"");
        mapList.add(map);
        return mapList;
    }

}
