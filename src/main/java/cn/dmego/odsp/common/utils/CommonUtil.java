package cn.dmego.odsp.common.utils;

import cn.dmego.odsp.algorithms.vo.DecisionVo;
import cn.dmego.odsp.common.JsonResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

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
     * 将 json 字符串转化成二维数组
     * @param riskVo
     * @return
     */
    public static Double[][] jsonToArray(DecisionVo riskVo){
        String jsonStr = riskVo.getRatioTableData(); //json 字符串
        int action = riskVo.getAction(); //行动方案数,真实的行数需要再+1,因为第一行是自然状态概率
        int state = riskVo.getState(); //自然状态数, 真实的列数
        //先定义一个 二维数组
        Double tableData[][] = new Double[action+1][state];
        //将 JSON 字符串转成矩阵数组
        JSONArray array = JSONObject.parseArray(jsonStr);
        for (int i = 0; i < array.size(); i++) {
            JSONObject jo = array.getJSONObject(i);
            Double tempArr[] = new Double[state];
            for (int j = 1; j <=state ; j++) {
                Double temp = jo.getDouble(j+"_state");
                tempArr[j-1] = temp;
            }
            tableData[i] = tempArr;
        }
        return tableData;
    }

    /**
     * 查看结果集大小，设置返回 code 参数
     * @param calculate
     * @return
     */
    public static JsonResult retState(JsonResult calculate){
        if(calculate.size() > 0 && calculate != null){
            calculate.setCode(200);
            calculate.setMessage("计算成功!");
        }else{
            calculate.setCode(500);
            calculate.setMessage("计算失败!");
        }
        return calculate;
    }

    /**
     * Double 类型保留 scale 位小数
     * @param value
     * @return
     */
    public static Double retainDecimal(Double value,int scale){
        return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 返回参数中的最大值
     * @param matrix
     * @param maxMar
     * @param flag
     * @return
     */
    public static String returnMax( Double[][] matrix,Double[] maxMar, Integer row, Integer col, int flag){
        Integer name = flag;
        Double max1 = matrix[1][1];
        Double max2 = maxMar[1];
        if (name == 0){
            row = row;
        }else if (name == 1){
            row = row+1;
        }
        for (int i = 0; i < row; i++) {
            if(maxMar[i] != null && max2 < maxMar[i]){
                max2 = maxMar[i];
            }
            for (int j = 0; j < col; j++) {
                if(matrix[i][j] != null && max1 < matrix[i][j]){
                    max1 = matrix[i][j];
                }
            }
        }
        Double max = max1 >= max2 ? max1 : max2;
        return String.valueOf(max);
    }
}
