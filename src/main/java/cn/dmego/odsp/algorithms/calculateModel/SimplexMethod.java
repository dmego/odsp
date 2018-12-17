package cn.dmego.odsp.algorithms.calculateModel;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class_name: SimplexMethod
 * package: cn.dmego.odsp.algorithms.calculateModel
 * describe: 单纯形法求解线性规划问题
 * creat_user: Dmego
 * creat_date: 2018/12/17
 * creat_time: 19:49
 **/
@Data
public class SimplexMethod {

    private double dm = 10000.0;
    private int relaxation; //松弛变量
    private int artificial; //人工变量
    private int iteration; //迭代
    public int resultType;

    public int vari;
    public int cons;
    public int sType;
    public double[][] matrix;
    public double[][] matrixBig;
    public double[] extremums;
    public double[] extremumsBig;
    public double[] increments;
    public Map<Integer, String> direction;
    public Map<Integer, Integer> ht;
    public double[][] target;
    public double[] xBest;
    public double zBest;
    public String log;

    public boolean simplexMethodCalculate() {
        relaxation = 0;
        artificial = 0;
        try {
            //将线性规划问题化为标准形式
            for (int i = 0; i < cons; i++) {
                String str = direction.get(i); //方向
                double num = increments[i]; //允许增量
                if (num < 0.0) {
                    if (str.equals(">=")) {
                        direction.put(i, "<=");
                        relaxation++;
                    } else if (str.equals("<=")) {
                        direction.put(i, ">=");
                        relaxation++;
                        artificial++;
                    } else {
                        artificial++;
                    }
                    for (int j = 0; j < vari; j++) {
                        matrix[i][j] = matrix[i][j] * -1.0;
                    }
                    increments[i] = num * -1.0;
                } else if (str.equals(">=")) {
                    relaxation++;
                    artificial++;
                } else if (str.equals("<=")) {
                    relaxation++;
                } else {
                    artificial++;
                }
            }

            int mVari = vari;
            int mCons = cons;
            int length = mVari + relaxation + artificial;
            matrixBig = new double[mCons][length];
            int num1 = 0, num2 = 0;
            for (int i = 0; i < mCons; i++) {
                boolean flag1 = false, flag2 = false;
                String str = direction.get(i);
                for (int j = 0; j < length; j++) {
                    if (j < mVari) {
                        matrixBig[i][j] = matrix[i][j];
                    } else if (str.equals(">=")) {
                        if (j == vari + num1) {
                            if (!flag1) {
                                matrixBig[i][j] = -1.0;
                                flag1 = true;
                            } else {
                                matrixBig[i][j] = 0.0;
                            }
                        } else if (j == vari + relaxation + num2) {
                            if (!flag2) {
                                matrixBig[i][j] = 1.0;
                                flag2 = true;
                            } else {
                                matrixBig[i][j] = 0.0;
                            }
                        } else {
                            matrixBig[i][j] = 0.0;
                        }
                    } else if (str.equals("<=")) {
                        if (j == vari + num1) {
                            if (!flag1) {
                                matrixBig[i][j] = 1.0;
                                flag1 = true;
                            } else {
                                matrixBig[i][j] = 0.0;
                            }
                        } else if (j == vari + relaxation + num2) {
                            if (!flag2) {
                                matrixBig[i][j] = 1.0;
                                flag2 = true;
                            } else {
                                matrixBig[i][j] = 0.0;
                            }
                        } else {
                            matrixBig[i][j] = 0.0;
                        }
                    }
                }
                if (str.equals(">=")) {
                    num1++;
                    num2++;
                } else if (str.equals("<=")) {
                    num1++;
                } else {
                    num2++;
                }
            }

            extremumsBig = new double[vari + relaxation + artificial];
            for (int i = 0; i < vari; i++) {
                extremumsBig[i] = (sType == 1) ? extremums[i] : extremums[i] * -1.0; //1:极大值,2:极小值,
            }
            for (int i = vari; i < vari + relaxation; i++) {
                extremumsBig[i] = 0.0;
            }
            for (int i = vari + relaxation; i < vari + relaxation + artificial; i++) {
                extremumsBig[i] = dm * -1.0;
            }
            target = new double[1][cons];
            for (int i = 0; i < cons; i++) {
                target[0][i] = !(direction.get(i).equals("<=")) ? -1.0 * dm : 0.0;
            }
            ht = new HashMap<Integer, Integer>();
            for (int i = 0; i < vari; i++) {
                ht.put(i, vari + 100);
            }
            iteration = 0;
            DoCalculate(); //开始计算

            for (int i = 0; i < cons; i++) {
                if(target[0][i] == -1.0 * dm && increments[i] != 0.0){
                    resultType = 2;
                    break;
                }
            }
            if(resultType != 2){
                createFinalBestValue();
            }
            return true;
        } catch (Exception e) {
            log = "计算错误:" + e.getMessage();
        }
        return false;
    }

