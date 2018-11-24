package cn.dmego.odsp.algorithms.service.impl;

import cn.dmego.odsp.algorithms.service.DynamicService;
import cn.dmego.odsp.algorithms.vo.DynamicVo;
import cn.dmego.odsp.common.JsonResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class_name: DynamicServiceImpl
 * package: cn.dmego.odsp.algorithms.service.impl
 * describe: 动态规划 Service 接口实现类
 * creat_user: Dmego
 * creat_date: 2018/11/22
 * creat_time: 12:54
 **/
@Service
public class DynamicServiceImpl implements DynamicService {


    @Override
    public JsonResult calculate(DynamicVo dynamicVo) {
        Integer fun = dynamicVo.getFun();

        if(fun == 1){ //如果选择的是背包问题
            Integer packFun = dynamicVo.getPackFun();
            Integer W = dynamicVo.getKVolume();
            Integer N = dynamicVo.getKBreedNum();
            Integer[] w = dynamicVo.getWeights();
            Integer[] v = dynamicVo.getValues();
            if(packFun == 1){//01背包
                zeroOnePack(dynamicVo,W,N,w,v);
            }else if(packFun == 2){

            }else if(packFun == 3){

            }
        }else if(fun == 2){

        }else if(fun == 3){

        }


        return null;
    }



    private void packToListMap(DynamicVo dynamicVo,boolean[][] path,Integer W,Integer N,Integer[] w,Integer[] v){
        Integer[] choiceNums = new Integer[N]; //选择物品的数量
        Integer[] sumValues = new Integer[N]; //物品的总价值
        Integer[] restVolume = new Integer[N]; //剩余容量
        //逆推出选择物品的路径
        int i = N,j = W,k = W;
        while(i > 0 && j > 0) {
            if(path[i][j]) { //如果选择了 i 物品
                choiceNums[i-1] = 1; //设置选择该物品,且数量为 1
                sumValues[i-1] = v[i];
                System.out.print(i+" ");
                j = j - w[i];
            }else{
                choiceNums[i-1] = 0;
                sumValues[i-1] = 0;
            }
            i--;
        }
        for (int n = 0; n < N;n++){
            if(choiceNums[n]> 0){
                restVolume[n] = k - w[n+1];
                k = restVolume[n];
            }else{
                restVolume[n] = k;
            }
        }

        List<Map<String,String>> mapList = new ArrayList<>();
        for (int ii = 0; ii < N; ii++) {
            Map<String,String> map = new HashMap<>();
        }




        dynamicVo.setChoiceNums(choiceNums);
        dynamicVo.setSumValues(sumValues);
        dynamicVo.setRestVolume(restVolume);

    }

    /**
     * 0-1背包问题 一维数组解法，记录路径
     *
     * @param W
     * @param N
     * @param w
     * @param v
     */
    private static void zeroOnePack(DynamicVo dynamicVo,Integer W,Integer N,Integer[] w,Integer[] v) {

        int[] dp = new int[W+1];
        boolean[][] path = new boolean[N+1][W+1]; //记录路径

        for(int i = 1; i <= N; i++) {
            for(int j = W; j >=w[i]; j--) {
                if(dp[j] < dp[j - w[i]] + v[i]) {
                    dp[j] = dp[j - w[i]] + v[i];
                    path[i][j] = true;
                }
            }
        }

        dynamicVo.setBestValue(dp[W]);



        /*
         * 逆向推导，先判断最后一个物品是否可以被装入
         */
//        int q = W;
//        for(int k = N; k > 0; k--) {
//            //如果当容量为 q 时记录着 k 物品可以被装入背包，这说明该物品可以被选中
//            if(q > 0 && path[k][q]) {
//                System.out.print(k+" ");
//                q -=w[k]; //装入该 i 物品，那么背包容量还 有 q - w[k],再次循环判断 i-1 物品 在容量为  q - w[k] 是否被装入过
//            }
//        }
        System.out.println(dp[W]);
    }


    /**
     * 完全背包问题：也就是说每一种物品可以拿无限次
     * @param W
     * @param N
     * @param w
     * @param v
     */
    private static void completePack(DynamicVo dynamicVo,Integer W,Integer N,Integer[] w,Integer[] v) {


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
    private static void multiplePack(int W,int N,int[] w,int[] v,int[] cot) {
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
