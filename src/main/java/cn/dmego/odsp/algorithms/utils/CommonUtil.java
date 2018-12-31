package cn.dmego.odsp.algorithms.utils;

import cn.dmego.odsp.algorithms.model.EData;
import cn.dmego.odsp.algorithms.model.Graph;
import cn.dmego.odsp.algorithms.vo.*;
import cn.dmego.odsp.common.JsonResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * class_name: CommonUtil
 * package: cn.dmego.odsp.common.utils
 * describe: 公用的 工具类
 * creat_user: Dmego
 * creat_date: 2018/11/14
 * creat_time: 18:11
 **/
public class CommonUtil {

    /**
     * 将 json 字符串转化成二维数组 决策分析问题
     *
     * @param decisionVo
     * @return
     */
    public static Double[][] jsonToArray(DecisionVo decisionVo) {
        String jsonStr = decisionVo.getRatioTableData(); //json 字符串
        int action = decisionVo.getAction(); //行动方案数,真实的行数需要再+1,因为第一行是自然状态概率
        int state = decisionVo.getState(); //自然状态数, 真实的列数
        //先定义一个 二维数组
        Double tableData[][] = new Double[action + 1][state];
        //将 JSON 字符串转成矩阵数组
        JSONArray array = JSONObject.parseArray(jsonStr);
        for (int i = 0; i < array.size(); i++) {
            JSONObject jo = array.getJSONObject(i);
            Double tempArr[] = new Double[state];
            for (int j = 1; j <= state; j++) {
                Double temp = jo.getDouble(j + "_state");
                tempArr[j - 1] = temp;
            }
            tableData[i] = tempArr;
        }
        return tableData;
    }

    /**
     * 将 json 字符串转化成二维数组 动态规划问题
     *
     * @param dynamicVo
     */
    public static void jsonToArray(DynamicVo dynamicVo) {
        String jsonStr = dynamicVo.getRatioTableData(); //json 字符串
        //根据调用不同的方法,设置不同的一维数组
        Integer fun = dynamicVo.getFun();

        //将 JSON 字符串转成矩阵数组
        JSONArray array = JSONObject.parseArray(jsonStr);

        if (fun == 1) {//如果是背包问题,那么需要
            Integer packFun = dynamicVo.getPackFun();
            Integer kBreedNum = dynamicVo.getKBreedNum(); //物品品种数
            Integer kVolume = dynamicVo.getKVolume(); //背包容量

            String[] packNames = new String[kBreedNum];
            Integer[] weights = new Integer[kBreedNum + 1];
            Integer[] values = new Integer[kBreedNum + 1];
            Integer[] limits = new Integer[kBreedNum + 1];

            weights[0] = 0;
            values[0] = 0;
            limits[0] = 0;
            for (int i = 1; i <= array.size(); i++) {
                JSONObject jo = array.getJSONObject(i - 1);
                packNames[i - 1] = jo.getString("name");
                weights[i] = jo.getInteger("weight");
                values[i] = jo.getInteger("value");
                if (packFun == 2) {//如果是多重背包
                    limits[i] = jo.getInteger("limit");
                }
            }

            dynamicVo.setWeights(weights);
            dynamicVo.setValues(values);
            dynamicVo.setLimit(limits);
            dynamicVo.setPackNames(packNames);

        } else if (fun == 2) { //如果是资源分配问题
            Integer ri = dynamicVo.getRItemNum(); //项目数
            Integer rs = dynamicVo.getRStrategy(); //可选投资策略数
            Integer[] strategy = new Integer[rs]; //可选投资策略数组
            Integer[][] matrix = new Integer[ri][rs]; //矩阵数据数组

            //为可选投资策略数组赋值
            for (int i = 0; i < array.size(); i++) {
                JSONObject jo = array.getJSONObject(i);
                strategy[i] = jo.getInteger("income");
            }

            //为矩阵数据数组赋值
            for (int i = 0; i < ri; i++) {
                for (int j = 0; j < rs; j++) {
                    JSONObject jo = array.getJSONObject(j);
                    matrix[i][j] = jo.getInteger((i + 1) + "_project");
                }
            }

            dynamicVo.setStrategy(strategy);
            dynamicVo.setMatrix(matrix);

        } else if (fun == 3) { //如果是生产与存储问题
            Integer n = dynamicVo.getPStage();//生产时期(阶段数)
            Integer[] demand = new Integer[n];
            Integer[] productPower = new Integer[n];
            Double[] unitProductCost = new Double[n];
            Double[] unitStorageCost = new Double[n];
            Double[] fixedProductCost = new Double[n];
            Integer[] maxStorage = new Integer[n];

            for (int i = 0; i < array.size(); i++) {
                JSONObject jo = array.getJSONObject(i);
                demand[i] = jo.getInteger("need");
                productPower[i] = jo.getInteger("ability");
                unitProductCost[i] = jo.getDouble("proCost");
                unitStorageCost[i] = jo.getDouble("stoCost");
                fixedProductCost[i] = jo.getDouble("fixedPCost");
                maxStorage[i] = jo.getInteger("storage");
            }

            dynamicVo.setDemand(demand);
            dynamicVo.setProductPower(productPower);
            dynamicVo.setUnitProductCost(unitProductCost);
            dynamicVo.setUnitStorageCost(unitStorageCost);
            dynamicVo.setFixedProductCost(fixedProductCost);
            dynamicVo.setMaxStorage(maxStorage);
        }
    }

