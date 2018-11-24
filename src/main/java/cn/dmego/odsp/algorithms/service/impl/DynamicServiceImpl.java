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

        JsonResult jsonResult = new JsonResult();
        List<Map<String, String>> mapList = new ArrayList<>();
        if(fun == 1){ //如果选择的是背包问题
            Integer packFun = dynamicVo.getPackFun();
            Integer W = dynamicVo.getKVolume();
            Integer N = dynamicVo.getKBreedNum();
            Integer[] w = dynamicVo.getWeights();
            Integer[] v = dynamicVo.getValues();
            Integer[] cot = dynamicVo.getLimit();
            if(packFun == 1){//01背包
                mapList = zeroOnePack(dynamicVo, W, N, w, v);
            }else if(packFun == 2){//多重
                mapList = multiplePack(dynamicVo, W, N, w, v, cot);
            }else if(packFun == 3){//完全
                mapList = completePack(dynamicVo, W, N, w, v);
            }
        }else if(fun == 2){

        }else if(fun == 3){

        }
        jsonResult.put("result",mapList);
        return jsonResult;
    }


    /**
     * 将背包问题求得的结果封装成List<Map<String, String>>并返回
     * @param dynamicVo
     * @param path
     * @param W
     * @param N
     * @param w
     * @param v
     * @return
     */
    private static List<Map<String, String>> packToListMap(DynamicVo dynamicVo,boolean[][] path,Integer W,Integer N,Integer[] w,Integer[] v){
        String[] packNames = dynamicVo.getPackNames();
        int packFun = dynamicVo.getPackFun();
        Integer[] cot = dynamicVo.getLimit();
        int[] choiceNums = new int[N]; //选择物品的数量
        int[] sumValues = new int[N]; //物品的总价值
        int[] restVolume = new int[N]; //剩余容量
        //逆推出选择物品的路径
        int i = N,j = W;
        int[] times = new int[N+1];
        if(packFun == 1){
            while(i > 0 && j > 0) {
                if(path[i][j]) { //如果选择了 i 物品
                    choiceNums[i-1] = 1; //设置选择该物品,且数量为 1
                    sumValues[i-1] = v[i];
                    System.out.print(i+" ");
                    j = j - w[i];
                }
                i--;
            }
            restVolume = getRestVolume(choiceNums, w, N, W);

        }else if(packFun == 2){ //如果是多重背包
            while(i > 0 && j > 0) {
                if(path[i][j] && cot[i] > 0) {
                    choiceNums[i-1] += 1;
                    sumValues[i-1] += v[i];
                    j -=w[i];
                    times[i]++;
                    cot[i]--;
                }else {
                    i--;//否则，判断 i-1 物品
                }
            }
            restVolume = getRestVolume(choiceNums, w, N, W);

        }else if(packFun == 3){ //如果是完全背包
            while(i > 0 && j > 0) {
                if(path[i][j]) {
                    choiceNums[i-1] += 1; //如果选择了该物品,该物品数量 +1
                    sumValues[i-1] += v[i]; //物品总价值同样
                    j = j - w[i];
                }else {//否则继续判断 i 物品是否可以存入背包中
                    i--;
                }
            }
            restVolume = getRestVolume(choiceNums, w, N, W);
        }


        List<Map<String,String>> mapList = new ArrayList<>();
        for (int ii = 0; ii <= N; ii++) {
            Map<String,String> map = new HashMap<>();
            if(ii == N){
                map.put("stage", "最大价值");
                map.put("name", String.valueOf(dynamicVo.getBestValue()));
                map.put("number", "");
                map.put("weight", "");
                map.put("value", "");
                map.put("sumValue", "");
                map.put("rescap","");
            }else{
                map.put("stage", String.valueOf(ii+1)); //物品阶段
                map.put("name", String.valueOf(packNames[ii])); //物品名称
                map.put("number", String.valueOf(choiceNums[ii])); //选择的物品数量
                map.put("weight", String.valueOf(w[ii+1]));//单位物品重量
                map.put("value", String.valueOf(v[ii+1])); //单位物品价值
                map.put("sumValue", String.valueOf(sumValues[ii])); //物品的总价值
                map.put("rescap", String.valueOf(restVolume[ii])); //剩余容量
            }
            mapList.add(map);
        }

        return mapList;
    }

    /**
     * 获取剩余容量
     * @param choiceNums
     * @param w
     * @param N
     * @param W
     * @return
     */
    private static int[] getRestVolume(int[] choiceNums,Integer[] w, int N,int W){
        int restVolume[] = new int[N];
        for (int i = 0; i < N;i++){
            if(choiceNums[i]> 0){
                restVolume[i] = W - (w[i+1] * choiceNums[i]);
                W = restVolume[i];
            }else{
                restVolume[i] = W;
            }
        }
        return restVolume;
    }

    /**
     * 0-1背包问题 一维数组解法，记录路径
     *
     * @param W
     * @param N
     * @param w
     * @param v
     */
    private static List<Map<String, String>> zeroOnePack(DynamicVo dynamicVo,Integer W,Integer N,Integer[] w,Integer[] v) {
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
        return packToListMap(dynamicVo, path, W, N, w, v);
    }


    /**
     * 完全背包问题：也就是说每一种物品可以拿无限次
     * @param W
     * @param N
     * @param w
     * @param v
     */
    private static List<Map<String, String>> completePack(DynamicVo dynamicVo,Integer W,Integer N,Integer[] w,Integer[] v) {
        int[] dp = new int[W+1];
        boolean[][] path = new boolean[N+1][W+1]; //记录路径

        for(int i = 1; i<= N;i++) {
            //这里不用考虑每种物品装入的次数，所以选择正序
            for (int j = w[i]; j <= W; j++) {
                if(dp[j] < dp[j-w[i]] +v[i]) {
                    dp[j] = dp[j-w[i]]+v[i];
                    path[i][j] = true;
                }
            }
        }
        dynamicVo.setBestValue(dp[W]);
        return packToListMap(dynamicVo, path, W, N, w, v);
    }

    /**
     * 多重背包问题，与完全背包问题类似，就是每种物品的数量有限制，限制数组为 cot
     * @param W
     * @param N
     * @param w
     * @param v
     * @param cot
     */
    private static List<Map<String, String>>  multiplePack(DynamicVo dynamicVo,Integer W,Integer N,Integer[] w, Integer[] v, Integer[] cot) {
        int[] dp = new int[W+1];
        boolean[][] path = new boolean[N+1][W+1];

        for (int i = 1; i <= N; i++) {
            //由于第 i 物品可以 拿 cot[i] 次，所以这里需要循环cot[i] 次，判断是否再次装入 i
            for (int k = 1; k <= cot[i]; k++) {
                //这里就和01背包一样，每次判断的时候，i 物品只能被装入 背包 1 次，这样才能正确记录 i 物品装入背包数量，保证不会多装
                for (int j = W; j >= w[i]; j--) {
                    if(dp[j] < dp[j-w[i]]+v[i]) {
                        dp[j] = dp[j - w[i]] + v[i];
                        path[i][j] = true;
                    }
                }
            }
        }
        dynamicVo.setBestValue(dp[W]);
        return packToListMap(dynamicVo, path, W, N, w, v);
    }

}
