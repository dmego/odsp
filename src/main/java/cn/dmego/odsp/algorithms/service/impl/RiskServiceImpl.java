package cn.dmego.odsp.algorithms.service.impl;

import cn.dmego.odsp.common.JsonResult;
import cn.dmego.odsp.common.utils.CommonUtil;
import cn.dmego.odsp.algorithms.service.RiskService;
import cn.dmego.odsp.algorithms.vo.DecisionVo;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * class_name: RiskServiceImpl
 * package: cn.dmego.odsp.decision.service.impl
 * describe: 风险决策法 Service 接口实现类
 * creat_user: Dmego
 * creat_date: 2018/11/12
 * creat_time: 13:59
 **/
@Service
public class RiskServiceImpl implements RiskService {

    @Override
    public JsonResult calculate(DecisionVo riskVo) {
        List<Integer> funs = riskVo.getFunctions(); //方法List
        Integer row = riskVo.getAction(); // 行数,实际比其多1
        Integer col = riskVo.getState(); //列数
        Double t = riskVo.getUtility(); //效用曲线系数
        Double[][] matrix = riskVo.getMatrix();

        JsonResult jsonResult = new JsonResult();
        for (int i = 0; i < funs.size(); i++) {
            if(funs.get(i) == 1){ //EMV
                List<Map<String, String>> emvMaps = EMV(riskVo,matrix,row,col);
                jsonResult.put("1",emvMaps);
            }else if(funs.get(i) == 2){ //EOL
                List<Map<String, String>> eolMaps = EOL(riskVo,matrix,row,col);
                jsonResult.put("2",eolMaps);
            }else if(funs.get(i) == 3){ //EVPI
                List<Map<String, String>> evpiMaps = EVPI(riskVo,matrix,row,col);
                jsonResult.put("3",evpiMaps);
            }else if(funs.get(i) == 4){ //EUV
                List<Map<String, String>> euvMaps = EUV(riskVo,matrix,t,row,col);
                jsonResult.put("4",euvMaps);
            }
        }

        return jsonResult;
    }

    /**
     * 将矩阵数组转成List<Map>
     * @param riskVo
     * @param matrix
     * @param row
     * @param col
     * @return
     */
    private static List<Map<String, String>> arrayToListMap(DecisionVo riskVo, Double[][] matrix, Integer row, Integer col,String name) {

        Integer bestFlag = riskVo.getBestFlag();  //最优方案的下标
        Double[] bestArr = riskVo.getBestArr(); //最优方案数组

        List<Map<String,String>> mapList = new ArrayList<>();
        for (int i = 0; i <= row; i++) {
            Map<String,String> map = new HashMap<>();
            if(i == 0){
                map.put("income","自然状态概率");
                map.put("result","");
                map.put("max",CommonUtil.returnMax(matrix,bestArr,row,col,1));
            }else{
                map.put("income","行动方案"+i);
                if(i == bestFlag){
                    //如果 except 与 value 都不为空
                    if(name.equals("EPVI")){
                        map.put("result",String.valueOf(bestArr[i]+"(最大期望收益)"));
                        map.put("except",String.valueOf(riskVo.getExpect()));
                        map.put("value",String.valueOf(riskVo.getValue()));
                    }else{
                        map.put("result",String.valueOf(bestArr[i]+"(最优方案)"));
                    }
                }else{
                    map.put("result", String.valueOf(bestArr[i]));
                }
            }
            for (int j = 0; j < col; j++) {
                map.put((j+1)+"_state",String.valueOf(matrix[i][j]));
            }
            mapList.add(map);
        }
        return mapList;
    }



    /**
     * 1
     * 最大期望收益决策准则(EMV)
     * @param matrix
     * @param row
     * @param col
     */
    private static List<Map<String,String>> EMV(DecisionVo riskVo, Double[][] matrix, Integer row, Integer col){
        Double[] maxMar = new Double[row+1]; //记录每种行动方案的结果，第一行是自然状态概率
        Double max = 0.0; //最优结果
        Double temp = 0.0;
        Double chance = 0.0; //概率
        Integer maxIndex = 0; //最优结果下标
        //因为第一行是自然状态概率，所以从第二行开始，实际的行数比 row 多一行
        for (int i = 1; i <= row; i++) {
            for (int j = 0; j < col; j++) {
                chance =  matrix[0][j]; //自然概率
                temp = (j != 0) ? (temp + matrix[i][j]*chance) : (matrix[i][j]*chance);
            }
            temp = CommonUtil.retainDecimal(temp,2);
            maxMar[i] = temp;
            if(i == 1){ // 当 i=1时，为 max 与 maxIndex 赋初值
                max = temp;
                maxIndex = 1;
            }else if(temp > max){
                max = temp;
                maxIndex = i;
            }
        }
        riskVo.setBestArr(maxMar);
        riskVo.setBestValue(max);
        riskVo.setBestFlag(maxIndex);

        return arrayToListMap(riskVo,matrix,row,col,"EMV");
    }


