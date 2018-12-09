package cn.dmego.odsp.algorithms.service.impl;

import cn.dmego.odsp.algorithms.service.DynamicService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
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
        }else if(fun == 2){ //如果选择的是资源分配问题
            Integer N = dynamicVo.getRItemNum(); //项目数
            Integer W = dynamicVo.getRSumValue(); //资源总数
            Integer[] r = dynamicVo.getStrategy(); //可选投资策略数组
            Integer[][] g = dynamicVo.getMatrix(); //项目收益矩阵数组

            mapList = resource(W, N, r, g);

        }else if(fun == 3){ //如果选择的是生产与存储问题
            Integer init = dynamicVo.getPStorage(); //期初存储量
            Integer[] d = dynamicVo.getDemand();
            Integer[] p = dynamicVo.getProductPower();
            Double[] pc = dynamicVo.getUnitProductCost();
            Double[] sc = dynamicVo.getUnitStorageCost();
            Integer[] ms = dynamicVo.getMaxStorage();
            Double[] fc = dynamicVo.getFixedProductCost();

            //首先判断出生产能力是否大于需求量
            if(!comparePD(init, p, d)){
                jsonResult = JsonResult.error(500, "需求超过生产能力!请调整输入数据");
                return jsonResult;
            }else{
                mapList = product(init,d,p,pc,sc,ms,fc);
            }
        }
        jsonResult.put("result",mapList);
        CommonUtil.retState(jsonResult,200);
        return jsonResult;
    }


    /**
     * 将背包问题求得的结果封装成List<Map<String, String>>并返回
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
            restVolume = getPackRestVolume(choiceNums, w, N, W);

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
            restVolume = getPackRestVolume(choiceNums, w, N, W);

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
            restVolume = getPackRestVolume(choiceNums, w, N, W);
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
     * 获取背包问题剩余容量
     */
    private static int[] getPackRestVolume(int[] choiceNums,Integer[] w, int N,int W){
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

    /**
     *
     * 资源分配问题
     *
     * @param W 总资源数
     * @param N 总项目数
     * @param r 可选投资策略数组
     * @param g 原始矩阵数据
     * @return
     */
    private static List<Map<String, String>> resource(Integer W,Integer N,Integer[] r,Integer[][] g){

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

        //6.资源分配问题求得的结果封装成List<Map<String, String>>并返回
        List<Map<String,String>> mapList = new ArrayList<>();
        for (int ii = 1; ii <= N+1; ii++) {
            Map<String,String> map = new HashMap<>();
            if(ii == N+1){
                map.put("stage", "最大价值"); //项目(阶段)
                map.put("input", String.valueOf(p[N][M])); //投入资源数
                map.put("income", ""); //项目收益
                map.put("rest",""); //剩余资源
            }else{
                map.put("stage", "项目"+ii);
                map.put("input", String.valueOf(input[ii]));
                map.put("income", String.valueOf(income[ii]));
                map.put("rest", String.valueOf(restVolume[ii]));//单位物品重量
            }
            mapList.add(map);
        }
        return mapList;
    }

    /**
     *  生产与存储问题
     *
     * @param init 期初存储量 init storage
     * @param d 需求量 demand
     * @param p 生产能力 product power
     * @param pc 单位产品生产成本 unit product cost
     * @param sc 单位产品存储成本 unit storage cost
     * @param ms 最大存储量 max storage
     * @param fc 固定生产成本 fixed product cost
     * @return
     */
    private static List<Map<String, String>> product(Integer init, Integer[] d,Integer[] p, Double[] pc, Double[] sc, Integer[] ms, Double[] fc){

        //1.先做初始化工作
        int n = d.length; //阶段数
        List<Double[]> f = new ArrayList<>(); //用来记录前 i 个 阶段在库存为 j 时的最小成本费用
        List<Integer[]> m = new ArrayList<>(); //记录生产量
        //初始化都为0
        for(int i = 0; i < n; i++) {
            int jr = Math.min(ms[i], sum(d, i)) + 1;
            Double[] df = new Double[jr];
            Integer[] im = new Integer[jr];
            for(int j = 0 ; j < jr; j++) {
                df[j] = 0.0;
                im[j] = 0;
            }
            f.add(df);
            m.add(im);
            if(i == n - 1) {
                f.add(df.clone()); //克隆一个数组,深拷贝
                m.add(im.clone());
            }
        }


        /*------------
            核心代码
        -------------*/
        //2.从最后一个阶段开始计算
        for (int i = n-1; i >=0 ; i--) {
            for (int j = 0; j < Math.min(ms[i],sum(d, i)) + 1; j++) {
                f.get(i)[j] = Double.POSITIVE_INFINITY; //先初始化费用为无穷大
                for (int k = Math.max(0, d[i] - j); k < min(p[i],sum(d, i) - j, ms[i] - j + d[i]) + 1; k++) {
                    double w = 0;
                    if(k == 0){
                        w = (j + k -d[i]) * sc[i];
                    }else{
                        w = fc[i] + k * pc[i] + (j + k - d[i]) * sc[i];
                    }
                    if(j + k - d[i] < f.get(i + 1).length && f.get(i)[j] > f.get(i + 1)[j + k - d[i]] + w){
                        f.get(i)[j] = f.get(i + 1)[j + k - d[i]] + w;
                        m.get(i)[j] = k;
                    }
                }
            }
        }

        //3.求出每个阶段最优生产量和期末存储量等中间显示结果值
        double minValue = f.get(0)[init]; //最小费用
        int[] endStorage = new int[n];//期末存储量
        int[] optimalYield = new int[n]; //最优生产量
        double[] productCost = new double[n]; //生产费用
        double[] storageCost = new double[n]; //存储费用
        double[] unitSumCost = new double[n]; //单个时期生产总费用

        for (int i = 0; i < n; i++) {
            if(init < 0){
                break;
            }
            optimalYield[i] = m.get(i)[init];
            init = m.get(i)[init] + init - d[i];
            endStorage[i] = init;
            storageCost[i] = init * sc[i];
            if(optimalYield[i] == 0){
                productCost[i] = 0;
            }else{
                productCost[i] = optimalYield[i] * pc[i] + fc[i];
            }
            unitSumCost[i] = storageCost[i] + productCost[i];
        }

        //4.生产与存储问题求得的结果封装成List<Map<String, String>>并返回
        List<Map<String,String>> mapList = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            Map<String,String> map = new HashMap<>();
            if(i == n){
                map.put("stage", "最小费用为"); //生产时期(阶段)
                map.put("endS", String.valueOf(minValue)); //期末存储量
                map.put("bestP", ""); //最优生产量
                map.put("pCost",""); //生产费用
                map.put("sCost",""); //存储费用
                map.put("sumCost",""); //单个时期生产总费用
            }else{
                map.put("stage", i+1+""); //生产时期(阶段)
                map.put("endS", String.valueOf(endStorage[i])); //期末存储量
                map.put("bestP", String.valueOf(optimalYield[i])); //最优生产量
                map.put("pCost",String.valueOf(productCost[i])); //生产费用
                map.put("sCost",String.valueOf(storageCost[i])); //存储费用
                map.put("sumCost",String.valueOf(unitSumCost[i])); //单个时期生产总费用
            }
            mapList.add(map);
        }
        return mapList;
    }

    /**
     *  求 整数数组 d 第 a 个下标之后的所有元素之后
     */
    private static int sum(Integer[] d, int a){
        int sum = 0;
        for (int i = d.length - 1; i >= a; i--) {
            sum += d[i];
        }
        return sum;
    }

    /**
     *  返回 整数 a, b, c 中的最小值
     */
    private static int min(int a, int b, int c){
        int min = a < b ? a : b;
        return min < c ? min : c;
    }

    /**
     *  比较每个阶段的生产量和需求量的大小
     */
    private static boolean comparePD(Integer init, Integer[] p, Integer[] d){
        int product = init;
        int demand = 0;
        for (int i = 0; i < d.length; i++) {
            product +=p[i];
            demand +=d[i];
            if(product < demand){
                return false;
            }
        }
        return true;
    }
}
