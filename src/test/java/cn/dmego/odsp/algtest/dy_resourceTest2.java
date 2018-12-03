package cn.dmego.odsp.algtest;

import java.util.Arrays;

public class dy_resourceTest2 {

    public static void main(String[] args) {

        int m = 10; //总资源数
        int n = 4; //项目数

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


        int[] r = {1, 2, 8, 3}; //可选投资策略

        int[][] arr = getArr(r, g);

        resources(n, r, arr);
    }

    /**
     * @param r
     * @param g
     * @return
     */
    private static int[][] getArr(int[] r, int[][] g) {
        int n = g.length;
        //求出 r 中的最大值
        int maxr = r[0];
        for (int i = 1; i < r.length; i++) {
            if (r[i] > maxr) {
                maxr = r[i];
            }
        }

        System.out.println(maxr);

        int[][] gg = new int[n][maxr];

        //int[] r = {1, 2, 8, 3};

        //从第一行开始
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < maxr; j++) {
                for (int k = 0; k < r.length; k++) {
                    int jj = r[k] - 1;
                    if (j == jj) {
                        gg[i][j] = g[i][k];
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < gg.length; i++) {
            System.out.println(Arrays.toString(gg[i]));
        }

        return gg;
    }


    /**
     *
     * @param n 项目个数
     * @param g g[i][j] 表示第 i 个工程 被分配 j 资源对应的利润表
     */
    public static void resources(int n, int[] r, int[][] g) {

        //求出 r 中的最大值
        int maxr = r[0];
        for (int i = 1; i < r.length; i++) {
            if (r[i] > maxr) {
                maxr = r[i];
            }
        }

        System.out.println(maxr);

        int[][] gg = new int[n][maxr];

        //int[] r = {1, 2, 8, 3};

        //从第一行开始
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < maxr; j++) {
                for (int k = 0; k < r.length; k++) {
                    int jj = r[k] - 1;
                    if (j == jj) {
                        gg[i][j] = g[i][k];
                        break;
                    }
                }
            }
        }

        int m = maxr + 1;


        int[][] p = new int[n][m];
        int[][] d = new int[n][m];

        for (int i = 0; i < n; i++) {
            p[i][0] = 0;
        }

        for (int i = 0; i < m; i++) {
            p[0][i] = 0;
        }

        for (int i = 1; i < n; i++) {

            for (int j = 1; j < m; j++) {
                int max = p[i - 1][j];
                d[i][j] = 0;
                for (int k = 1; k <= j; k++) {
                    int sum = g[i - 1][k - 1] + p[i - 1][j - k];
                    if (max < sum) {
                        max = sum;
                        d[i][j] = k;
                    }
                }
                p[i][j] = max;
            }
        }

        System.out.println("最大利润:" + p[n - 1][m - 1]);
        for (int i = n - 1, j = m - 1; i > 0 && j > 0; ) {
            if (d[i][j] == 0) {
                i--;
            } else {
                System.out.println("第" + i + "项工程分配资源:" + d[i][j]);
                j = j - d[i][j];
                i--;
            }
        }
    }
}
