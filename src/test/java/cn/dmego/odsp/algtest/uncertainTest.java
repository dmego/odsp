package cn.dmego.odsp.algtest;

/**
 * class_name: uncertainTest
 * package: cn.dmego.odsp.algtest
 * describe: 不确定型决策算法测试
 * creat_user: Dmego
 * creat_date: 2018/11/4
 * creat_time: 15:24
 **/
import com.alibaba.fastjson.JSONArray;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 不确定型决策算法测试
 *
 */
public class uncertainTest {

    private static int row = 5; //行
    private static int col = 5; //列

    private static double a = 1.0/3.0; //乐观系数

    //决策矩阵
    private static double[][] matrix = {
       //事件   1  2   3   4   5    策略
            {  0, 0,  0,  0,  0},  //1
            {-10,50, 50, 50, 50},  //2
            {-20,40,100,100,100},  //3
            {-30,30, 90,150,150},  //4
            {-40,20, 80,140,200}   //5
    };

    //决策矩阵
//    private static double[][] matrix = {
//       //事件   1  2   3   4   5    策略
//            {140,130,100, 80, 90},  //1
//            {200,300,250,180,178},  //2
//            {340,320,231,233,320},  //3
//            {400,308,320,310,412},  //4
//            {420,429,478,430,380}   //5
//    };

    @Test
    public void test(){
//        maxMax(matrix,row,col);
        maxMin(matrix,row,col);
//        laplace(matrix,row,col);
//        savage(matrix,row,col);
//        eclecticism(matrix,row,col,a);
    }

    /**
     * 悲观主义决策
     * 最小里选最大
     * 思路：先算出每一行里的最小值，然后求里面最大的值
     */
    @Test
    public void maxMin(){
       double[] maxMar = new double[5];
        for (int i = 0; i < 5; i++) {
            double min = matrix[i][0]; //让第一个最小
            for (int j = 1; j < 5; j++) {
                if(matrix[i][j] < min){
                    min = matrix[i][j];
                }
            }
            maxMar[i] = min;
        }
        System.out.println(Arrays.toString(maxMar));
        double max = maxMar[0];
        for (int i = 0; i < 5; i++) {
            if(maxMar[i] > max){
                max = maxMar[i];
            }
        }
        System.out.println("悲观决策结果："+max);
    }

    /**
     * 悲观主义决策
     * @matrix 决策矩阵
     * @row 决策矩阵行数
     * @col 决策矩阵列数
     */
    public static void maxMin(double[][] matrix, int row, int col){
        double[] maxMar = new double[row];
        for (int i = 0; i < row; i++) {
            double min = matrix[i][0]; //让第一个最小
            for (int j = 1; j < col; j++) {
                if(matrix[i][j] < min){
                    min = matrix[i][j];
                }
            }
            maxMar[i] = min;
        }
        double max = maxMar[0];
        int maxFlag = 0;
        for (int i = 0; i < row; i++) {
            if(maxMar[i] > max){
                max = maxMar[i];
                maxFlag = i;
            }
        }
        //将矩阵数组转为json字符串
        List<Map<String,String>> mapList = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            Map<String,String> map = new HashMap<>();
            for (int j = 0; j < col; j++) {
                map.put(j+"state", String.valueOf(matrix[i][j]));
            }
            if(i == maxFlag){
                map.put("result", String.valueOf(maxMar[i])+"(最优方案)");
            }else{
                map.put("result", String.valueOf(maxMar[i]));
            }
            mapList.add(map);
        }