    /**
     * 图与网络分析
     * 将从前台传递过来的参数转换为graph对象,用作后面计算使用
     * @param graphVo
     */
    public static void jsonToGraph(String[] vexsArr, GraphVo graphVo) {
        String jsonStr = graphVo.getRatioTableData();
        //根据调用不同的方法,设置不同的一维数组
        Integer fun = graphVo.getFun();
        //图的类型
        Integer gType = graphVo.getGType();
        //将 JSON 字符串转成矩阵数组
        JSONArray array = JSONObject.parseArray(jsonStr);
        EData[] edges = new EData[array.size()];
        if(fun == 1 || fun == 2){
            for (int i = 0; i < array.size(); i++) {
                JSONObject jo = array.getJSONObject(i);
                EData eData = new EData(jo.getString("start"),jo.getString("end"),jo.getDouble("weight"));
                edges[i] = eData;
            }
        }else if(fun == 3){
            //TODO 最大流问题
            return;
        }else if(fun == 4){
            //TODO 最小费用最大流问题
            return;
        }

        Graph graph = new Graph(vexsArr,edges,gType);
        graphVo.setGraph(graph);

    }

    /**
     * 决策规划,对前台的json数据进行处理,方便后面计算
     * @param strategyVo
     */
    public static void jsonToArray(StrategyVo strategyVo) {

        String jsonStr = strategyVo.getRatioTableData(); //json 字符串
        Integer vari = strategyVo.getVariable(); //决策变量
        Integer cons = strategyVo.getConstraint(); //约束条件格式

        double[] extremum = new double[vari]; //极值
        double[] increments = new double[cons]; //允许增量数组
        double[][] matrix = new double[cons][vari]; //矩阵数据数组[约束][决策]
        Map<Integer,String> directions = new HashMap<>(); //方向map
        
        //将 JSON 字符串转成矩阵数组
        JSONArray array = JSONObject.parseArray(jsonStr);

        //第一行为极值
        JSONObject joex = array.getJSONObject(0);
        for (int i = 1; i <= vari; i++) {
            extremum[i-1] = joex.getDouble(i+"_x");
        }
        //从第二行开始为矩阵数据和方向,允许增量
        for (int i = 1; i < array.size(); i++) { //约束
            JSONObject jo = array.getJSONObject(i);
            for (int j = 1; j <= vari; j++) {//决策
                matrix[i-1][j-1] = jo.getDouble(j+"_x");
            }
            directions.put(i-1,jo.getString("direction"));
            increments[i-1] = jo.getDouble("increment");

        }
        //将数据存入Vo对象中
        strategyVo.setExtremums(extremum);
        strategyVo.setIncrements(increments);
        strategyVo.setMatrix(matrix);
        strategyVo.setDirection(directions);
    }

