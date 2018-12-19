package cn.dmego.odsp.algorithms.calculateModel;

import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.StrategyVo;
import lombok.Data;
import java.util.*;

/**
 * class_name: ZeroOneProgram
 * package: cn.dmego.odsp.algorithms.calculateModel
 * describe: 0-1规划问题
 * creat_user: Dmego
 * creat_date: 2018/12/17
 * creat_time: 19:49
 **/
@Data
public class ZeroOneProgram {

    private double dm = 10000.0;
    public int vari;
    public int cons;
    public int sType;
    public double[][] matrix;
    public double[] extremums;
    public double[] increments;
    public Map<Integer, String> direction;
    public double[] xBest;
    public boolean hashBestValue;
    public String log;

    public List<Map<String, String>> resultData;
    public List<Map<String, String>> calculateResultData;


    public boolean zeroOneProgramCalculate(StrategyVo strategyVo) {

        vari = strategyVo.getVariable();
        cons = strategyVo.getConstraint();
        sType = strategyVo.getSType();
        extremums = strategyVo.getExtremums();
        matrix = strategyVo.getMatrix();
        direction = strategyVo.getDirection();
        increments = strategyVo.getIncrements();

        try {
            double[] numArray = new double[vari];
            List<String> pListChangeX = new ArrayList<>();
            Map<Double, String> map = new TreeMap<>(
                    new Comparator<Double>() {
                        public int compare(Double obj1, Double obj2) {
                            //升序排序
                            return obj1.compareTo(obj2);
                        }
                    });
            for (int i = 0; i < vari; i++) {
                double num1 = extremums[i];
                if (sType == 1) {//极大
                    double num2 = num1 * -1.0;
                    if (num2 < 0.0) {
                        numArray[i] = num2 * -1.0;
                        pListChangeX.add("x" + (i + 1));
                        map.put(num2 * -1.0, "x" + (i + 1));
                    } else {
                        numArray[i] = num2;
                        map.put(num2, "x" + (i + 1));
                    }
                } else if (num1 < 0.0) {
                    numArray[i] = num1 * -1.0;
                    pListChangeX.add("x" + (i + 1));
                    map.put(num1 * -1.0, "x" + (i + 1));
                } else {
                    numArray[i] = num1;
                    map.put(num1, "x" + (i + 1));
                }
            }

            String[] strArray = new String[map.size()];
            double[] keys = new double[map.size()];
            int index = 0;
            for (Map.Entry<Double, String> entry : map.entrySet()) {
                strArray[index] = entry.getValue();
                keys[index] = entry.getKey();
                index++;
            }

            strArray = CommonUtil.invertArray(strArray);
            keys = CommonUtil.invertArray(keys);

//            Collections.reverse(Arrays.asList(strArray));
//            Collections.reverse(Arrays.asList(keys));

            hashBestValue = false;
            xBest = new double[vari];
            double num3 = (sType == 1) ? 0.0 : dm;
            //序号 X1 X2 X3 X4 Z 是否满足约束条件:meet
            calculateResultData = new ArrayList<>();
            int num4 = 1;
            double z1 = 0.0;
            boolean validity = true;
            double[] xUserValue = new double[vari];
            double[] xValue = new double[vari];
            for (int i = 0; i < xValue.length; i++) {
                xValue[i] = 0.0;
            }
            Ref ref1 = new Ref(xUserValue, z1, validity);
            calculateTargetAndJudgeValidity(xValue, strArray, pListChangeX, ref1);
            xUserValue = ref1.xUserValue;
            z1 = ref1.z1;
            validity = ref1.validity;
            Map<String, String> row1 = new HashMap<>();
            row1.put("index", String.valueOf(num4));
            int num5 = num4 + 1;
            for (int i = 1; i < vari + 1; i++) {
                row1.put("X" + i, String.valueOf(xUserValue[i - 1]));
            }
            row1.put("Z", String.valueOf(z1));
            row1.put("meet", !validity ? "否" : "是");
            calculateResultData.add(row1);

            if (validity) {
                hashBestValue = true;
                if (sType == 2 && z1 < num3) {
                    num3 = z1;
                    xBest = xUserValue.clone();
                }
                if (sType == 1 && z1 > num3) {
                    num3 = z1;
                    xBest = xUserValue.clone();
                }
            }
            double z2;
            for (int index1 = 1; index1 <= xValue.length; index1++) {
                switch (index1) {
                    case 2:
                        int num1 = 0;
                        for (int index2 = xValue.length - 1; index2 >= 0 && index2 - index1 + 1 >= 0; index2--) {
                            xValue[index2] = 1.0;
                            boolean flag = true;
                            for (int index3 = 1; index3 < xValue.length; index3++) {
                                int num2 = 1;
                                for (int index4 = 0; index4 < xValue.length; index4++) {
                                    if (index4 != index2) {
                                        if (index2 - index3 < 0) {
                                            flag = false;
                                            break;
                                        }
                                        if (index4 == index2 - index3 && num2 == 1) {
                                            xValue[index4] = 1.0;
                                            num2++;
                                        } else {
                                            xValue[index4] = 0.0;
                                        }
                                    }
                                }
                                if (flag) {
                                    num1++;
                                    z2 = 0.0;
                                    Ref ref2 = new Ref(xUserValue, z2, validity);
                                    calculateTargetAndJudgeValidity(xValue, strArray, pListChangeX, ref2);
                                    xUserValue = ref2.xUserValue;
                                    z2 = ref2.z1;
                                    validity = ref2.validity;
                                    Map<String, String> row2 = new HashMap<>();
                                    row2.put("index", String.valueOf(num5));
                                    num5++;
                                    for (int i = 1; i < vari + 1; i++) {
                                        row2.put("X" + i, String.valueOf(xUserValue[i - 1]));
                                    }
                                    row2.put("Z", String.valueOf(z2));
                                    row2.put("meet", !validity ? "否" : "是");
                                    calculateResultData.add(row2);
                                    if (validity) {
                                        hashBestValue = true;
                                        if (sType == 2 && z2 < num3) {
                                            num3 = z2;
                                            xBest = xUserValue.clone();
                                        }
                                        if (sType == 1 && z2 > num3) {
                                            num3 = z2;
                                            xBest = xUserValue.clone();
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case 3:
                        int num6 = 0;
                        for (int index2 = xValue.length - 1; index2 >= 0 && index2 - index1 + 1 >= 0; index2--) {
                            xValue[index2] = 1.0;
                            for (int index3 = index2 - 1; index3 >= 0; index3--) {
                                xValue[index3] = 1.0;
                                for (int index4 = 1; index4 < xValue.length - 1; index4++) {
                                    int num2 = 1;
                                    boolean flag = true;
                                    for (int index5 = 0; index5 < xValue.length; index5++) {
                                        if (index5 != index3 && index5 != index2) {
                                            if (index3 - index4 < 0) {
                                                flag = false;
                                                break;
                                            }
                                            if (index5 == index3 - index4 && num2 == 1) {
                                                xValue[index5] = 1.0;
                                                num2++;
                                            } else {
                                                xValue[index5] = 0.0;
                                            }
                                        }
                                    }
                                    if (flag) {
                                        num6++;
                                        z2 = 0.0;
                                        Ref ref3 = new Ref(xUserValue, z2, validity);
                                        calculateTargetAndJudgeValidity(xValue, strArray, pListChangeX, ref3);
                                        xUserValue = ref3.xUserValue;
                                        z2 = ref3.z1;
                                        validity = ref3.validity;
                                        Map<String, String> row2 = new HashMap<>();
                                        row2.put("index", String.valueOf(num5));
                                        num5++;
                                        for (int i = 1; i < vari + 1; i++) {
                                            row2.put("X" + i, String.valueOf(xUserValue[i - 1]));
                                        }
                                        row2.put("Z", String.valueOf(z2));
                                        row2.put("meet", !validity ? "否" : "是");
                                        calculateResultData.add(row2);
                                        if (validity) {
                                            hashBestValue = true;
                                            if (sType == 2 && z2 < num3) {
                                                num3 = z2;
                                                xBest = xUserValue.clone();
                                            }
                                            if (sType == 1 && z2 > num3) {
                                                num3 = z2;
                                                xBest = xUserValue.clone();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        for (int index2 = xValue.length - 1; index2 >= 0 && index2 - index1 + 1 >= 0; index2--) {
                            for (int index3 = 0; index3 < xValue.length; index3++) {
                                xValue[index3] = index2 - index3 > index1 - 1 || index2 - index3 < 0 ? 0.0 : 1.0;
                            }
                            z2 = 0.0;
                            Ref ref4 = new Ref(xUserValue, z2, validity);
                            calculateTargetAndJudgeValidity(xValue, strArray, pListChangeX, ref4);
                            xUserValue = ref4.xUserValue;
                            z2 = ref4.z1;
                            validity = ref4.validity;
                            Map<String, String> row2 = new HashMap<>();
                            row2.put("index", String.valueOf(num5));
                            num5++;
                            for (int i = 1; i < vari + 1; i++) {
                                row2.put("X" + i, String.valueOf(xUserValue[i - 1]));
                            }
                            row2.put("Z", String.valueOf(z2));
                            row2.put("meet", !validity ? "否" : "是");
                            calculateResultData.add(row2);
                            if (validity) {
                                hashBestValue = true;
                                if (sType == 2 && z2 < num3) {
                                    num3 = z2;
                                    xBest = xUserValue.clone();
                                }
                                if (sType == 1 && z2 > num3) {
                                    num3 = z2;
                                    xBest = xUserValue.clone();
                                }
                            }
                        }
                        break;
                }
            }
            if (!hashBestValue) {
                int num1 = 1;
                StringBuilder stringBuilder = new StringBuilder();
                for (int index1 = 0; (double) index1 <= Math.pow(2.0, (double) vari) - 1.0; index1++) {
                    String str1 = Integer.toBinaryString(index1);
                    int index2 = 0;
                    char[] chars = str1.toCharArray();
                    for (char ch : chars) {
                        char[] chArray = new char[]{ch};
                        stringBuilder.delete(0, stringBuilder.length()); //清空
                        stringBuilder.append(chArray);
                        String str2 = stringBuilder.toString();
                        xValue[index2] = Double.valueOf(str2);
                        index2++;
                    }
                    z2 = 0.0;
                    Ref ref5 = new Ref(xUserValue, z2, validity);
                    calculateTargetAndJudgeValidity(xValue, strArray, pListChangeX, ref5);
                    xUserValue = ref5.xUserValue;
                    z2 = ref5.z1;
                    validity = ref5.validity;
                    Map<String, String> row2 = new HashMap<>();
                    row2.put("index", String.valueOf(num1));
                    num1++;
                    for (int i = 1; i < vari + 1; i++) {
                        row2.put("X" + i, String.valueOf(xUserValue[i - 1]));
                    }
                    row2.put("Z", String.valueOf(z2));
                    row2.put("meet", !validity ? "否" : "是");
                    calculateResultData.add(row2);
                    if (validity) {
                        hashBestValue = true;
                        if (sType == 2 && z2 < num3) {
                            num3 = z2;
                            xBest = xUserValue.clone();
                        }
                        if (sType == 1 && z2 > num3) {
                            num3 = z2;
                            xBest = xUserValue.clone();
                        }
                    }
                }
            }
            resultData = createResultData();
            return true;
        } catch (Exception e) {
            log = "计算错误:" + e.getMessage();
            return false;
        }
    }

    private List<Map<String, String>> createResultData() {
        List<Map<String, String>> mapList = new ArrayList<>();
        double num1 = 0.0;
        //决策变量 最优解 目标系数 贡献
        for (int i = 0; i < vari; i++) {
            Map<String, String> map = new HashMap<>();
            String str2 = String.valueOf(xBest[i]);
            String str3 = String.valueOf(extremums[i]);
            double num2 = xBest[i] * extremums[i];
            String str4 = String.valueOf(num2);
            map.put("variable", "X" + (i + 1));
            map.put("best", str2);
            map.put("ratio", str3);
            map.put("de", str4);
            mapList.add(map);
            num1 += num2;
        }
        Map<String, String> map = new HashMap<>();
        String strt = (sType == 1) ? "[Max.]=" : "[Min.]=";
        map.put("variable",strt);
        map.put("best","");
        map.put("ratio","");
        map.put("de",String.valueOf(num1));
        mapList.add(map);
        return mapList;
    }

    private void calculateTargetAndJudgeValidity(double[] xValue, String[] arrKey, List<String> pListChangeX, Ref ref) {
        double num1 = 0.0;
        for (int i = 0; i < vari; i++) {
            String str = "x" + (i + 1);
            for (int j = 0; j < arrKey.length; j++) {
                if (arrKey[j].equals(str)) {
                    num1 = xValue[j];
                    break;
                }
            }
            if (pListChangeX.contains(str)) {
                num1 = 1.0 - num1;
            }
            ref.xUserValue[i] = num1;
        }
        for (int i = 0; i < vari; i++) {
            double num2 = ref.xUserValue[i];
            double num3 = extremums[i];
            ref.z1 += num2 * num3;
        }
        boolean flag = true;
        for (int i = 0; i < cons; i++) {
            String str = direction.get(i);
            double num2 = increments[i];
            double num3 = 0.0;
            for (int j = 0; j < vari; j++) {
                double num4 = ref.xUserValue[j];
                double num5 = matrix[i][j];
                num3 += num4 * num5;
            }
            flag = !(str.equals(">=")) ? (!(str.equals("<=")) ? num3 == num2 : num3 <= num2) : num3 >= num2;
            if (!flag) {
                break;
            }
        }
        ref.validity = flag;
    }

    @Data
    private class Ref {
        public double[] xUserValue;
        public double z1;
        public boolean validity;

        public Ref(double[] xUserValue, double z1, boolean validity) {
            this.xUserValue = xUserValue;
            this.z1 = z1;
            this.validity = validity;
        }
    }


}
