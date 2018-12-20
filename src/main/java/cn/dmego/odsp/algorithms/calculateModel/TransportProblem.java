package cn.dmego.odsp.algorithms.calculateModel;

import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.TransportVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class_name: TransportProblem
 * package: cn.dmego.odsp.algorithms.calculateModel
 * describe: 运输问题计算模型
 * creat_user: Dmego
 * creat_date: 2018/12/19
 * creat_time: 18:06
 **/
@Data
public class TransportProblem {

    private double dm = 9999.0;
    private int originNum;
    private int salesNum;

    public int funType; //1:极大，2：极小
    private int outputsSalesEqualType;
    private double[] output;
    private double[] sales;
    private double[][] price;
    private double[][] price2;
    private double[][] plan;
    public List<Map<String, String>> resultData;
    public String log;

    public boolean TransportationProblemCalculate(TransportVo transportVo) {
        try {
            originNum = transportVo.getOriginNum();
            salesNum = transportVo.getSalesNum();
            price = transportVo.getPrice();
            price2 = transportVo.getPrice();
            plan = transportVo.getPlan();
            sales = transportVo.getSales();
            output = transportVo.getOutput();
            funType = transportVo.getSType();

            outputsSalesEqualType = judgeOutputsSalesIfEqual();

            if (funType == 1) { //极大
                double matrixMavValue = getMatrixMavValue(price);
                for (int i = 0; i < price.length; i++) {
                    for (int j = 0; j < price[0].length; j++) {
                        price[i][j] = matrixMavValue - price[i][j];
                    }
                }
            }
            vogelCalculate();

            resultData = createResultData();
            log = "计算成功!";
            return true;
        } catch (Exception e) {
            log = "计算失败!";
            return false;
        }
    }

    private int judgeOutputsSalesIfEqual() {
        double num1 = 0.0;
        for (int i = 0; i < originNum; i++) {
            num1 += output[i];
        }
        double  num2 = 0.0;
        for (int i = 0; i < salesNum; i++) {
            num2 += sales[i];
        }
        if (num1 > num2){
            double[][] price22 = new double[originNum][salesNum+1];
            double[][] price3 = new double[originNum][salesNum+1];
            double[][] plan1 = new double[originNum][salesNum+1];
            double[] sales1 = new double[salesNum+1];
            for (int i = 0; i < originNum; i++) {
                for (int j = 0; j < salesNum; j++) {
                    price22[i][j] = price[i][j];
                    price3[i][j] = price[i][j];
                    plan1[i][j] = 0.0;
                    sales1[j] = sales[j];
                }
                price3[i][salesNum] = funType != 1 ? 0: dm;
                price22[i][salesNum] = funType != 1 ? 0: dm;
                plan1[i][salesNum] = 0.0;
            }
            sales1[salesNum] = num1 - num2;
            price = price3;
            price2 = price22;
            salesNum = salesNum+1;
            plan = plan1;
            sales = sales1;
            return 1;
        }
        if(num1 < num2){
            double[][] price22 = new double[originNum+1][salesNum];
            double[][] price3 = new double[originNum+1][salesNum];
            double[][] plan1 = new double[originNum+1][salesNum];
            double[] output1 = new double[originNum+1];
            for (int i = 0; i < originNum; i++) {
                for (int j = 0; j < salesNum; j++) {
                    price22[i][j] = price[i][j];
                    price22[originNum][j] = funType != 1 ? 0: dm;
                    price3[i][j] = price[i][j];
                    price3[originNum][j] = funType != 1 ? 0: dm;
                }
                output1[i] = output[i];
            }
            output1[originNum] = num2 - num1;
            price = price3;
            price2 = price22;
            originNum = originNum+1;
            plan = plan1;
            output = output1;
            return 2;
        }

        double[][] price22 = new double[originNum][salesNum];
        for (int i = 0; i < originNum; i++) {
            for (int j = 0; j < salesNum; j++) {
                price22[i][j] = price[i][j];
            }
        }
        price2 = price22;
        return 0;
    }

