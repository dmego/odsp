package cn.dmego.odsp.algorithms.service.impl;

import cn.dmego.odsp.common.JsonResult;
import cn.dmego.odsp.common.utils.CommonUtil;
import cn.dmego.odsp.algorithms.service.UncertainService;
import cn.dmego.odsp.algorithms.vo.DecisionVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * class_name: UncertainServiceImpl
 * package: cn.dmego.odsp.decisionModels.service.impl
 * describe: 不确定型决策法 Service 接口实现类
 * creat_user: Dmego
 * creat_date: 2018/11/4
 * creat_time: 16:41
 **/
@Service
public class UncertainServiceImpl implements UncertainService {

    @Override
    public JsonResult calculate(DecisionVo uncertainVo) {
        List<Integer> funs = uncertainVo.getFunctions();
        Integer row = uncertainVo.getAction();
        Integer col = uncertainVo.getState();
        Double a = uncertainVo.getHopeful(); //乐观系数
        Double[][] matrix = uncertainVo.getMatrix();

        JsonResult jsonResult = new JsonResult();

        for (int i = 0; i < funs.size(); i++) {
            if(funs.get(i) == 1){ //悲观
                List<Map<String, String>> maxMinMaps = maxMin(uncertainVo, matrix, row, col);
                jsonResult.put("1",maxMinMaps);
            } else if(funs.get(i) == 2){ //乐观
                List<Map<String, String>> maxMaxMaps = maxMax(uncertainVo, matrix, row, col);
                jsonResult.put("2",maxMaxMaps);
            } else if(funs.get(i) == 3){ //最小机会
                List<Map<String, String>> savageMaps = savage(uncertainVo, matrix, row, col);
                jsonResult.put("3",savageMaps);
            } else if(funs.get(i) == 4){ //等概率
                List<Map<String, String>> laplaceMaps = laplace(uncertainVo, matrix, row, col);
                jsonResult.put("4",laplaceMaps);
            } else if(funs.get(i) == 5){ //折中
                List<Map<String, String>> eclecticismMaps = eclecticism(uncertainVo, matrix, row, col, a);
                jsonResult.put("5",eclecticismMaps);
            }
        }
        return jsonResult;
    }