    private void DoCalculate() {
        int x = 0;
        int y = 0;
        boolean flag = true;
        int num1 = vari + relaxation + artificial;
        double[][] c = new double[1][1];
        double num2 = 0.0;
        for (int i = 0; i < num1; i++) {
            double[][] b = new double[cons][1];
            for (int j = 0; j < cons; j++) {
                b[j][0] = matrixBig[j][i];
            }
            MatrixMultiply(target, b, c);
            double num3 = c[0][0] - extremumsBig[i];
            if (num3 < 0.0) {
                flag = false;
            }
            if (i == 0) {
                x = i;
                num2 = num3;
            } else if (num3 < num2) {
                num2 = num3;
                x = i;
            }
        }
        if (flag) {
            return;
        }
        if (iteration > cons + artificial + relaxation) {
            resultType = 2;
        }else {
            double num3 = 0.0;
            for (int i = 0; i < cons; i++) {
                double num4;
                try {
                    num4 = increments[i] / matrixBig[i][x];
                    if(matrixBig[i][x] < 0.0){
                        num4 = -1.0;
                    }
                }catch (Exception e){
                    if(i == 0){
                        y = 0;
                        continue;
                    }
                    continue;
                }
                if(i == 0){
                    y = 0;
                    num3 = num4 >= 0.0 ? num4 : dm;
                }else if(num4 >= 0.0 && num4 < num3){
                    num3 = num4;
                    y = i;
                }
            }
            if(ht.containsValue(y)){
                int key = -1;
                for (int i = 0; i < ht.size(); i++) {
                    if(ht.get(i) == y){
                        key = i;
                    }
                }
                ht.remove(key);
            }
            ht.put(x,y);
            target[0][y] = extremumsBig[x];
            increments[y] = increments[y] / matrixBig[y][x];
            for (int i = 0; i < cons; i++) {
                if(i != y){
                    increments[i] = increments[i] - matrixBig[i][x] * increments[y];
                }
            }
            double[] numArray = new double[vari + relaxation + artificial];
            for (int i = 0; i < vari + relaxation + artificial; i++) {
                numArray[i] = matrixBig[y][i];
            }
            double num5 = matrixBig[y][x];
            double num6 = 0.0;
            for (int i = 0; i < cons; i++) {
                for (int j = 0; j < vari + relaxation + artificial; j++) {
                    if(i == y){
                        matrixBig[i][j] = matrixBig[i][j] / num5;
                    }else {
                        if(j == 0){
                            num6 = matrixBig[i][x];
                        }
                        matrixBig[i][j] = matrixBig[i][j] - num6 *(numArray[j] / num5);
                    }
                }
            }
            iteration++;
            DoCalculate();
        }
    }

    private void createFinalBestValue() {
        xBest = new double[vari];
        double num1 = 0.0;
        for (int i = 0; i < vari; i++) {
            double num2;
            double num3;
            if(ht.containsKey(i)){
                int tmp = ht.get(i);
                try{
                    num2 = increments[tmp];
                }catch (Exception e){
                    num2 = 0.0;
                }
                try {
                    num3 = (sType == 1) ? increments[tmp] * target[0][tmp] : increments[tmp] * target[0][tmp] * -1.0;
                }catch (Exception e){
                    num3 = 0.0;
                }
            }else {
                num2 = 0.0;
                num3 = 0.0;
            }
            xBest[i] = num2;
            num1 += num3;
        }
        zBest = num1;
    }

    private boolean MatrixMultiply(double[][] a, double[][] b, double[][] c) {
        if (a[0].length != b.length || a.length != c.length || b[0].length != c[0].length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                c[i][j] = 0.0;
                for (int k = 0; k < b.length; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return true;
    }
}