package cn.dmego.odsp.algtest;

public class knapsackTest {
    private static int[] w = {0,200,150,400,60,300}; //单位物品重量数组
    private static int[] v = {0,40,25,60,70,50}; //单位物品价值数组
    private static int N = 5; //物品种类
    private static int W = 800; //背包容量

    public static void main(String[] args) {
        int[] ww =  {0,4,7,5};
        int[] vv = {0,5,9,6};

        int W = 800;
        int w[] = {0,250,150,400,350,300};
        int v[] = {0,40,25,60,70,50 };
        int cot[] = {0,15,10,10,20,12 };

//		zeroOnePack(W, N, w, v);
//		zeroOnePack(10, 3, ww, vv);
//		completePack(W, N, w, v);
        multiplePack(800,5,w,v,cot);
    }

    /**
     * 0-1背包问题 一维数组解法，记录路径
     * @param W
     * @param N
     * @param w
     * @param v
     */
    public static void zeroOnePack(int W,int N,int[] w,int[] v) {
        /* 选用 1 维数组来优化空间，当进行第  i 次循环时，所有的dp[0~W] 都还没被更新
         * 也就是说，dp[j] 还记录着前 i-1 个物品在容量为 j 时的最大价值
         * 这就相当于还记录着 dp[i -1][j]和dp[i - 1][j-w[i]]+v[i]
         *为了保证每一次更新时，dp[j] 的值都是 i-1 层循环时的值，必须倒序循环，
         *假设你这个时候第一个数是求的dp[0], 一直求到了第dp[10], 那么你这个时候再去调用dp[10-w[i]].
         *那么排在10前面的数肯定已经被第 i 层的循环动过了，而不再是 i-1 层循环时的数。
         *
         */

        //如果是二维数组，那么 dp[i][j] 表示前 i 件物品放入 容量为 j的背包中可以获得的最大收益值
        //这里使用一维数组，求最大收益值，所以不用保留每种情况，只有最后前 N 种物品放入 W 容量的背包中的最大收益值就行
        int[] dp = new int[W+1]; //第 i 件物品 放入一个容量为w的背包中可以获得的最大收益值

        boolean[][] path = new boolean[N+1][W+1]; //记录路径

        //针对每一种物品进行循环判断，判断该物品是否可以被装入背包
        for(int i = 1; i <= N; i++) {
            //从最大容量开始，判断第 i 种物品是否可以被装入
            for(int j = W; j >=w[i]; j--) {
                //如果装入第 i 种物品能获取最大收益，则装入，并且记录下，在该容量下，可以装入  i 物品
                if(dp[j] < dp[j - w[i]] + v[i]) {
                    dp[j] = dp[j - w[i]] + v[i]; //更新前 i 种物品 放入容量为 j 背包中的最大收益值。
                    path[i][j] = true; //这里可以理解为第 i 个物品在背包容量为  j 时可以被放入背包中，并且保证值最大

                }
            }
            System.out.println();
        }

        //逆推出选择物品的路径
        //
//    	int i = N,j = W;
//    	while(i > 0 && j > 0) {
//    		//如果
//    		if(path[i][j]) {
//    			System.out.print(i+" ");
//    			j = j - w[i];
//    		}
//    		i--;
//    	}

        /*
         * 逆向推导，先判断最后一个物品是否可以被装入
         */
        int q = W;
        for(int k = N; k > 0; k--) {
            //如果当容量为 q 时记录着 k 物品可以被装入背包，这说明该物品可以被选中
            if(q > 0 && path[k][q]) {
                System.out.print(k+" ");
                q -=w[k]; //装入该 i 物品，那么背包容量还 有 q - w[k],再次循环判断 i-1 物品 在容量为  q - w[k] 是否被装入过
            }
        }
        System.out.println(dp[W]);
    }


    /**
     * 完全背包问题：也就是说每一种物品可以拿无限次
     * @param W
     * @param N
     * @param w
     * @param v
     */
    public static void completePack(int W,int N,int[] w,int[] v) {
        int[] dp = new int[W+1];
        int[][] path = new int[N+1][W+1];

        for(int i = 1; i<= N;i++) {
            //这里不用考虑每种物品装入的次数，所以选择正序
            for (int j = w[i]; j <= W; j++) {
                if(dp[j] < dp[j-w[i]] +v[i]) {
                    dp[j] = dp[j-w[i]]+v[i];
                    path[i][j] = 1;
                }
            }
        }
        int i = N,j = W;
        int[] times = new int[N+1];
        while(i > 0 && j > 0) {
            if(path[i][j] == 1) {
                j = j - w[i];
                times[i]++;
            }else {//继续判断 i 物品是否可以存入背包中
                i--;
            }
        }
        for (int k = 1; k <= N; k++) {
            System.out.println("选择物品："+k+",数量："+times[k]+" ");
        }


        System.out.println(dp[W]);
    }

    /**
     * 多重背包问题，与完全背包问题类似，就是每种物品的数量有限制，限制数组为 cot
     * @param W
     * @param N
     * @param w
     * @param v
     * @param cot
     */
    public static void multiplePack(int W,int N,int[] w,int[] v,int[] cot) {
        int[] dp = new int[W+1];
        int[][] path = new int[N+1][W+1];

        for (int i = 1; i <= N; i++) {
            //由于第 i 物品可以 拿 cot[i] 次，所以这里需要循环cot[i] 次，判断是否再次装入 i
            for (int k = 1; k <= cot[i]; k++) {
                //这里就和01背包一样，每次判断的时候，i 物品只能被装入 背包 1 次，这样才能正确记录 i 物品装入背包数量，保证不会多装
                for (int j = W; j >= w[i]; j--) {
                    if(dp[j] < dp[j-w[i]]+v[i]) {
                        dp[j] = dp[j - w[i]] + v[i];
                        path[i][j] = 1;
                    }
                }
            }
        }

        int i = N,j = W;
        int[] times = new int[N+1];
        while(i > 0 && j > 0) {
            //如果 i 物品在 背包容量 为 j 时可以装入背包，并且  i 物品装入数量没有超过限制cot[i],继续判断 i 物品是否可以继续装入背包
            if(path[i][j] == 1 && cot[i] > 0) {
                j -=w[i];
                times[i]++;
                cot[i]--;
            }else {
                i--;//否则，判断 i-1 物品
            }
        }
        for (int k = 1; k <= N; k++) {
            System.out.println("选择物品："+k+",数量："+times[k]+" ");
        }
        System.out.println(dp[W]);
    }

}
