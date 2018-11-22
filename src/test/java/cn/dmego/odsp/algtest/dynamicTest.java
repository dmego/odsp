package cn.dmego.odsp.algtest;

import org.junit.Test;

/**
 * class_name: dynamicTest
 * package: cn.dmego.odsp.algtest
 * describe: 动态规划算法测试
 * creat_user: Dmego
 * creat_date: 2018/11/21
 * creat_time: 19:32
 **/
public class dynamicTest {
    /**
     * 背包问题
     * 有 N 件物品和一个容量为 V 的背包,第 i 件物品的占用空间为 c[i],价值为w[i], 求解将哪些物品放入背包可以使价值总和最大
     *
     *
     *  0-1背包
     *  多重背包
     *  完全背包
     */

    private static int[] A = {200,150,400,60,300}; //单位物品重量数组
    private static int[] V = {40,25,60,70,50}; //单位物品价值数组
    private static int N = 5; //物品种类
    private static int W = 800; //背包容量

    public static void main(String[] args) {
        System.out.println( knapsack02(W,A,V));
        //System.out.println( knapsack03(N,V,weights,values));
    }



    public static void knapsack01(int N,int V,int[] weights,int[] values){
        int[][] dp = new int[N+1][V+1];

        //依次对每个物品进行判断
//        for (int i = 1; i <= N; ++i) {
//            int w = weights[i-1];
//            int v = values[i-1];
//            //依次对容量的每个阶段进行判断
//            for (int j = V; j >=1 ; --j) {
//                //该阶段的背包容量可以满足该物品
//                if(j>=w){
//                    dp[i][j] = Math.max(dp[i-1][j],dp[i-1][j-w]+v);
//                }else{
//                    dp[i][j] = dp[i-1][j];
//                }
//            }
//        }
        System.out.println(dp[N][V]);

    }

    /**
     * 算法空间优化后的0-1背包问题
     * @param W 背包容量
     * @param A 物品重量数组
     * @param V 物品价值数组
     * @return
     */
    public static int knapsack02(int W,int[] A, int[] V){
        int[] dp = new int[W+1];
        boolean[] record = new boolean[A.length];
        //依次对每个物品进行判断
        for (int i = 0; i < A.length ; i++) {
            //依次对容量的每个阶段进行判断
            for (int j = W; j >= A[i]; j--) {
                //该阶段背包容量可以满足该物品
                record[i] = true;
                dp[j] = Math.max(dp[j],dp[j-A[i]]+V[i]);
            }
        }
        //倒推选择了哪些物品
        int j = W;
        for(int i = A.length - 1; i > 0; --i){
            //选择了第 i 个物品
            if(dp[j] == dp[j-A[i]]+V[i]){
                record[i] = true;
                j = j - V[i];
            }
        }
        for (int n= 0; n <record.length;n++){
            if(record[n]){
                System.out.print("1 ");
            }else{
                System.out.print("0 ");
            }
        }
        System.out.println();

        System.out.println( dp[W]);

        return dp[W];
    }


    /**
     *
     * @param N 物品种类数
     * @param V 背包容量
     * @param weights
     * @param values
     * @return
     */
    public static int knapsack03(int N,int V,int[] weights,int[] values){
        int[][] dp = new int[N+1][V+1];
        //依次对每个物品进行判断
        for (int i = 1; i <= N; ++i) {
            int w = weights[i-1];
            int v = values[i-1];
            //依次对容量的每个阶段进行判断
            for (int j = V; j >=1 ; --j) {
                //该阶段的背包容量可以满足该物品
                if(j>=w){
                    dp[i][j] = Math.max(dp[i-1][j],dp[i-1][j-w]+v);
                }else{
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        boolean[] record = new boolean[N];
        //倒推选择了哪些物品
        int i = N,j = V;
        for(; i >= 1; --i){
            int w = weights[i-1];
            int v = values[i-1];
            //选择了第 i 个物品
            if(dp[i][j] == dp[i-1][j-w]+v){
                record[i-1] = true;
                j = j - w;
            }
        }
        for (int n= 0; n <record.length;n++){
            if(record[n]){
                System.out.print("1 ");
            }else{
                System.out.print("0 ");
            }
        }
        System.out.println();

        return dp[N][V];

    }



}
