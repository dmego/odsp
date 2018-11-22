package cn.dmego.odsp.algtest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * class_name: riskTest
 * package: cn.dmego.odsp.algtest
 * describe: 风险决策法算法测试
 * creat_user: Dmego
 * creat_date: 2018/11/13
 * creat_time: 17:40
 **/
public class riskTest {
    private static int row = 4; //行
    private static int col = 3; //列

    private static double t =2; //效用曲率系数

    //决策矩阵
    private static double[][] matrix = {
            //事件   1  2   3
            {0.2,0.5,0.3},
            {140,120, 80},  //2
            {200,150, 40},  //3
            {340,140,-20},  //4
    };

    public static void main(String[] args) {
        //EMV(matrix,row,col);
        //EOL(matrix,row,col);
//        EVPI(matrix,row,col);
        EUV(matrix,t,row,col);
    }



    /**
     * 最大期望收益决策准则(EMV)
     * @param matrix
     * @param row
     * @param col
     */
    private static void EMV(double[][] matrix, int row, int col){
        double[] maxMar = new double[row];
        double max = maxMar[1];
        int maxIndex = 1;
        for (int i = 1; i < row; i++) {
            double temp = 0.0;
            for (int j = 0; j < col; j++) {
                double ch =  matrix[0][j]; //自然概率
                temp = (j != 0) ? (temp + matrix[i][j]*ch) : (matrix[i][j]*ch);
            }
            maxMar[i] = temp;
            if(temp > max){
                max = temp;
                maxIndex = i;
            }
        }
        System.out.println(Arrays.toString(maxMar));
        System.out.println(max+"--"+maxIndex);
    }

    /**
     * 最小机会损失决策准则(EOL)
     * @param matrix
     * @param row
     * @param col
     */
    private static void EOL(double[][] matrix, int row, int col){
        //先求损失矩阵
        double[][] loss = new double[row][col];
        for (int j = 0; j < col; j++) {
            loss[0][j] = matrix[0][j]; //将自然概率复制到损失矩阵的第一行中
            double max = matrix[1][j]; //先定每一列的第一个最大
            for (int i = 1; i < row; i++) {
                if(matrix[i][j] > max){
                    max = matrix[i][j];
                }
            }
            //此时已经求出该列的最大值
            //损失矩阵中对应位置的值 = 决策矩阵中列最大值 - 决策矩阵中对应位置值
            for (int i = 1; i < row; i++) {
                loss[i][j] = max - matrix[i][j];
            }
        }
        for (int i = 0; i < row; i++) {
            System.out.println(Arrays.toString(loss[i]));
        }

        //然后再求 EOL 决策
        double[] maxMar = new double[row];
        double min = 0.0;
        int minIndex = 0;
        for (int i = 1; i < row; i++) {
            double temp = 0.0;
            for (int j = 0; j < col; j++) {
                double ch =  loss[0][j]; //自然概率
                temp = (j != 0) ? (temp + loss[i][j]*ch) : (loss[i][j]*ch);
            }
            maxMar[i] = temp;
            if(i == 1){
                min = temp;
                minIndex = 1;
            } else if(temp < min){
                min = temp;
                minIndex = i;
            }
        }
        System.out.println(Arrays.toString(maxMar));
        System.out.println(min+"--"+minIndex);
    }


    /**
     * 全情报价值(EVPI)
     * @param matrix
     * @param row
     * @param col
     */
    private static void EVPI(double[][] matrix, int row, int col){
        double[] maxMar = new double[row];
        double max = maxMar[1];
        int maxIndex = 1;
        for (int i = 1; i < row; i++) {
            double temp = 0.0;
            for (int j = 0; j < col; j++) {
                double ch =  matrix[0][j]; //自然概率
                temp = (j != 0) ? (temp + matrix[i][j]*ch) : (matrix[i][j]*ch);
            }
            maxMar[i] = temp;
            if(temp > max){
                max = temp;
                maxIndex = i;
            }
        }
        System.out.println(Arrays.toString(maxMar));
        System.out.println(max+"--"+maxIndex);

        double num3 = 0.0;
        double num5 = 0.0;
        double num6 = 0.0;
        for (int k = 0; k < col; k++) {
            double ch =  matrix[0][k]; //自然概率
            for (int l = 1; l < row; l++) {
                num3 = matrix[l][k];
                if(l == 1){
                    num5 = num3;
                }else if (num3 > num5){
                    num5 = num3;
                }
            }
            num6 += num5 * ch;
        }
        double num7 = num6 - max;
        System.out.println("全情报期望收益:"+num6);
        System.out.println("全情报价值EVPI:"+num7);
    }

    /**
     *  效用曲线拟合
     * @param matrix
     * @param row
     * @param col
     */
    private static void EUV(double[][] matrix, double a, int row, int col){
        //先求出收益矩阵中的最大值与最小值
        double min = matrix[1][0];
        double max = matrix[1][0];
        for (int i = 2; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if(matrix[i][j] > max){
                    max = matrix[i][j];
                }
                if(matrix[i][j] < min){
                    min = matrix[i][j];
                }
            }
        }
        System.out.println("min="+min+"max="+max);
        //然后求出效用值矩阵
        /*计算方法
           小于最小值(a) => 0
           大于最大值(b) => 1
           介于之间=> (x-a/b-a)^t,其中t为效用曲线系数
         */
        double[][] avail = new double[row][col];
        for (int i = 1; i < row; i++) {
            for (int j = 0; j < col; j++) {
                avail[0][j] = matrix[0][j];
                if(matrix[i][j] < min){
                    avail[i][j] = 0;
                }else if(matrix[i][j] > max){
                    avail[i][j] = 1;
                }else {
                    System.out.println((matrix[i][j] - min) / (max - min));
                    double pow = Math.pow((matrix[i][j] - min) / (max - min), a);
                    System.out.println(pow);
                    double value = new BigDecimal(pow).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    avail[i][j] = value;
                }
            }
        }

        for (int i = 0; i < row; i++) {
            System.out.println(Arrays.toString(avail[i]));
        }

        //再求期望效用值
        double[] maxMar = new double[row];
        double max2 = 0.0;
        int maxIndex = 0;
        for (int i = 1; i < row; i++) {
            double temp = 0.0;
            for (int j = 0; j < col; j++) {
                double ch =  avail[0][j]; //自然概率
                temp = (j != 0) ? (temp + avail[i][j]*ch) : (avail[i][j]*ch);
            }
            double value = new BigDecimal(temp).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            maxMar[i] = value;
            if(i == 1){
                max2 = value;
                maxIndex = 1;
            } else if(value > max2){
                max2 = value;
                maxIndex = i;
            }
        }
        System.out.println(Arrays.toString(maxMar));
        System.out.println(max2+"--"+maxIndex);

    }

}