    /**
     * 运输问题创建返回结果数据
     * @return
     */
    private List<Map<String, String>> createResultData() {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (int i = 0; i < output.length; i++) {
            Map<String, String> map = new HashMap<>();

            if(outputsSalesEqualType == 2){ //如果 产量和大于销量和
                if(i == output.length - 1){
                    map.put("sales","销量剩余");
                    for (int j = 0; j <  price[0].length; j++) {
                        map.put("B"+(j+1),String.valueOf(CommonUtil.retainDecimal(plan[i][j],3)));
                    }
                }else{
                    map.put("sales","A"+(i+1));
                    for (int j = 0; j < plan[0].length; j++) {
                        map.put("B"+(j+1),String.valueOf(CommonUtil.retainDecimal(plan[i][j],3)));
                    }
                    map.put("surplus",String.valueOf(CommonUtil.retainDecimal(output[i],3)));
                }
            }else if(outputsSalesEqualType == 1 ){//如果产量和小于销量和
                map.put("sales","A"+(i+1));
                for (int j = 0; j < plan[0].length -1; j++) {
                    map.put("B"+(j+1),String.valueOf(CommonUtil.retainDecimal(plan[i][j],3)));
                }
                map.put("surplus",String.valueOf(CommonUtil.retainDecimal(plan[i][plan[0].length - 1],3)));
            }else if(outputsSalesEqualType == 0){
                map.put("sales","A"+(i+1));
                for (int j = 0; j < plan[0].length; j++) {
                    map.put("B"+(j+1),String.valueOf(CommonUtil.retainDecimal(plan[i][j],3)));
                }
                map.put("surplus",String.valueOf(CommonUtil.retainDecimal(output[i],3)));
            }
            mapList.add(map);
        }
        if(outputsSalesEqualType == 1 || outputsSalesEqualType == 0){
            Map<String, String> map1 = new HashMap<>();
            map1.put("sales","销量剩余");
            for (int j = 0; j <  price[0].length; j++) {
                map1.put("B"+(j+1),String.valueOf(CommonUtil.retainDecimal(sales[j],3)));
            }
            mapList.add(map1);
        }

        double num1 = 0.0;
        for (int i = 0; i < price2.length; i++) {
            for (int j = 0; j < price2[0].length; j++) {
                double num2 = plan[i][j];
                if(num2 != 0.0){
                    double num3 = price2[i][j];
                    if(funType != 1 || num3 != dm){
                        double num4 = num2 * num3;
                        if(num1 == 0.0){
                            num1 = num4;
                        }else {
                            num1 += num4;
                        }
                    }
                }
            }
        }

        Map<String, String> map2 = new HashMap<>();
        map2.put("sales",funType == 2 ? "总运输成本" : "总运输利润");
        map2.put("B1",String.valueOf(CommonUtil.retainDecimal(num1,3)));
        mapList.add(map2);

        return mapList;
    }

    private void vogelCalculate() {
        boolean flag1 = true;
        boolean flag2 = true;
        for (int i = 0; i < output.length; i++) {
            if(output[i] > 0.0){
                flag1 = false;
            }
        }
        for (int i = 0; i < sales.length; i++) {
            if(sales[i] > 0.0){
                flag2 = false;
            }
        }
        if(flag1 || flag2){
            return;
        }
        int length1 = price.length;
        int length2 = price[0].length;

        double num1 = 0.0;
        int nIndex1 = 0;
        boolean flag3 = true;
        double[] price1 = new double[length2];
        for (int index1 = 0; index1 < length1; index1++) {
            if(output[index1] != 0.0){
                for (int index2 = 0; index2 < length2; index2++) {
                    price1[index2] = price[index1][index2];
                }
                double minAndNextMinMinus = calculateMinAndNextMinMinus(price1);
                if(flag3){
                    num1 = minAndNextMinMinus;
                    nIndex1 = index1;
                    flag3 = false;
                }else if(minAndNextMinMinus > num1){
                    num1 = minAndNextMinMinus;
                    nIndex1 = index1;
                }
            }
        }

        double num2 = 0.0;
        int nIndex2 = 0;
        boolean flag4 = true;
        double[] price2 = new double[length1];
        for (int index1 = 0; index1 < length2; index1++) {
            if(sales[index1] != 0.0){
                for (int index2 = 0; index2 < length1; index2++) {
                    price2[index2] = price[index2][index1];
                }
                double minAndNextMinMinus = calculateMinAndNextMinMinus(price2);
                if(flag4){
                    num2 = minAndNextMinMinus;
                    nIndex2 = index1;
                    flag4 = false;
                }else if(minAndNextMinMinus > num2){
                    num2 = minAndNextMinMinus;
                    nIndex2 = index1;
                }
            }
        }

        double dMinValue1 = 0.0;
        int index3;
        int index4;
        if(num2 > num1){
            int nMinValueIndex = 0;
            Ref ref1 = new Ref(dMinValue1,nMinValueIndex);
            getMinValueFromMartixByIndex(nIndex2,2,price,ref1);
            nMinValueIndex = ref1.nMinValueIndex;
            dMinValue1 = ref1.dMinValue;
            index3 = nMinValueIndex;
            index4 = nIndex2;
        }else if(num2 < num1){
            int nMinValueIndex = 0;
            Ref ref2 = new Ref(dMinValue1,nMinValueIndex);
            getMinValueFromMartixByIndex(nIndex1,1,price,ref2);
            nMinValueIndex = ref2.nMinValueIndex;
            dMinValue1 = ref2.dMinValue;
            index3 = nIndex1;
            index4 = nMinValueIndex;
        }else {
            double dMinValue2 = 0.0;
            int nMinValueIndex1 = 0;
            Ref ref3 = new Ref(dMinValue2,nMinValueIndex1);
            getMinValueFromMartixByIndex(nIndex1,1,price,ref3);
            dMinValue2 = ref3.dMinValue;
            nMinValueIndex1 = ref3.nMinValueIndex;

            double dMinValue3 = 0.0;
            int nMinValueIndex2 = 0;
            Ref ref4 = new Ref(dMinValue3,nMinValueIndex2);
            getMinValueFromMartixByIndex(nIndex2,2,price,ref4);
            dMinValue3 = ref4.dMinValue;
            nMinValueIndex2 = ref4.nMinValueIndex;

            if(dMinValue2 < dMinValue3){
                index3 = nIndex1;
                index4 = nMinValueIndex1;
            }else {
                index3 = nMinValueIndex2;
                index4 = nIndex2;
            }
        }

        double num3 = output[index3];
        double mSale = sales[index4];
        if(num3 < mSale){
            plan[index3][index4] = num3;
            output[index3] = 0.0;
            sales[index4] = mSale - num3;
            for (int index1 = 0; index1 < length2; index1++) {
                price[index3][index1] = -1.0;

            }
        }else if(num3 > mSale){
            plan[index3][index4] = mSale;
            output[index3] = num3 - mSale;
            sales[index4] = 0.0;
            for (int index1 = 0; index1 < length1; index1++) {
                price[index1][index4] = -1.0;
            }
        }else {
            plan[index3][index4] = mSale;
            output[index3] = 0.0;
            sales[index4] = 0.0;
            for (int index1 = 0; index1 < length2; index1++) {
                price[index3][index1] = -1.0;
            }
            for (int index1 = 0; index1 < length1; index1++) {
                price[index1][index4] = -1.0;
            }
        }
        vogelCalculate();
    }

