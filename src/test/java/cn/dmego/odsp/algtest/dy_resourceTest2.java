package cn.dmego.odsp.algtest;

import java.util.Arrays;

public class dy_resourceTest2 {

    public static void main(String[] args) {

        int W = 10; //总资源数
        int N = 3; //项目数

        int[][] g = {
                {8, 15, 30, 38},
                {9, 20, 35, 40},
                {10, 28, 35, 43}
        };

//        int[][] g = {
//                {0, 0, 0, 0, 0, 0},
//                {0, 0, 8, 0, 15, 0, 30, 0, 38},
//                {0, 0, 9, 0, 20, 0, 35, 0, 40},
//                {0, 0, 10, 0, 28, 0, 35, 0, 43}
//        };


//        int[][] g = {
//                {8, 15, 0, 0, 30, 0,  0, 38},
//                {9, 20, 0, 0, 35, 0,  0, 40},
//                {10,28, 0, 0, 35, 0,  0, 43}
//        };


        int[] r = {2, 4, 6, 8}; //可选投资策略


        resources(W, N, r, g);
    }

    /**
     *
     * @param g g[i][j] 表示第 i 个工程 被分配 j 资源对应的利润表
     */
    public static void resources(int W,int N,int[] r,int[][] g) {

        //1.求可选投资策略数组中的最大值,该值为矩阵数组的列数
        int maxr = r[0];
        for (int i = 0; i < r.length; i++) {
            if (r[i] > maxr) {
                maxr = r[i];
            }
        }

        //2.构造一个新的数据二维数组
        int M = maxr > W ? maxr : W; //首先确定数组最大列
        int[][] gg = new int[N+1][M+1];

        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++) {
                for (int k = 0; k < r.length; k++) {
                    if(j == r[k]){
                        gg[i][j] = g[i-1][k];
                        break;
                    }
                }
            }
        }

        //3.开始计算
        int[][] p = new int[N+1][M+1]; //p[i][j] 存储 前 i 个项目 分配 j 资源能获得的最大收益
        int[][] d = new int[N+1][M+1]; //d[i][j] 存储 第 i 个项目获得最大收益时分配的资源数

        for (int i = 0; i <= N; i++) {
            p[i][0] = 0; //初始化 当资源为0时,前i 个项目最大收益为 0
        }
        for (int i = 0; i <= M; i++) {
            p[0][i] = 0; //初始化 当 项目数为0时,所有分配资源的情况,最大收益都为 0
        }

        /*------------
            核心代码
        -------------*/
        //从第 1 个项目开始
        for (int i = 1; i <= N; i++) {
            //从分配 1 个资源开始
            for (int j = 1; j <= M; j++) {
                int max = p[i-1][j]; //初始化最大值为 前 i - 1 项的最大值,也就是第 i 个项目不投资(分配资源为 0),收益为 0 时
                d[i][j] = 0; //初始化 第 i 项 分配资源为 0

                //分配 k 资源给第 i 个项目,剩下 j - k 资源给前 i - 1 个项目
                for (int k = 1; k <= j; k++) {
                    int sum = gg[i][k] +  p[i - 1][j - k];
                    if(sum > max){
                        max = sum; //如果分配 j 资源给 i 项目能让前 i 个 项目利益最大,那么最大值为 sum
                        d[i][j] = k;
                    }
                }
                p[i][j] = max; //前 i 个项目 分配 j 资源后的最大收益值
            }
        }

        //4.逆序推出每个项目分配了多少资源
        int[] input = new int[N+1]; //投入资源数
        int[] income = new int[N+1]; //项目收益

        int i = N,j = M;
        while(i > 0 & j > 0){
            if(d[i][j] == 0){
                i--;
            }else {
                input[i] = d[i][j]; //记录 i 个项目投入的资源数
                income[i] = gg[i][d[i][j]]; //第 i 个项目的收益
                j -= d[i][j];
                i--;
            }
        }

        //5.根据总的的资源数,求剩余资源数组
        int[] restVolume = new int[N+1]; //剩余资源
        restVolume[0] = W;
        for (int k = 1; k <=N; k++) {
            restVolume[k] = restVolume[k-1] - input[k];
        }

        System.out.println(Arrays.toString(restVolume));

        System.out.println(1);
    }
}
