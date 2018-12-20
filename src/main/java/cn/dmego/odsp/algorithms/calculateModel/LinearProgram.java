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
 * describe: 线性规划问题计算模型
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
    public Map<Integer, String> direction;
    public Map<Integer, Integer> ht;
    public double[][] target;
    public double[] xBest;
    public double zBest;
    public double[] xIntBest;
    public double zIntBest;
    public String log;
    public List<Map<String, String>> resultData;


    /**
     * 线性规划问题计算
     *
     * @return
     */
    public boolean linearProgramCalculate(StrategyVo strategyVo) {
        try {
            SimplexMethod simplexMethod = new SimplexMethod();
            simplexMethod.vari = strategyVo.getVariable();
            vari = strategyVo.getVariable();
            simplexMethod.cons = strategyVo.getConstraint();
            cons = strategyVo.getConstraint();
            simplexMethod.increments = strategyVo.getIncrements();
            simplexMethod.sType = strategyVo.getSType();
            sType = strategyVo.getSType();
            simplexMethod.matrix = strategyVo.getMatrix();
            simplexMethod.extremums = strategyVo.getExtremums();
            extremums = strategyVo.getExtremums();
            simplexMethod.direction = strategyVo.getDirection();
            if (simplexMethod.simplexMethodCalculate()) {
                resultType = simplexMethod.resultType;
                ht = simplexMethod.ht;
                increments = simplexMethod.increments;
                target = simplexMethod.target;
                if (resultType != 2) {
                    resultData = createResultData();//创建返回结果数据
                    xBest = simplexMethod.xBest;
                    zBest = simplexMethod.zBest;
                }
                return true;
            }
            log = simplexMethod.log;
            return false;
        } catch (Exception ex) {
            log = ex.getMessage();
            return false;
        }
    }

    /**
     * 整数规划问题计算
     *
     * @return
     */
    public boolean IntegerProgramCalculate(StrategyVo strategyVo) {
        try {
            vari = strategyVo.getVariable();
            cons = strategyVo.getConstraint();
            matrix = strategyVo.getMatrix();
            increments = strategyVo.getIncrements();
            extremums = strategyVo.getExtremums();
            direction = strategyVo.getDirection();
            sType = strategyVo.getSType();

            SimplexMethod simplexMethod = new SimplexMethod();
            simplexMethod.vari = strategyVo.getVariable();
            simplexMethod.cons = strategyVo.getConstraint();
            double[][] numArray1 = new double[cons][vari];
            for (int i = 0; i < cons; i++) {
                for (int j = 0; j < vari; j++) {
                    numArray1[i][j] = matrix[i][j];
                }
            }
            simplexMethod.matrix = numArray1;
            double[] numArray2 = new double[increments.length];
            for (int i = 0; i < increments.length; i++) {
                numArray2[i] = increments[i];
            }
            simplexMethod.increments = numArray2;
            Map<Integer, String> hashmap = new HashMap<>();
            for (Map.Entry<Integer, String> entry : direction.entrySet()) {
                hashmap.put(entry.getKey(), entry.getValue());
            }
            simplexMethod.direction = hashmap;
            double[] eextremums = new double[vari];
            int type;
            if (sType == 2) { //极小
                type = 1;
                for (int i = 0; i < vari; i++) {
                    eextremums[i] = extremums[i] * -1.0;
                }
            } else {
                type = 1;
                for (int i = 0; i < vari; i++) {
                    eextremums[i] = extremums[i];
                }
            }
            simplexMethod.extremums = eextremums;
            simplexMethod.sType = type;
            //计算
            if (!simplexMethod.simplexMethodCalculate()) {
                return false;
            }
            resultType = simplexMethod.resultType;
            if (resultType != 2) {
                xBest = simplexMethod.xBest;
                zBest = simplexMethod.zBest;
                if (zBest < 0.0) {
                    zBest *= -1.0;
                }
                upLine = zBest;
                downLine = 0.0;
                iPFenZhiCalculate(vari, cons, extremums, matrix, increments, sType, direction, xBest, zBest);
            }
            resultData = createIPResultData();
            resultType = resultData == null || resultData.size() <= 0 ? 2 : 1;
            return true;
        } catch (Exception ex) {
            log = ex.getMessage();
            return false;
        }
    }


    private List<Map<String, String>> createIPResultData() {
        List<Map<String, String>> mapList = new ArrayList<>();
        double num1 = 0.0;
        //决策变量 最优解 目标系数 贡献
        for (int i = 0; i < vari; i++) {
            Map<String, String> map = new HashMap<>();
            double num2 = xIntBest[i];
            String str2 = String.valueOf((int) num2);
            double num3 = extremums[i];
            String str3 = String.valueOf((int) num3);
            double num4 = num2 * num3;

            map.put("variable", "X" + (i + 1));
            map.put("best", str2);
            map.put("ratio", str3);
            map.put("de", CommonUtil.retainDecimal(num4, 3) + "");
            mapList.add(map);
            if (i == 0) {
                num1 = num4;
            } else {
                num1 += num4;
            }
        }
        Map<String, String> map = new HashMap<>();
        String strt = (sType == 1) ? "[Max.]=" : "[Min.]=";
        map.put("variable", strt);
        map.put("best", "");
        map.put("ratio", "");
        map.put("de", CommonUtil.retainDecimal(num1, 3) + "");
        mapList.add(map);
        return mapList;
    }

    private void iPFenZhiCalculate(int vari, int cons, double[] extremums, double[][] matrix, double[] increments, int sType, Map<Integer, String> direction, double[] xBest, double zBest) {
        if (judgeIfAllInt(xBest)) {
            if (zBest < 0.0) {
                zBest *= -1.0;
                if (downLine == 0.0) {
                    downLine = zBest;
                    xIntBest = xBest;
                    zIntBest = zBest;
                    return;
                }
                if (zBest < downLine) {
                    downLine = zBest;
                    xIntBest = xBest;
                    zIntBest = zBest;
                    return;
                }
            } else if (zBest > downLine) {
                downLine = zBest;
                xIntBest = xBest;
                zIntBest = zBest;
                return;
            }
        } else if (zBest < 0.0) {
            zBest *= -1.0;
            if (zBest > downLine) {
                return;
            }
            if (zBest < upLine) {
                upLine = zBest;
            }
        } else {
            if (zBest < downLine) {
                return;
            }
            if (zBest > upLine) {
                upLine = zBest;
            }
        }
        for (int i = 0; i < xBest.length; i++) {
            double num1 = xBest[i];
            //如果num1 不是整数并且不为 0
            if (num1 != 0.0 && !judgeIfInt(num1)) {
                cons++;
                double num2 = Math.floor(num1);
                double[][] matrix1 = new double[cons][vari];
                try {
                    for (int j = 0; j < cons; j++) {
                        for (int k = 0; k < vari; k++) {
                            matrix1[j][k] = (j != cons - 1) ? matrix[j][k] : (k != i ? 0.0 : 1.0);
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
                double[] increments1 = new double[increments.length + 1];
                for (int j = 0; j < increments.length + 1; j++) {
                    increments1[j] = (j != increments.length) ? increments[j] : num2;
                }
                Map<Integer, String> direction1 = new HashMap<>();
                for (Map.Entry<Integer, String> entry : direction.entrySet()) {
                    direction1.put(entry.getKey(), entry.getValue());
                }
                direction1.put(direction.size(), "<=");
                double[] xBest1 = new double[vari];
                double zBest1 = 0.0;
                Ref ref1 = new Ref(xBest1, zBest1);
                if (doLPCalculate(vari, cons, extremums, matrix1, increments1, sType, direction1, ref1) != 2) {
                    xBest1 = ref1.xxBest;
                    zBest1 = ref1.zzBest;
                    iPFenZhiCalculate(vari, cons, extremums, matrix1, increments1, sType, direction1, xBest1, zBest1);
                }
                double num3 = Math.ceil(num1);
                double[] increments2 = new double[increments.length + 1];
                for (int j = 0; j < increments.length + 1; j++) {
                    increments2[j] = (j != increments.length) ? increments[j] : num3;
                }
                Map<Integer, String> direction2 = new HashMap<>();
                for (Map.Entry<Integer, String> entry : direction.entrySet()) {
                    direction2.put(entry.getKey(), entry.getValue());
                }
                direction2.put(direction.size(), ">=");
                double[] xBest2 = new double[vari];
                double zBest2 = 0.0;
                Ref ref2 = new Ref(xBest2, zBest2);
                if (doLPCalculate(vari, cons, extremums, matrix1, increments2, sType, direction2, ref2) != 2) {
                    xBest2 = ref2.xxBest;
                    zBest2 = ref2.zzBest;
                    iPFenZhiCalculate(vari, cons, extremums, matrix1, increments2, sType, direction2, xBest2, zBest2);
                }
            }
        }
    }

    private int doLPCalculate(int vari, int cons, double[] extremums, double[][] mmatrix, double[] iincrements, int sType, Map<Integer, String> ddirection, Ref ref) {
        SimplexMethod simplexMethod = new SimplexMethod();
        simplexMethod.vari = vari;
        simplexMethod.cons = cons;
        simplexMethod.sType = sType;
        simplexMethod.extremums = extremums;

        double[] numArray1 = new double[iincrements.length];
        for (int i = 0; i < iincrements.length; i++) {
            numArray1[i] = iincrements[i];
        }
        simplexMethod.increments = numArray1;
        double[][] numArray2 = new double[cons][vari];
        for (int i = 0; i < cons; i++) {
            for (int j = 0; j < vari; j++) {
                numArray2[i][j] = mmatrix[i][j];
            }
        }
        simplexMethod.matrix = numArray2;

        Map<Integer, String> hashmap = new HashMap<>();
        for (Map.Entry<Integer, String> entry : ddirection.entrySet()) {
            hashmap.put(entry.getKey(), entry.getValue());
        }
        simplexMethod.direction = hashmap;

        if (!simplexMethod.simplexMethodCalculate()) {
            return 2;
        }
        resultType = simplexMethod.resultType;
        if (resultType != 2) {
            ref.xxBest = simplexMethod.xBest;
            ref.zzBest = simplexMethod.zBest;
        }
        return simplexMethod.resultType;
    }

    /**
     * 线性规划问题返回结果数据
     *
     * @return
     */
    private List<Map<String, String>> createResultData() {
        List<Map<String, String>> mapList = new ArrayList<>();
        double num1 = 0.0;
        //决策变量 最优解 目标系数 贡献
        for (int i = 0; i < vari; i++) {
            Map<String, String> map = new HashMap<>();
            String str1, str2, str3;
            double num2;
            if (ht.containsKey(i)) {
                int tmp = ht.get(i);
                try {
                    String strt1 = String.valueOf(target[0][tmp]);
                    String strt2 = String.valueOf(target[0][tmp] * -1.0);
                    str2 = String.valueOf(CommonUtil.retainDecimal(increments[tmp], 3));
                    str3 = (sType == 1) ? strt1 : strt2;
                    num2 = (sType == 1) ? increments[tmp] * target[0][tmp] : increments[tmp] * target[0][tmp] * -1.0;
                } catch (Exception e) {
                    str2 = "0";
                    str3 = String.valueOf(extremums[i]);
                    num2 = 0.0;
                }
            } else {
                str2 = "0";
                str3 = String.valueOf(extremums[i]);
                num2 = 0.0;
            }
            map.put("variable", "X" + (i + 1));
            map.put("best", str2);
            map.put("ratio", str3);
            map.put("de", CommonUtil.retainDecimal(num2, 3) + "");
            mapList.add(map);
            num1 += num2;
        }
        Map<String, String> map = new HashMap<>();
        String strt = (sType == 1) ? "[Max.]=" : "[Min.]=";
        map.put("variable", strt);
        map.put("best", "");
        map.put("ratio", "");
        map.put("de", CommonUtil.retainDecimal(num1, 3) + "");
        mapList.add(map);
        return mapList;
    }


    /**
     * 判断数组中是否所有都是int值
     */
    private boolean judgeIfAllInt(double[] xBest) {
        for (int i = 0; i < xBest.length; i++) {
            //如果有一个不是整数,返回false
            if (!(((int) xBest[i] * 1000) == (int) (xBest[i] * 1000))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个double 类型的数是否为整数
     */
    private boolean judgeIfInt(double x) {
        return ((int) x * 1000) == (int) (x * 1000);
    }


    @Data
    private class Ref {
        public double[] xxBest;
        public double zzBest;

        public Ref(double[] xxBest, double zzBest) {
            this.xxBest = xxBest;
            this.zzBest = zzBest;
        }
    }
}
