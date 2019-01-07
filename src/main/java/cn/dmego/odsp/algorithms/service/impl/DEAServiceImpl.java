package cn.dmego.odsp.algorithms.service.impl;

import cn.dmego.odsp.algorithms.service.DEAService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.DEAVo;
import cn.dmego.odsp.common.JsonResult;
import org.opensourcedea.dea.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * class_name: DEAServiceImpl
 * package: cn.dmego.odsp.algorithms.service.impl
 * describe: 数据包络分析 Service 实现类
 * creat_user: Dmego
 * creat_date: 2018/12/29
 * creat_time: 22:46
 **/
@Service
public class DEAServiceImpl implements DEAService {


    /**
     * 解决方案下效率评价DEA求解
     */
    @Override
    public JsonResult calFromSo(DEAVo deaVo) {

        JsonResult jsonResult = new JsonResult();

        String[] input = deaVo.getInput();
        String[] output = deaVo.getOutput();

        String[] funs = deaVo.getFunctions();
        String[] dmuNames = deaVo.getDmuNames(); //决策单元数组
        String[] variableNames = deaVo.getVariableNames(); //所有指标数组简写
        String[] varChNames = deaVo.getVarCHNames(); //指标数组中文

        double[][] matrix = deaVo.getMatrix(); //所有数据数组

        //1.确定决策单元个数和指标个数
        int nbDMUs = dmuNames.length; //决策单元数
        int nbVar = input.length + output.length; //投入与产出总指标数

        //2.初始化 VariableOrientation 数据
        VariableOrientation[] variableOrientations = new VariableOrientation[nbVar];
        for (int i = 0; i < nbVar; i++) {
            if (i < input.length) {//前 input.length 项为投入
                variableOrientations[i] = VariableOrientation.INPUT;
            } else {//后面的为产出
                variableOrientations[i] = VariableOrientation.OUTPUT;
            }
        }

        //3.初始化 VariableTypes 数据
        VariableType[] variableTypes = new VariableType[nbVar];
        for (int i = 0; i < nbVar; i++) {
            variableTypes[i] = VariableType.STANDARD;
        }

        //4.构建DEA问题求解对象


        //5.根据不同的方法进行计算,并将结果
        //            区域  综合效率  纯技术效率 规模效率  GRS效率 ... DEA有效   排名
        //返回结果表头： AREA   OE      TE      SE      GRS   ....  VALID    RANK

        //需要返回的数据：table数据和图数据
        List<Map<String, Object>> effMapHeadList = new ArrayList<>(); //效率值表头数据
        List<Map<String, Object>> effMapList = new ArrayList<>(); //效率值表数据
        List<Map<String, Object>> effMapGraph = new ArrayList<>(); //效率值图数据
        double[] ccrEff = null, bccEff = null, drsEff = null, grsEff = null, irsEff = null, sbmEff = null, ncEff = null, ndEff = null; //各模型计算出来的效率值
        double[] seEff = new double[nbDMUs]; //规模效率
        String[] valName = new String[nbDMUs];
        int[] ranks = new int[nbDMUs]; //排名
        double[][] projections = new double[nbDMUs][nbVar],
                slacks = new double[nbDMUs][nbVar],
                weight = new double[nbDMUs][nbVar];

        boolean[] effice = new boolean[nbDMUs];

        try {
            for (int i = 0; i < funs.length; i++) {
                if (funs[i].equals("CCR")) { //如果是CCR模型
                    DEAProblem deaProblem = rendDEAProblem(nbDMUs, nbVar, dmuNames, variableNames, variableOrientations, variableTypes, matrix, ModelType.CCR_I);
                    deaProblem.solve();

                    //需要返回的数据
                    ccrEff = new double[nbDMUs];
                    ccrEff = deaProblem.getObjectives(); //效率值
                    ranks = deaProblem.getRanks(true, RankingType.STANDARD, 10); //排名

                    projections = retainDecimal(deaProblem.getProjections(), 4); //投影值
                    slacks = retainDecimal(deaProblem.getSlacks(), 4); //松弛变量值
                    weight = retainDecimal(deaProblem.getWeight(), 4); //权值

                    effice = deaProblem.getSolution().getEfficient();

                } else if (funs[i].equals("BCC")) {
                    DEAProblem deaProblem = rendDEAProblem(nbDMUs, nbVar, dmuNames, variableNames, variableOrientations, variableTypes, matrix, ModelType.BCC_I);
                    deaProblem.solve();
                    bccEff = new double[nbDMUs];
                    bccEff = deaProblem.getObjectives();

                } else if (funs[i].equals("DRS")) {
                    DEAProblem deaProblem = rendDEAProblem(nbDMUs, nbVar, dmuNames, variableNames, variableOrientations, variableTypes, matrix, ModelType.DRS_I);
                    deaProblem.solve();
                    drsEff = new double[nbDMUs];
                    drsEff = retainDecimal(deaProblem.getObjectives(), 4);

                } else if (funs[i].equals("GRS")) {//如果是GRS lb = 0.8 ub = 1.2
                    DEAProblem deaProblem = rendDEAProblem(nbDMUs, nbVar, dmuNames, variableNames, variableOrientations, variableTypes, matrix, ModelType.GRS_I);
                    deaProblem.setRTSLowerBound(0.8);
                    deaProblem.setRTSUpperBound(1.2);
                    deaProblem.solve();
                    grsEff = new double[nbDMUs];
                    grsEff = retainDecimal(deaProblem.getObjectives(), 4);

                } else if (funs[i].equals("IRS")) {
                    DEAProblem deaProblem = rendDEAProblem(nbDMUs, nbVar, dmuNames, variableNames, variableOrientations, variableTypes, matrix, ModelType.IRS_I);
                    deaProblem.solve();
                    irsEff = new double[nbDMUs];
                    irsEff = retainDecimal(deaProblem.getObjectives(), 4);

                } else if (funs[i].equals("SBM")) {
                    DEAProblem deaProblem = rendDEAProblem(nbDMUs, nbVar, dmuNames, variableNames, variableOrientations, variableTypes, matrix, ModelType.SBM);
                    deaProblem.solve();
                    sbmEff = new double[nbDMUs];
                    sbmEff = retainDecimal(deaProblem.getObjectives(), 4);

                } else if (funs[i].equals("NC")) {
                    DEAProblem deaProblem = rendDEAProblem(nbDMUs, nbVar, dmuNames, variableNames, variableOrientations, variableTypes, matrix, ModelType.NC_I);
                    deaProblem.solve();
                    ncEff = new double[nbDMUs];
                    ncEff = retainDecimal(deaProblem.getObjectives(), 4);

                } else if (funs[i].equals("ND")) {
                    DEAProblem deaProblem = rendDEAProblem(nbDMUs, nbVar, dmuNames, variableNames, variableOrientations, variableTypes, matrix, ModelType.ND_I);
                    deaProblem.solve();
                    ndEff = new double[nbDMUs];
                    ndEff = retainDecimal(deaProblem.getObjectives(), 4);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //6.计算中间需要显示的数据
        //计算规模效率和DEA有效性
        Map<String, Object> validGraph = new HashMap<>(); //有效性分析图表

        //求规模效率值
        for (int i = 0; i < nbDMUs; i++) {
            seEff[i] = CommonUtil.retainDecimal(ccrEff[i] / bccEff[i], 4);
        }
        //求DEA有效性
        ccrEff = retainDecimal(ccrEff, 4);
        bccEff = retainDecimal(bccEff, 4);
        int aNum = 0, bNum = 0, cNum = 0;
        for (int i = 0; i < nbDMUs; i++) {
            if (ccrEff[i] == 1) { //DEA有效
                valName[i] = "DEA有效";
                aNum++;
            } else if (ccrEff[i] > 0.5) {
                valName[i] = "弱DEA无效";
                bNum++;
            } else {
                valName[i] = "严重DEA无效";
                cNum++;
            }
        }

        String[] legend = {"DEA有效", "弱DEA无效", "严重DEA无效"};
        List<Map<String, Object>> valSeriesMapList = new ArrayList<>();
        Map<String, Object> aMap = new HashMap<>();
        aMap.put("value", aNum);
        aMap.put("name", legend[0]);
        aMap.put("selected",true);
        valSeriesMapList.add(aMap);
        Map<String, Object> bMap = new HashMap<>();
        bMap.put("value", bNum);
        bMap.put("name", legend[1]);
        valSeriesMapList.add(bMap);
        Map<String, Object> cMap = new HashMap<>();
        cMap.put("value", cNum);
        cMap.put("name", legend[2]);
        valSeriesMapList.add(cMap);

        validGraph.put("legend", legend);
        validGraph.put("series", valSeriesMapList);


        //7.根据计算的结果生产结果表数据和图数据
        //先生成表头
        effMapHeadList.add(rendHead("AREA", "评价单元"));
        effMapHeadList.add(rendHead("OE", "综合效率"));
        effMapHeadList.add(rendHead("TE", "纯技术效率"));
        effMapHeadList.add(rendHead("SE", "规模效率"));
        effMapHeadList.add(rendHead("VALID", "DEA有效性"));
        effMapHeadList.add(rendHead("RANK", "排名"));
        if (drsEff != null) effMapHeadList.add(rendHead("DRS", "DRS效率"));
        if (grsEff != null) effMapHeadList.add(rendHead("GRS", "GRS效率"));
        if (irsEff != null) effMapHeadList.add(rendHead("IRS", "IRS效率"));
        if (sbmEff != null) effMapHeadList.add(rendHead("SBM", "SBM效率"));
        if (ncEff != null) effMapHeadList.add(rendHead("NC", "NCN效率"));
        if (ndEff != null) effMapHeadList.add(rendHead("ND", "NDSC效率"));

        //再生成效率表数据
        List<double[]> seriesData = new ArrayList<>();
        for (int i = 0; i < nbDMUs; i++) {
            Map<String, Object> effmap = new HashMap<>();

            effmap.put("AREA", dmuNames[i]);
            effmap.put("OE", String.valueOf(ccrEff[i]));
            seriesData.add(ccrEff);
            effmap.put("TE", String.valueOf(bccEff[i]));
            seriesData.add(bccEff);
            effmap.put("SE", String.valueOf(seEff[i]));
            seriesData.add(seEff);
            //d g i s c d
            if (drsEff != null) {
                effmap.put("DRS", String.valueOf(drsEff[i]));
                seriesData.add(drsEff);
            }
            if (grsEff != null) {
                effmap.put("GRS", String.valueOf(grsEff[i]));
                seriesData.add(grsEff);
            }
            if (irsEff != null) {
                effmap.put("IRS", String.valueOf(irsEff[i]));
                seriesData.add(irsEff);
            }
            if (sbmEff != null) {
                effmap.put("SBM", String.valueOf(sbmEff[i]));
                seriesData.add(sbmEff);
            }
            if (ncEff != null) {
                effmap.put("NC", String.valueOf(ncEff[i]));
                seriesData.add(ncEff);
            }
            if (ndEff != null) {
                effmap.put("ND", String.valueOf(ndEff[i]));
                seriesData.add(ndEff);
            }

            effmap.put("VALID", valName[i]); //有效性
            effmap.put("RANK", String.valueOf(ranks[i])); //排名

            effMapList.add(effmap);
        }

        //生成效率值图数据
        String[] funsStr = new String[funs.length + 1];
        for (int i = 0; i < funsStr.length; i++) {
            funsStr[0] = "综合效率";
            funsStr[1] = "纯技术效率";
            funsStr[2] = "规模效率";
            if (i > 2) {
                funsStr[i] = funs[i - 1] + "效率";
            }
        }

        List<Map<String, Object>> seriesMapList = new ArrayList<>();
        for (int i = 0; i < funsStr.length; i++) {
            Map<String, Object> seriesMap = new HashMap<>();
            seriesMap.put("name", funsStr[i]);
            seriesMap.put("type", "line");
            seriesMap.put("data", seriesData.get(i));
            seriesMapList.add(seriesMap);
        }

        Map<String, Object> effGraph = new HashMap<>();
        effGraph.put("legend", funsStr);
        effGraph.put("xAxis", dmuNames);
        effGraph.put("series", seriesMapList);


        ////////////////////////////
        //  生成非DEA有效排名表数据  //
        ///////////////////////////
        Map<String, Integer> rankMap = new HashMap<>();
        int oneNum = 0;
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < nbDMUs; i++) {
            if (ranks[i] == 1) {
                oneNum++;
                strb.append(dmuNames[i]+";");
            }
        }
        String strOne = strb.toString();
        String firstRank = strOne.substring(0, strOne.lastIndexOf(";"));
        rankMap.put(firstRank,1);
        for (int i = 0; i < nbDMUs; i++) {
            if (ranks[i] != 1) {
                rankMap.put(dmuNames[i], ranks[i] - oneNum + 1);
            }
        }
        //这里将map.entrySet()转换成list
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(rankMap.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            //升序排序
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        List<Map<String, Object>> rankMapList = new ArrayList<>();
        for (Map.Entry<String, Integer> mapping : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", mapping.getKey());
            map.put("rank", mapping.getValue());
            rankMapList.add(map);
        }

        ////////////////////////////
        //  生成松弛变量表和图数数据  //
        ///////////////////////////

        List<Map<String, Object>> slackHeadList = new ArrayList<>(); //表头
        List<Map<String, Object>> slackDataList = new ArrayList<>(); //表数据
        Map<String, Object> slackData = new HashMap<>();

        //先生成表头
        slackHeadList.add(rendHead("dmu", "评价单元"));
        for (int i = 0; i < nbVar; i++) {
            slackHeadList.add(rendHead(variableNames[i], varChNames[i]));
        }
        //生成表数据
        for (int i = 0; i < nbDMUs; i++) { //行代表评价单元
            Map<String, Object> data = new HashMap<>();
            data.put("dmu", dmuNames[i]);
            for (int j = 0; j < nbVar; j++) { //列代表指标
                data.put(variableNames[j], slacks[i][j]);
            }
            slackDataList.add(data);
        }

        //生成图数据
        List<Map<String, Object>> slackSerMLiat = new ArrayList<>();

        for (int i = 0; i < nbVar; i++) {
            double[] varData = new double[nbDMUs]; //一个指标数据数组;
            for (int j = 0; j < nbDMUs; j++) { //
                varData[j] = slacks[j][i];
            }
            Map<String, Object> data = new HashMap<>();
            data.put("name", varChNames[i]);
            data.put("type", "bar");
            if (i >= input.length) { //投入指标数据 指标为 负
                data.put("stack", "input");
                for (int k = 0; k < nbDMUs; k++) {
                    varData[k] = varData[k] * -1;
                    data.put("data", varData);
                }
            } else {
                data.put("stack", "output");
                data.put("data", varData);
            }
            slackSerMLiat.add(data);
        }

        slackData.put("head", slackHeadList);
        slackData.put("data", slackDataList);
        slackData.put("legend", varChNames);
        slackData.put("xAxis", dmuNames);
        slackData.put("series", slackSerMLiat);

        ////////////////////////////
        //  生成权值表和图数数据     //
        ///////////////////////////
        List<Map<String, Object>> weightHeadList = new ArrayList<>(); //表头
        List<Map<String, Object>> weightDataList = new ArrayList<>(); //表数据
        Map<String, Object> weightData = new HashMap<>();

        //先生成表头
        weightHeadList.add(rendHead("dmu", "评价单元"));
        for (int i = 0; i < nbVar; i++) {
            weightHeadList.add(rendHead(variableNames[i], varChNames[i]));
        }
        //生成表数据
        for (int i = 0; i < nbDMUs; i++) { //行代表评价单元
            Map<String, Object> data = new HashMap<>();
            data.put("dmu", dmuNames[i]);
            for (int j = 0; j < nbVar; j++) { //列代表指标
                data.put(variableNames[j], weight[i][j]);
            }
            weightDataList.add(data);
        }

        //生成图数据
        //legend
        //indicator: List<Map>
        //series -->data

        List<Map<String, Object>> weightIndLiat = new ArrayList<>();
        List<Map<String, Object>> weightSerDLiat = new ArrayList<>();

        double[] weightMax = CommonUtil.getMax(weight,0);
        for (int i = 0; i < nbDMUs; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", dmuNames[i]);
            map.put("max", weightMax[i]);
            weightIndLiat.add(map);
        }

        for (int i = 0; i < nbVar; i++) {
            double[] varData = new double[nbDMUs];
            for (int j = 0; j < nbDMUs; j++) {
                varData[j] = weight[j][i];
            }
            Map<String, Object> data = new HashMap<>();
            data.put("name", varChNames[i]);
            data.put("value", varData);
            weightSerDLiat.add(data);
        }

//        for (int i = 0; i < nbDMUs; i++) {
//            Map<String, Object> data = new HashMap<>();
//            data.put("name", dmuNames[i]);
//            data.put("value", weight[i]);
//            weightSerDLiat.add(data);
//        }

        weightData.put("head", weightHeadList);
        weightData.put("data", weightDataList);
        weightData.put("legend", varChNames);
        weightData.put("indicator", weightIndLiat);
        weightData.put("seriesData", weightSerDLiat);


        ////////////////////////////////////////////
        //  生成投影表和图数数据                     //
        ///////////////////////////////////////////
        List<Map<String, Object>> projectHeadList = new ArrayList<>(); //表头
        List<Map<String, Object>> porjectDataList = new ArrayList<>(); //表数据
        Map<String, Object> projectData = new HashMap<>();

        //表头数据
        projectHeadList.add(rendHead("dmu", "评价单元"));
        for (int i = 0; i < nbVar; i++) {
            projectHeadList.add(rendHead(variableNames[i], varChNames[i]));
        }
        //生成表数据
        for (int i = 0; i < nbDMUs; i++) { //行代表评价单元
            Map<String, Object> data = new HashMap<>();
            data.put("dmu", dmuNames[i]);
            for (int j = 0; j < nbVar; j++) { //列代表指标
                data.put(variableNames[j], projections[i][j]);
            }
            porjectDataList.add(data);
        }

        //生成图数据(折线图 + 南丁格尔图)
        double[][] projectTrans = CommonUtil.transpose(projections); //投影值转置
        double[][] matrixTrans = CommonUtil.transpose(matrix); //目标值转置

        //生成目标值与原始值的比值数组
        String[][] source = new String[nbVar + 1][nbDMUs + 1];
        for (int i = 0; i < nbVar + 1; i++) {
            if (i == 0) {
                source[i][0] = "dmu";
                for (int j = 0; j < nbDMUs; j++) {
                    source[i][j + 1] = dmuNames[j];
                }
            } else {
                source[i][0] = varChNames[i - 1];
                for (int j = 0; j < nbDMUs; j++) {
                    //计算投入冗余百分比和产出不足百分比
                    double value = 0.0;
                    if ((i - 1) < input.length) { //投入
                        value = CommonUtil.retainDecimal((projectTrans[i - 1][j] / matrixTrans[i - 1][j]) * 100, 2);
                    }else{
                        value = CommonUtil.retainDecimal((matrixTrans[i - 1][j] / projectTrans[i - 1][j]) * 100, 2);
                    }
                    source[i][j + 1] = String.valueOf(value);
                }
            }
        }

        Map<String, Object> sourceMap = new HashMap<>();
        sourceMap.put("sourceData", source);
        sourceMap.put("firstdmu", dmuNames[0]);
        //legend
        //series -->data
        List<Map<String, Object>> proSerLiat = new ArrayList<>(); //达到DEA有效的数据

        for (int i = 0; i < nbVar; i++) {
            double[] varData = new double[nbDMUs];
            for (int j = 0; j < nbDMUs; j++) {
                varData[j] = projections[j][i];
            }
            Map<String, Object> varMap = new HashMap<>();
            varMap.put("name", varChNames[i]);
            varMap.put("type", "line");
            varMap.put("smooth", true);
            varMap.put("seriesLayoutBy", "row");
            varMap.put("data", varData);
            proSerLiat.add(varMap);
        }


        projectData.put("head", projectHeadList);
        projectData.put("data", porjectDataList);
        projectData.put("legend", varChNames);
        projectData.put("source", sourceMap);
        projectData.put("xAxis", dmuNames);
        projectData.put("series", proSerLiat);

        ////////////////////////////////////////////
        //  生成投入冗余和产出不足表数据和图数数据      //
        ///////////////////////////////////////////

        List<Map<String, Object>> inoutHead1List = new ArrayList<>(); //一级表头
        List<Map<String, Object>> inoutHead2List = new ArrayList<>(); //二级表头
        List<Map<String, Object>> inoutDataList = new ArrayList<>(); //表数据
        Map<String, Object> inoutData = new HashMap<>();

        //先生成表头
        inoutHead1List.add(rendHead("dmu", "评价单元",2,1));
        inoutHead1List.add(rendHead("input", "投入冗余值",1,input.length));
        inoutHead1List.add(rendHead("output", "产出不足值",1,output.length));
        for (int i = 0; i < nbVar; i++) {
            inoutHead2List.add(rendHead(variableNames[i], varChNames[i]));
        }

        //生成表数据
        double[][] inout = new double[nbDMUs][nbVar];
        for (int i = 0; i < nbDMUs; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("dmu", dmuNames[i]);
            for (int j = 0; j < nbVar; j++) {
                if(j < input.length){ //投入
                    inout[i][j] = CommonUtil.retainDecimal(matrix[i][j] - projections[i][j],4);
                }else{
                    inout[i][j] =  CommonUtil.retainDecimal(projections[i][j] - matrix[i][j],4);
                }
                map.put(variableNames[j],inout[i][j]);
            }
            inoutDataList.add(map);
        }
        //生成图数据(点图联动)
        Map<String,Object> inoutGraphData = new HashMap<>();
        int maxAvIndex = 0;
        double maxAvValue = 0;
        for (int i = 0; i < nbDMUs; i++) {
            List<double[]> mapData= new ArrayList<>();
            double[] more = new double[nbVar];
            double[] less = new double[nbVar];
            double[] aver = new double[2];
            double moreAv = 0,lessAv = 0;
            for (int j = 0; j < nbVar; j++) {
                if(j < input.length){
                    more[j] = inout[i][j];
                    moreAv += more[j] / matrix[i][j];
                    less[j] = 0;
                }else {
                    more[j] = 0;
                    less[j] = inout[i][j];
                    lessAv += less[j] / matrix[i][j];
                }
            }
            aver[0] = CommonUtil.retainDecimal((moreAv / input.length)*100,2);
            aver[1] = CommonUtil.retainDecimal((lessAv / output.length)*100,2);
            if( maxAvValue < (aver[0] + aver[1])){
                maxAvIndex = i;
                maxAvValue = aver[0] + aver[1];
            }
            mapData.add(more);mapData.add(less);mapData.add(aver);
            inoutGraphData.put(dmuNames[i],mapData);
        }



        inoutData.put("head1", inoutHead1List);
        inoutData.put("head2", inoutHead2List);
        inoutData.put("tableData", inoutDataList);
        inoutData.put("optionData", inoutGraphData);
        inoutData.put("yAxis", varChNames);
        inoutData.put("dmuName",dmuNames[maxAvIndex]);


        ///////////////////////////////
        //  生成返回数据               //
        ///////////////////////////////
        jsonResult.put("effHead", effMapHeadList);
        jsonResult.put("objectives", effMapList);
        jsonResult.put("effGraph", effGraph);

        jsonResult.put("validGraph", validGraph);

        jsonResult.put("deaRanks", rankMapList);

        jsonResult.put("slackData", slackData);
        jsonResult.put("weightData", weightData);
        jsonResult.put("projectData", projectData);
        jsonResult.put("inoutData", inoutData);

        CommonUtil.retState(jsonResult, 200);
        return jsonResult;
    }


    /**
     * 对 一维数组数据进行保留 p 位小数处理
     */
    private double[] retainDecimal(double[] arr, int p) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = CommonUtil.retainDecimal(arr[i], p);
        }
        return arr;
    }

    /**
     * 对 二维数组数据进行保留 p 位小数处理
     */
    private double[][] retainDecimal(double[][] arr, int p) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = CommonUtil.retainDecimal(arr[i][j], p);
            }
        }
        return arr;
    }


    /**
     * 生成头数据
     */
    private Map<String, Object> rendHead(String field, String name) {
        Map<String, Object> Head = new HashMap<>();
        Head.put("field", field);
        Head.put("title", name);
        return Head;
    }

    /**
     * 生成头数据
     * rowspan即纵向跨越的单元格数
     * colspan即横跨的单元格数
     */
    private Map<String, Object> rendHead(String field, String name,int row,int col) {
        Map<String, Object> Head = new HashMap<>();
        if(col == 1){
            Head.put("field", field);
        }else {
            Head.put("align", "center");
        }
        Head.put("title", name);
        Head.put("rowspan", row);
        Head.put("colspan", col);
        return Head;
    }


    /**
     * 根据参数创建 DEAProblem
     */
    private DEAProblem rendDEAProblem(int nbDMUs, int nbVar, String[] dmuNames, String[] variableNames, VariableOrientation[] variableOrientations,
                                      VariableType[] variableTypes, double[][] matrix, ModelType modelType) {
        DEAProblem deaProblem = new DEAProblem(nbDMUs, nbVar);
        deaProblem.setDMUNames(dmuNames);
        deaProblem.setVariableNames(variableNames);
        deaProblem.setVariableOrientations(variableOrientations);
        deaProblem.setVariableTypes(variableTypes);
        deaProblem.setDataMatrix(matrix);
        deaProblem.setModelType(modelType);
        return deaProblem;
    }
}