    /**
     * 2
     * 最小机会损失决策准则(EOL)
     * @param matrix
     * @param row
     * @param col
     */
    private static List<Map<String,String>> EOL(DecisionVo riskVo, Double[][] matrix, Integer row, Integer col){

        //先求损失矩阵
        Double[][] loss = new Double[row+1][col];
        for (int j = 0; j < col; j++) {
            loss[0][j] = matrix[0][j]; //将自然概率复制到损失矩阵的第一行中
            Double max = matrix[1][j]; //先定每一列的第一个最大
            for (int i = 1; i <= row; i++) {
                if(matrix[i][j] > max){
                    max = matrix[i][j];
                }
            }
            //此时已经求出该列的最大值
            //损失矩阵中对应位置的值 = 决策矩阵中列最大值 - 决策矩阵中对应位置值
            for (int i = 1; i <= row; i++) {
                loss[i][j] = max - matrix[i][j];
            }
        }

        //然后再求 EOL 决策
        Double[] maxMar = new Double[row+1];
        Double min = 0.0;
        Double temp = 0.0;
        Integer minIndex = 0;
        Double chance = 0.0;
        for (int i = 1; i <= row; i++) {
            for (int j = 0; j < col; j++) {
                chance =  loss[0][j]; //自然概率
                temp = (j != 0) ? (temp + loss[i][j]*chance) : (loss[i][j]*chance);
            }
            temp = CommonUtil.retainDecimal(temp,2);
            maxMar[i] = temp;
            if(i == 1){
                min = temp;
                minIndex = 1;
            } else if(temp < min){
                min = temp;
                minIndex = i;
            }
        }

        riskVo.setBestArr(maxMar);
        riskVo.setBestValue(min);
        riskVo.setBestFlag(minIndex);

        return arrayToListMap(riskVo,loss,row,col,"EOL");
    }


    /**
     * 3
     * 全情报价值(EVPI)
     * @param matrix
     * @param row
     * @param col
     */
    private static List<Map<String,String>> EVPI(DecisionVo riskVo, Double[][] matrix, Integer row, Integer col){

        EMV(riskVo,matrix,row,col); //先进行 EMV 决策法计算
        Double max = riskVo.getBestValue();

        Double rowMax = 0.0;
        Double expect = 0.0;
        Double chance = 0.0;

        //计算全情报价值收益
        for (int i = 0; i < col; i++) {
            chance =  matrix[0][i]; //自然概率
            for (int j = 1; j <= row; j++) {
                if(j == 1){ //初始化第一行第一个为该行最大的收益值
                    rowMax = matrix[j][i];
                }else if (matrix[j][i] > rowMax){
                    rowMax = matrix[j][i];
                }
            }
            expect += rowMax * chance;
        }
        riskVo.setExpect(CommonUtil.retainDecimal(expect,2)); //将全情报期望收益(保留两位小数)加入 Vo对象中
        riskVo.setValue(CommonUtil.retainDecimal(expect - max,2)); //将全情报价值EVPI（保留两位小数）加入 Vo 对象中

        return arrayToListMap(riskVo,matrix,row,col,"EPVI");

    }

    /**
     * 4
     * 效用曲线拟合
     * @param matrix
     * @param row
     * @param col
     */
    private static List<Map<String,String>> EUV(DecisionVo riskVo, Double[][] matrix, Double t, Integer row, Integer col){

        //先求出收益矩阵中的最大值与最小值
        Double min = matrix[1][0];
        Double max = matrix[1][0];
        for (int i = 2; i <= row; i++) {
            for (int j = 0; j < col; j++) {
                if(matrix[i][j] > max){
                    max = matrix[i][j];
                }
                if(matrix[i][j] < min){
                    min = matrix[i][j];
                }
            }
        }

        //然后求出效用值矩阵
        /*计算方法
           小于最小值(a) => 0
           大于最大值(b) => 1
           介于之间=> (x-a/b-a)^t,其中t为效用曲线系数
         */
        Double[][] avail = new Double[row+1][col];
        for (int i = 1; i <= row; i++) {
            for (int j = 0; j < col; j++) {
                avail[0][j] = matrix[0][j]; //将自然状态概率复制到第一行中
                if(matrix[i][j] <= min){
                    avail[i][j] = 0.0;
                }else if(matrix[i][j] >= max){
                    avail[i][j] = 1.0;
                }else {
                    Double pow = Math.pow((matrix[i][j] - min) / (max - min), t);
                    avail[i][j] = CommonUtil.retainDecimal(pow,2);
                }
            }
        }

        //再求期望效用值
        Double[] maxMar = new Double[row+1];
        Double availMax = 0.0; //效用期望最大值
        Integer maxIndex = 0;
        Double temp = 0.0;
        Double chance = 0.0;
        for (int i = 1; i <= row; i++) {
            for (int j = 0; j < col; j++) {
                chance =  avail[0][j]; //自然概率
                temp = (j != 0) ? (temp + avail[i][j]*chance) : (avail[i][j]*chance);
            }
            Double value = CommonUtil.retainDecimal(temp,3);
            maxMar[i] = value;
            if(i == 1){
                availMax = value;
                maxIndex = 1;
            } else if(value > availMax){
                availMax = value;
                maxIndex = i;
            }
        }

        riskVo.setBestArr(maxMar);
        riskVo.setBestValue(availMax);
        riskVo.setBestFlag(maxIndex);

        return arrayToListMap(riskVo,avail,row,col,"EUV");
    }


}