    /**
     * 将数组转成List<Map>
     *
     * @return
     */
    private static List<Map<String,String>> arrayToListMap(DecisionVo uncertainVo, Double[][] matrix, Integer row, Integer col){

        Integer bestFlag = uncertainVo.getBestFlag();
        Double[] bestArr = uncertainVo.getBestArr();

        //将矩阵数组转为 List<Map<String,String>> 并返回
        List<Map<String,String>> mapList = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("income","行动方案"+(i+1));
            if(i == 0){
                map.put("max", CommonUtil.returnMax(matrix,bestArr,row,col,0));
            }
            for (int j = 0; j < col; j++) {
                map.put((j+1)+"_state", String.valueOf(matrix[i][j]));
            }
            if(i == bestFlag){
                map.put("result", String.valueOf(bestArr[i])+"(最优方案)");
            }else{
                map.put("result", String.valueOf(bestArr[i]));
            }
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 返回最佳方案的值与所在数组的下标
     * @param uncertainVo
     * @param fun 类型{max：数组中的最大值；min:数组中的最小值}
     */
    private static void calBestValue(DecisionVo uncertainVo, Integer row, Double[] bestArr, String fun){
        Double bestValue = bestArr[0];
        Integer bestFlag = 0;
        if(fun.equals("max")){
            for (int i = 0; i < row; i++) {
                if(bestArr[i] > bestValue){
                    bestValue = bestArr[i];
                    bestFlag = i;
                }
            }
        } else if(fun.equals("min")){

            for (int i = 0; i < row; i++) {
                if(bestArr[i] < bestValue){
                    bestValue = bestArr[i];
                    bestFlag = i;
                }
            }
        }
        uncertainVo.setBestValue(bestValue);
        uncertainVo.setBestFlag(bestFlag);
    }


    /**
     * 1
     * 悲观主义决策
     * @matrix 决策矩阵
     * @row 决策矩阵行数
     * @col 决策矩阵列数
     * @return 返回计算完成后的 json 数据
     */
    private static List<Map<String,String>> maxMin(DecisionVo uncertainVo, Double[][] matrix, Integer row, Integer col){
        Double[] maxMar = new Double[row];
        for (int i = 0; i < row; i++) {
            double min = matrix[i][0]; //让第一个最小
            for (int j = 1; j < col; j++) {
                if(matrix[i][j] < min){
                    min = matrix[i][j];
                }
            }
            maxMar[i] = min;
        }
        //将计算结果放入Vo对象中
        uncertainVo.setBestArr(maxMar);
        //计算最佳方案值与下标，放入Vo对象中
        calBestValue(uncertainVo,row,maxMar,"max");
        //将数组矩阵转为List<Map> 并返回
        return arrayToListMap(uncertainVo,matrix,row,col);
    }

    /**
     * 2
     * 乐观主义决策
     * @matrix 决策矩阵
     * @row 决策矩阵行数
     * @col 决策矩阵列数
     */
    private static List<Map<String,String>> maxMax(DecisionVo uncertainVo, Double[][] matrix, Integer row, Integer col){
        Double[] maxMar = new Double[row];
        for (int i = 0; i < row; i++) {
            double max = matrix[i][0]; //让第一个最大
            for (int j = 1; j < col; j++) {
                if(matrix[i][j] > max){
                    max = matrix[i][j];
                }
            }
            maxMar[i] = max;
        }
        //将计算结果放入Vo对象中
        uncertainVo.setBestArr(maxMar);
        //计算最佳方案值与下标，放入Vo对象中
        calBestValue(uncertainVo,row,maxMar,"max");
        //将数组矩阵转为List<Map> 并返回
        return arrayToListMap(uncertainVo,matrix,row,col);
    }

    /**
     * 3
     * 最小机会损失决策
     * @matrix 决策矩阵
     * @row 决策矩阵行数
     * @col 决策矩阵列数
     */
    private static List<Map<String,String>> savage(DecisionVo uncertainVo, Double[][] matrix, Integer row, Integer col){
        //损失矩阵
        Double[][] loss = new Double[row][col];
        for (int j = 0; j < col; j++) {
            double max = matrix[0][j]; //先定每一列的第一个最大
            for (int i = 1; i < row; i++) {
                if(matrix[i][j] > max){
                    max = matrix[i][j];
                }
            }
            //损失矩阵中对应位置的值 = 决策矩阵中列最大值 - 决策矩阵中对应位置值
            for (int i = 0; i < row; i++) {
                loss[i][j] = max - matrix[i][j];
            }
        }
        //此时损失矩阵已经求出
        Double[] maxMar = new Double[row];
        for (int i = 0; i < row; i++) {
            double max = loss[i][0];
            for (int j = 1; j < col; j++) {
                if(loss[i][j] > max){
                    max = loss[i][j];
                }
            }
            maxMar[i] = max;
        }

        //将计算结果放入Vo对象中
        uncertainVo.setBestArr(maxMar);
        //计算最佳方案值与下标，放入Vo对象中
        calBestValue(uncertainVo,row,maxMar,"min");
        //将数组矩阵转为List<Map> 并返回
        return arrayToListMap(uncertainVo,loss,row,col);
    }

    /**
     * 4
     * 等概率准则决策
     * @matrix 决策矩阵
     * @row 决策矩阵行数
     * @col 决策矩阵列数
     */
    private static List<Map<String,String>> laplace(DecisionVo uncertainVo, Double[][] matrix, Integer row, Integer col){
        Double[] maxMar = new Double[row];
        for (int i = 0; i < row; i++) {
            double sum = 0;
            for (int j = 0; j < col; j++) {
                sum += matrix[i][j];
            }
            maxMar[i] = new BigDecimal(sum / col).setScale(3, RoundingMode.UP).doubleValue();
        }

        //将计算结果放入Vo对象中
        uncertainVo.setBestArr(maxMar);
        //计算最佳方案值与下标，放入Vo对象中
        calBestValue(uncertainVo,row,maxMar,"max");
        //将数组矩阵转为List<Map> 并返回
        return arrayToListMap(uncertainVo,matrix,row,col);
    }

    /**
     * 5
     * 折中主义决策
     * @matrix 决策矩阵
     * @row 决策矩阵行数
     * @col 决策矩阵列数
     * @a 乐观系数
     */
    private static List<Map<String,String>> eclecticism(DecisionVo uncertainVo, Double[][] matrix, Integer row, Integer col, Double a){
        Double[] maxMar = new Double[row];
        for (int i = 0; i < row; i++) {
            double max = matrix[i][0]; //让第一个最大
            double min = matrix[i][0]; //让第一个最小
            for (int j = 1; j < col; j++) {
                if(matrix[i][j] > max){
                    max = matrix[i][j];
                }
                if(matrix[i][j] < min){
                    min = matrix[i][j];
                }
            }
            //对运算结果四舍五入,保留两位小数
            maxMar[i] = new BigDecimal(a*max + (1-a) * min).setScale(2, RoundingMode.UP).doubleValue();
        }

        //将计算结果放入Vo对象中
        uncertainVo.setBestArr(maxMar);
        //计算最佳方案值与下标，放入Vo对象中
        calBestValue(uncertainVo,row,maxMar,"max");
        //将数组矩阵转为List<Map> 并返回
        return arrayToListMap(uncertainVo,matrix,row,col);

    }

}
