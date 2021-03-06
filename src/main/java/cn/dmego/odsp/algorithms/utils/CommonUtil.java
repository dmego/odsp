package cn.dmego.odsp.algorithms.utils;

import cn.dmego.odsp.algorithms.model.EData;
import cn.dmego.odsp.algorithms.model.Graph;
import cn.dmego.odsp.algorithms.vo.*;
import cn.dmego.odsp.common.JsonResult;
import cn.dmego.odsp.common.utils.DateUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
     * 数据包络分析，对前台的json数据进行处理,方便后面计算
     * @param deaVo
     */
    public static void jsonToArray(DEAVo deaVo){
        String jsonStr = deaVo.getRatioTableData(); //json 字符串
        int dumNum = deaVo.getDumNum();
        int inputNum = deaVo.getInputNum();
        int outputNum = deaVo.getOutputNum();
        int varNum = inputNum + outputNum; //总指标数

        //先定义一个 二维数组
        double tableData[][] = new double[dumNum][varNum];
        //将 JSON 字符串转成矩阵数组
        JSONArray array = JSONObject.parseArray(jsonStr);
        for (int i = 0; i < array.size(); i++) {
            JSONObject jo = array.getJSONObject(i);
            double tempArr[] = new double[varNum];
            for (int j = 1; j <= varNum; j++) {
                if(j <= inputNum){
                    Double temp = jo.getDouble("input_"+j);
                    tempArr[j - 1] = temp;
                }else {
                    Double temp = jo.getDouble("output_"+(j-inputNum));
                    tempArr[j - 1] = temp;
                }
            }
            tableData[i] = tempArr;
        }
        deaVo.setMatrix(tableData);
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
     * Int 类型保留 scale 位小数
     *
     * @param value
     * @return
     */
    public static Double retainDecimal(Integer value, int scale) {
        return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 返回一个int数组最大值的下标
     */
    public static int bigValueIndex(int[] data){
        int big = data[0];
        for (int i = 1; i < data.length; i++) {
            if(data[i] > big){
                return i;
            }
        }
        return 0;
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
     * 矩阵转置
     */
    public static double[][] transpose(double[][] matrix){
        int line = matrix.length;
        int list = matrix[0].length;
        double[][] trans = new double[list][line];
        for (int i = 0; i < line; i++) {
            for (int j = 0; j < list; j++) {
                trans[j][i] = matrix[i][j];
            }
        }
        return trans;
    }

    /**
     * 矩阵转置 String
     */
    public static String[][] transpose(String[][] matrix){
        int line = matrix.length;
        int list = matrix[0].length;
        String[][] trans = new String[list][line];
        for (int i = 0; i < line; i++) {
            for (int j = 0; j < list; j++) {
                trans[j][i] = matrix[i][j];
            }
        }
        return trans;
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

    /**
     * 返回二维数组中每一行或列得最大值
     *  0 行
     *  1 列
     */
    public static double[] getMax(double[][] arr, int a) {
        double[] maxcol = null;
        if(a == 0){
            maxcol = new double[arr.length];
            for (int i = 0; i < arr.length; i++) {
                double max = arr[i][0];
                for (int j = 0; j < arr[0].length; j++) {
                    if(arr[i][j] > max){
                        max = arr[i][j];
                    }
                }
                maxcol[i] = max;
            }
        }else if(a == 1){
            maxcol = new double[arr[0].length];
            for (int i = 0; i < arr[0].length; i++) {
                double max = arr[0][i];
                for (int j = 0; j < arr.length; j++) {
                    if(arr[j][i] > max){
                        max = arr[j][i];
                    }
                }
                maxcol[i] = max;
            }
        }
        return maxcol;
    }


    /**
     * 行业解决方案模板文件下载
     * @param filePath
     * @param fileName
     */
    public static void downLoadTempFile(String filePath, String fileName, HttpServletResponse response) {
        File file = new File(filePath);
        if(file.exists()){
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                String name = new String(fileName.getBytes("utf-8"), "iso8859-1");
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName="+name+".xlsx");// 设置文件名

                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1){
                    os.write(buffer,0,i);
                    i = bis.read(buffer);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(bis != null){
                    try {
                        bis.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                if(fis != null){
                    try {
                        fis.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 将上传的文件保存到指定文件夹下
     */
    public static String saveFile(String filePath, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //获取文件名
        String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
        //获取时间戳
        String stamp = DateUtil.getStamp();
        //重新生成文件名
        fileName = prefixName + "_" + stamp + suffixName;

        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath + fileName);
            out.write(file.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件上传失败!");
            return null;
        }
        return fileName;
    }

    /**
     * 读取Excel sheet1 并返回数据
     */
    public static List<Object> easyExcel(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            return EasyExcelFactory.read(inputStream, new Sheet(1, 0));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("解析Execl 文件出错");
            return null;
        }
    }

    /**
     * 读取Excel sheet1 并返回数据
     */
    public static List<Object> easyExcel(String filePath,int sheetNo) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            return EasyExcelFactory.read(inputStream, new Sheet(sheetNo, 0));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("解析Execl 文件出错");
            return null;
        }
    }
}