    /**
     * 运输问题，对前台的json数据进行处理,方便后面计算
     * @param transportVo
     */
    public static void jsonToArray(TransportVo transportVo) {
        String jsonStr = transportVo.getRatioTableData(); //json 字符串
        Integer origin = transportVo.getOriginNum(); //A 产地
        Integer sale = transportVo.getSalesNum(); //B 销地

         double[] output = new double[origin]; //产量
         double[] sales = new double[sale]; //销量
         double[][] price = new double[origin][sale];
         double[][] price2 = new double[origin][sale];
         double[][] plan = new double[origin][sale];

        //将 JSON 字符串转成矩阵数组
        JSONArray array = JSONObject.parseArray(jsonStr);


        //最后一行是销量
        JSONObject josa = array.getJSONObject(array.size() - 1);
        for (int i = 0; i < sale; i++) {
            sales[i] = josa.getDouble("B"+(i+1));
        }
        for (int i = 0; i < array.size() - 1; i++) {
            JSONObject jo = array.getJSONObject(i);
            for (int j = 0; j < sale; j++) {
                price[i][j] = jo.getDouble("B"+(j+1));
                price2[i][j] = jo.getDouble("B"+(j+1));
                plan[i][j] = 0.0;
            }
            output[i] = jo.getDouble("output");
        }

        transportVo.setOutput(output);
        transportVo.setSales(sales);
        transportVo.setPrice(price);
        transportVo.setPrice2(price2);
        transportVo.setPlan(plan);
    }

    /**
     * 设置返回json中的 code 参数
     *
     * @param calculate
     * @return
     */
    public static JsonResult retState(JsonResult calculate,int code) {
        if (code == 200 && calculate.size() > 0 && calculate != null) {
            calculate.setCode(200);
            calculate.setMessage("计算成功!");
        } else{
            calculate.setCode(500);
            calculate.setMessage("计算失败!");
        }
        return calculate;
    }

    /**
     * 设置返回json中的 code 参数
     *
     * @param calculate
     * @return
     */
    public static JsonResult retState(JsonResult calculate, String msg, int code) {
        if (code == 200 && calculate.size() > 0 && calculate != null) {
            calculate.setCode(200);
            calculate.setMessage(msg+"成功!");
        } else{
            calculate.setCode(500);
            calculate.setMessage(msg+"失败!");
        }
        return calculate;
    }

    /**
     * Double 类型保留 scale 位小数
     *
     * @param value
     * @return
     */
    public static Double retainDecimal(Double value, int scale) {
        return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 返回参数中的最大值
     *
     * @param matrix
     * @param maxMar
     * @param flag
     * @return
     */
    public static String returnMax(Double[][] matrix, Double[] maxMar, Integer row, Integer col, int flag) {
        Integer name = flag;
        Double max1 = matrix[1][1];
        Double max2 = maxMar[1];
        if (name == 0) {
            row = row;
        } else if (name == 1) {
            row = row + 1;
        }
        for (int i = 0; i < row; i++) {
            if (maxMar[i] != null && max2 < maxMar[i]) {
                max2 = maxMar[i];
            }
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] != null && max1 < matrix[i][j]) {
                    max1 = matrix[i][j];
                }
            }
        }
        Double max = max1 >= max2 ? max1 : max2;
        return String.valueOf(max);
    }


    /**
     * 反转数组
     */
    public static <T> T invertArray(T array) {
        int len = Array.getLength(array);
        Class<?> classz = array.getClass().getComponentType();
        Object dest = Array.newInstance(classz, len);
        System.arraycopy(array, 0, dest, 0, len);
        Object temp;
        for (int i = 0; i < (len / 2); i++) {
            temp = Array.get(dest, i);
            Array.set(dest, i, Array.get(dest, len - i - 1));
            Array.set(dest, len - i - 1, temp);
        }
        return (T)dest;
    }

    /**
     * 返回二维数组中每一行得最大值
     */
    public static double[] getMax(double[][] arr){
        double[] maxcol = new double[arr.length];

        for (int i = 0; i < arr.length; i++) {
            double max = arr[i][0];
            for (int j = 0; j < arr[0].length; j++) {
                if(arr[i][j] > max){
                    max = arr[i][j];
                }
            }
            maxcol[i] = max;
        }
        return maxcol;
    }

}