        System.out.println(mapList.toArray().toString());

    }

    /**
     * 乐观主义决策
     * 最大里选最大
     */
    @Test
    public void maxMax(){
        double[] maxMar = new double[5];
        for (int i = 0; i < 5; i++) {
            double max = matrix[i][0]; //让第一个最大
            for (int j = 1; j < 5; j++) {
                if(matrix[i][j] > max){
                    max = matrix[i][j];
                }
            }
            maxMar[i] = max;
        }
        System.out.println(Arrays.toString(maxMar));
        double max = maxMar[0];
        for (int i = 0; i < 5; i++) {
            if(maxMar[i] > max){
                max = maxMar[i];
            }
        }
        System.out.println("乐观决策结果："+max);
    }

    /**
     * 乐观主义决策
     * @matrix 决策矩阵
     * @row 决策矩阵行数
     * @col 决策矩阵列数
     */
    public static void maxMax(double[][] matrix, int row, int col){
        double[] maxMar = new double[row];
        for (int i = 0; i < row; i++) {
            double max = matrix[i][0]; //让第一个最大
            for (int j = 1; j < 5; j++) {
                if(matrix[i][j] > max){
                    max = matrix[i][j];
                }
            }
            maxMar[i] = max;
        }
        System.out.println(Arrays.toString(maxMar));
        double max = maxMar[0];
        for (int i = 0; i < row; i++) {
            if(maxMar[i] > max){
                max = maxMar[i];
            }
        }
        System.out.println("乐观主义决策结果："+max);
    }



    /**
     * 等概率准则决策
     * 先求每一行的平均值，然后求最大值
     */
    @Test
    public void laplace(){
        double[] maxMar = new double[5];
        for (int i = 0; i < 5; i++) {
            double sum = 0;
            for (int j = 0; j < 5; j++) {
               sum += matrix[i][j];
            }
            maxMar[i] = sum / 5;
        }
        System.out.println(Arrays.toString(maxMar));
        double max = maxMar[0];
        for (int i = 0; i < 5; i++) {
            if(maxMar[i] > max){
                max = maxMar[i];
            }
        }
        System.out.println(" 等概率准则决策结果："+max);
    }
    /**
     * 等概率准则决策
     * @matrix 决策矩阵
     * @row 决策矩阵行数
     * @col 决策矩阵列数
     */
    public static void laplace(double[][] matrix, int row, int col){
        double[] maxMar = new double[row];
        for (int i = 0; i < row; i++) {
            double sum = 0;
            for (int j = 0; j < col; j++) {
                sum += matrix[i][j];
            }
            maxMar[i] = sum / col;
        }
        System.out.println(Arrays.toString(maxMar));
        double max = maxMar[0];
        for (int i = 0; i < row; i++) {
            if(maxMar[i] > max){
                max = maxMar[i];
            }
        }
        System.out.println("等概率准则决策结果："+max);
    }

    /**
     *最小机会损失决策准则
     * 当某一个事件发生的时候，由于决策者没有选用收益最大的策略，形成损失值
     * 先求损失矩阵
     * 然后求每一个策略的最大机会损失值
     * 最后选取其中的最小值
     *
     * 思路：先求出每一列中的最大值，然后用这个最大值减去该列中的其他值，作为损失矩阵里同位置的新值
     * 然后求出 每损失矩阵中的每一行的最大值
     * 最后求其中的最小值
     */
    @Test
    public void savage(){
        //损失矩阵
        double[][] loss = new double[5][5];
        for (int j = 0; j < 5; j++) {
            double max = matrix[0][j]; //先定每一列的第一个最大
            for (int i = 1; i < 5; i++) {
                if(matrix[i][j] > max){
                    max = matrix[i][j];
                }
            }
            //此时已经求出该列的最大值
            //损失矩阵中对应位置的值 = 决策矩阵中列最大值 - 决策矩阵中对应位置值
            for (int i = 0; i < 5; i++) {
                loss[i][j] = max - matrix[i][j];
            }
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(Arrays.toString(loss[i]));
        }

        //此时损失矩阵已经求出
        double[] maxMar = new double[5];
        for (int i = 0; i < 5; i++) {
            double max = loss[i][0]; //让第一个最大
            for (int j = 1; j < 5; j++) {
                if(loss[i][j] > max){
                    max = loss[i][j];
                }
            }
            maxMar[i] = max;
        }
        System.out.println(Arrays.toString(maxMar));
        double min = maxMar[0];
        for (int i = 0; i < 5; i++) {
            if(maxMar[i] < min){
                min = maxMar[i];
            }
        }
        System.out.println("最小机会损失决策结果："+min);
    }

    /**
     * 最小机会损失决策
     * @matrix 决策矩阵
     * @row 决策矩阵行数
     * @col 决策矩阵列数
     */
    public static void savage(double[][] matrix, int row, int col){
        //损失矩阵
        double[][] loss = new double[row][col];
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
        double[] maxMar = new double[row];
        for (int i = 0; i < row; i++) {
            double max = loss[i][0]; //让第一个最大
            for (int j = 1; j < col; j++) {
                if(loss[i][j] > max){
                    max = loss[i][j];
                }
            }
            maxMar[i] = max;
        }
        System.out.println(Arrays.toString(maxMar));
        double min = maxMar[0];
        for (int i = 0; i < row; i++) {
            if(maxMar[i] < min){
                min = maxMar[i];
            }
        }
        System.out.println("最小机会损失决策结果："+min);
    }

    /**
     * 折中主义准则决策
     * 乐观系数:a (0< a < 1)
     * 公式: Hi = a * ai(max) + (1-a) * ai(min)
     * 按照上公式求出每一行的Hi
     * 最后求其中最大值
     *
     * 思路:先求出每一行的最大值和最小值
     *
     */
    @Test
    public void eclecticism(){

        double[] eclMar = new double[5];

        for (int i = 0; i < 5; i++) {
            double max = matrix[i][0]; //让第一个最大
            double min = matrix[i][0]; //让第一个最小
            for (int j = 1; j < 5; j++) {
                if(matrix[i][j] > max){
                    max = matrix[i][j];
                }
                if(matrix[i][j] < min){
                    min = matrix[i][j];
                }
            }
            //对运算结果四舍五入,保留两位小数
            eclMar[i] = new BigDecimal(a*max + (1-a) * min).setScale(2, RoundingMode.UP).doubleValue();
        }
        System.out.println(Arrays.toString(eclMar));
        double max = eclMar[0];
        for (int i = 0; i < 5; i++) {
            if(eclMar[i] > max){
                max = eclMar[i];
            }
        }
        System.out.println("折中主义准则决策结果："+max);
    }

    /**
     * 折中主义决策
     * @matrix 决策矩阵
     * @row 决策矩阵行数
     * @col 决策矩阵列数
     * @a 乐观系数
     */
    public static void eclecticism(double[][] matrix, int row, int col,double a){
        double[] H = new double[row];
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
            H[i] = new BigDecimal(a*max + (1-a) * min).setScale(2, RoundingMode.UP).doubleValue();
        }
        System.out.println(Arrays.toString(H));
        double max = H[0];
        for (int i = 0; i < row; i++) {
            if(H[i] > max){
                max = H[i];
            }
        }
        System.out.println("折中主义准则决策结果："+max);
    }

}