    private void getMinValueFromMartixByIndex(int nIndex, int nIndexType, double[][] mMartix, Ref ref1) {
        boolean flag = true;
        int num1 = nIndexType != 1 ? mMartix.length : mMartix[0].length;
        for (int i = 0; i < num1; i++) {
            double num2 = nIndexType != 1 ? mMartix[i][nIndex] : mMartix[nIndex][i];
            if(nIndexType == 1){
                if(sales[i] == 0.0){
                    continue;
                }
            }else if (output[i] == 0.0){
                continue;
            }
            if(flag){
                ref1.dMinValue = num2;
                ref1.nMinValueIndex = i;
                flag = false;
            }else if(num2 < ref1.dMinValue){
                ref1.dMinValue = num2;
                ref1.nMinValueIndex = i;
            }
        }
    }

    private double calculateMinAndNextMinMinus(double[] price) {
        double num1 = 0.0;
        double num2 = 0.0;
        int length = price.length;
        boolean flag1 = true;
        int index1 = 0;
        for (int index2 = 0; index2 < length; index2++) {
            double num3 = price[index2];
            if(num3 >= 0.0){
                if(flag1){
                    num1 = num3;
                    index1 = index2;
                    flag1 = false;
                }else if(num3 < num1){
                    num1 = num3;
                    index1 = index2;
                }
            }
        }
        price[index1] = -1.0;
        boolean flag2 = true;
        for (int index2 = 0; index2 < length; index2++) {
            double num3 = price[index2];
            if(num3 >= 0.0){
                if(flag2){
                    num2 = num3;
                    flag2 = false;
                }else if(num3 < num2){
                    num2 = num3;
                }
            }
        }
        if(num2 < num1){
            return num1;
        }
        return num2 - num1;
    }

    private double getMatrixMavValue(double[][] price) {
        double num1 = 0.0;
        boolean flag = true;
        for (int i = 0; i < price.length; i++) {
            for (int j = 0; j < price[0].length; j++) {
                double num2 = price[i][j];
                if(flag){
                    num1 = num2;
                }else if(num2 > num1){
                    num1 = num2;
                }
            }
        }
        return num1;
    }

    @Data
    private class Ref{
        public double dMinValue;
        public int nMinValueIndex;

        public Ref(double dMinValue,int nMinValueIndex){
            this.dMinValue = dMinValue;
            this.nMinValueIndex = nMinValueIndex;
        }
    }

}
