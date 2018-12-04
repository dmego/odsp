package cn.dmego.odsp.algtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dy_productTest {
    public static void main(String[] args) {
        int[] d = {2,3,4,2}; //需求量 demand
        int[] p = {6,4,5,4}; //生产能力 product power
        int[] c = {1,1,1,1}; //单位产品生产成本 unit product cost
        double[] f = {0.5,0.5,0.5,0.5}; //单位产品存储成本 unit storage cost
        int[] s = {3,3,3,3}; //最大存储量 max storage
        int[] t = {3,3,3,3}; //固定生产成本 fixed product cost
        int o = 0; //期初存储量 init storage

        product(d, p, c, f, s, t, o);
    }

    public static int sum(int[] d,int a) {
        int sum = 0;
        for (int i = d.length - 1; i >= a; i--) {
            sum +=d[i];
        }
        return sum;
    }

    public static int min(int a, int b, int c) {
        int min = a;
        if( b < min) {
            min = b;
        }
        if(c < min) {
            min = c;
        }
        return min;
    }

    /**
     *
     * @param d d[i] 该阶段的需求量 demand
     * @param p p[i] 该阶段的生产能力  product
     * @param c c[i] 该阶段的单位产品生产成本 product_fee
     * @param f f[i] 该阶段单位产品存储成本 storage_cost
     * @param s s[i] 该阶段最大存储量 max_storage max_s
     * @param t t[i] 该阶段固定生产成本  stable_fee pro_fee
     * @param o 期初存储量 original_storage init
     */
    public static void product(int[] d,int[] p,int[] c, double[] f,int[] s,int[] t,int o) {
        int n = d.length; //阶段数
        List<Double[]> listf = new ArrayList<>(); //用来记录前 i 个 阶段在库存为 j 时的最小成本费用
        List<Integer[]> listm = new ArrayList<>();
        //初始化
        for(int i = 0; i < n; i++) {
            int jr = Math.min(s[i], sum(d, i)) + 1;
            Double[] df = new Double[jr];
            Integer[] im = new Integer[jr];
            for(int j = 0 ; j < jr; j++) {
                df[j] = 0.0;
                im[j] = 0;
            }
            listf.add(df);
            listm.add(im);
            if(i == n - 1) {
                listf.add(df.clone()); //克隆一个数组,深拷贝
                listm.add(im.clone());
            }
        }

        //从最后一个阶段开始计算
        for(int i = n-1; i >= 0; i--) {
            for(int j = 0; j < Math.min(s[i], sum(d, i)) + 1; j++){
                listf.get(i)[j] = Double.MAX_VALUE;
                for(int k = Math.max(0, d[i] - j); k < min(p[i], sum(d, i) - j, s[i] - j + d[i])+1; k++) {
                    double w = 0;
                    if(k == 0) {
                        w = (j + k -d[i]) * f[i];
                    }else {
                        w = t[i] + k * c[i] + (j + k - d[i]) * f[i];
                    }
                    if(j + k - d[i] < listf.get(i + 1).length && listf.get(i)[j] > listf.get(i + 1)[j + k - d[i]] + w) {
                        listf.get(i)[j] = listf.get(i + 1)[j + k -d[i]] + w;
                        listm.get(i)[j] = k;
                    }else {
                        continue;
                    }
                }
            }
        }

        double maxValue = listf.get(0)[o]; //最小费用
        System.out.println("最小费用:"+maxValue);
        int[] products = new int[n]; //最优生产量
        int[] end_storage = new int[n]; //期末存储量
        double[] storage_fee = new double[n]; //存储费用
        double[] product_cost = new double[n]; //生产费用
        double[] total_fee = new double[n]; //单个生产时期总费用

        for (int i = 0; i < n; i++) {
            if(o < 0) {
                break;
            }
            products[i] = listm.get(i)[o];
            o = listm.get(i)[o] + o - d[i];
            end_storage[i] = o;
            storage_fee[i] = o * f[i];
            if(products[i] == 0) {
                product_cost[i] = 0;
            }else {
                product_cost[i] = products[i] * c[i] + t[i];
            }
            total_fee[i] = storage_fee[i]+product_cost[i];
        }

        System.out.println("期末存储量:"+Arrays.toString(end_storage));
        System.out.println("最优生产量:"+ Arrays.toString(products));
        System.out.println("生产费用:"+Arrays.toString(product_cost));
        System.out.println("存储费用:"+Arrays.toString(storage_fee));
        System.out.println("单个生产时期总费用:"+Arrays.toString(total_fee));

    }

}
