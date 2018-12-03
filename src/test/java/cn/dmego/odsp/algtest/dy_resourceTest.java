package cn.dmego.odsp.algtest;

public class dy_resourceTest {


    public static void main(String[] args) {

        int m = 6; //总资源数
        int n = 3; //项目数

//        int[][] g = {
//                {0, 0, 0, 0, 0, 0},
//                {0, 8, 15, 30, 38},
//                {0, 9, 20, 35, 40},
//                {0, 10, 28, 35, 43}
//        };

//        int[][] g = {
//                {0, 0, 0, 0, 0, 0},
//                {0, 0, 8, 0, 15, 0, 30, 0, 38},
//                {0, 0, 9, 0, 20, 0, 35, 0, 40},
//                {0, 0, 10, 0, 28, 0, 35, 0, 43}
//        };


        int[][] g = {
                {0, 0,  0, 0, 0, 0, 0,  0,  0},
                {0, 8, 15, 0, 0, 00, 0,  0, 38},
                {0, 9, 20, 0, 0, 35, 0,  0, 40},
                {0, 10,28, 0, 0, 35, 0,  0, 43}
        };


        int[] r = {1, 2, 3, 8}; //可选投资策略

        resources(m, n, g);
    }

    /**
     * @param m 总资源数
     * @param n 项目个数
     * @param g g[i][j] 表示第 i 个工程 被分配 j 资源对应的利润表
     */
    public static void resources(int m, int n, int[][] g) {

        int[][] f = new int[n + 1][m + 1]; //f[i][j]表示前 i 个 项目 分配了 j 资源后的最大利润值
        int[][] d = new int[n + 1][m + 1]; //d[i][j]存储的是 f[i][j] 已经求出的情况下对应的第 i 项的资源分配额度
        int[] q = new int[n + 1]; //q[i]代表第i个工程被分配的资源额度，也就是说q数组保存最优解对应的每项工程的分配额度

        //进行初始化工作
        for (int j = 0; j <= m; j++) {
            f[1][j] = g[1][j]; //表示前 1 个 项目分配 j 资源后的最大利润表
            d[1][j] = j;
        }

        //从第 2 个 项目开始进行计算最优解
        for (int i = 2; i <= n; i++) {
            //自底向上遍历所有分配情况
            for (int j = 0; j <= m; j++) {
                f[i][j] = 0;//初始化 最大利润值为 0
                //遍历前一项工程的分配 k 的组合利润值
                for (int k = 0; k <= j; k++) {
                    int max = g[i][k] + f[i - 1][j - k];//计算前一项工程(j-k)资源是最大利润+本项K资源的数值
                    if (max > f[i][j]) {
                        f[i][j] = max;
                        d[i][j] = k;
                    }
                }
            }
        }

        int s = m;
        q[n] = d[n][m]; //保存计算到最后一个工程的最优分配额度
        for (int i = n - 1; i > 0; i--) { //初始化 i = n-1,从倒数第二项开始向前遍历查找最优解
            s = s - q[i + 1]; //前一项最优解的分配总额度
            q[i] = d[i][s]; //记录当前工程被分配的资源数
        }

        System.out.println("最大利润:" + f[n][m]);
        for (int i = 1; i <= n; i++) {
            System.out.println("第" + i + "个工程分配资源数为" + q[i]);
        }


    }


}
