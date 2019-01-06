package cn.dmego.odsp.solution.controller;

import cn.dmego.odsp.algorithms.calculateModel.TransportProblem;
import cn.dmego.odsp.algorithms.service.TransportService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.TransportVo;
import cn.dmego.odsp.common.BaseController;
import cn.dmego.odsp.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SAAJResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class_name: TransoptiController
 * package: cn.dmego.odsp.solution.controller
 * describe: 运输优化 Controller
 * creat_user: Dmego
 * creat_date: 2019/1/4
 * creat_time: 5:26
 **/
@Controller
@RequestMapping("/solution/transport")
public class TransoptiController extends BaseController implements ErrorController {

    @Autowired
    private TransportService transportService;

    /**
     * 跳转到案例简介页面
     */
    @RequestMapping("/trIntro")
    public String trIntro(){
        return "solution/transport/trIntro.html";
    }

    /**
     * 跳转到数据采集页面
     */
    @RequestMapping("/trCollect")
    public String trCollect(){
        return "solution/transport/trCollect.html";
    }

    /**
     * 跳转到数据处理页面
     */
    @RequestMapping("/trAnalysis")
    public String trAnalysis(){
        return "solution/transport/trAnalysis.html";
    }


    /**
     * 下载模板数据
     */
    @RequestMapping("/download")
    public String download(HttpServletRequest request, HttpServletResponse response){
        String filePath = "src/main/resources/static/upload/mould/运输优化模板.xlsx";
        String fileName = "运输优化模板";
        CommonUtil.downLoadTempFile(filePath,fileName,response);
        return null;
    }


    /**
     * 上传运输优化模板,解析文件,计算优化结果,将解析数据与优化数据返回
     */
    @ResponseBody
    @RequestMapping("/upload")
    public JsonResult upload(@RequestParam("file") MultipartFile file){
        String filePath = "src/main/resources/static/upload/temp/transport/"+getLoginUser().getUserName()+"/";
        JsonResult jsonResult = new JsonResult();
        //存储上传的文件,返回存储的文件名
        String fileName = CommonUtil.saveFile(filePath, file);
        //使用EasyExcel读取文件并返回数据
        List<Object> datas = CommonUtil.easyExcel(filePath+fileName);

        //2.解析读取的数据
        List<Map<String,String>> headMapList = new ArrayList<>(); //表头
        List<Map<String,String>> dataMapList = new ArrayList<>(); //表数据

        //生成表头
        List<String> list = (List<String>) datas.get(0); //解析第一行,生成表头数据(产地)

        int originNum = list.size()-2; //产地数
        int saleNum = datas.size()-2; //销地数
        String[] originName = new String[originNum]; //产地
        String[] saleName = new String[saleNum]; //销地

        double[] output = new double[originNum]; //产量
        double[] sales = new double[saleNum]; //销量
        double[][] price = new double[originNum][saleNum];
        double[][] price2 = new double[originNum][saleNum];
        double[][] plan = new double[originNum][saleNum];

        //生成原始数据表头
        for (int i = 0; i < list.size(); i++) {
            Map<String,String> headMap = new HashMap<>();
            if(i > 0 && i < list.size()-1){
                originName[i-1] = list.get(i);
            }
            headMap.put("field","sale_"+i);
            headMap.put("title",list.get(i));
            headMapList.add(headMap);
        }

        //生成原始数据表数据
        for (int i = 1; i < datas.size(); i++) {
            Map<String,String> dataMap = new HashMap<>();
            List<String> dataList = (List<String>) datas.get(i);
            if(i < datas.size()-1){
                saleName[i-1] = dataList.get(0);
            }

            for (int j = 0; j < dataList.size(); j++) {
                if(i < datas.size() -1){
                    if(j > 0 && j < dataList.size()-1) {
                        price[j - 1][i - 1] = Double.parseDouble(dataList.get(j));
                        price2[j - 1][i - 1] = Double.parseDouble(dataList.get(j));
                        plan[j-1][i-1] = 0.0;
                    }

                    if(j == dataList.size()-1){//最后一列为销量
                        sales[i-1] = Double.parseDouble(dataList.get(j));
                    }
                }
                if(i == datas.size()-1){ //最后一行为产量
                    if(j > 0 && j < dataList.size() - 1){
                        output[j-1] = Double.parseDouble(dataList.get(j));
                    }
                }
                dataMap.put("sale_"+j,dataList.get(j));
            }
            dataMapList.add(dataMap);
        }

        ////////////////////////////////////////
        //构建运输问题计算VO对象,调用模型进行计算   //
        ///////////////////////////////////////
        Map<String,Object> resData = new HashMap<>(); //计算后的结果数据

        TransportVo transportVo = new TransportVo();
        transportVo.setOriginNum(originNum);
        transportVo.setOriginNames(originName);
        transportVo.setSalesNum(saleNum);
        transportVo.setSaleNames(saleName);
        transportVo.setOutput(output.clone());
        transportVo.setSales(sales.clone());
        transportVo.setPrice(price.clone());
        transportVo.setPrice2(price2.clone());
        transportVo.setPlan(plan);
        transportVo.setSType(2); //极小值(运输成本最小)
        //计算优化问题结果
        TransportProblem solution = transportService.calculateSolution(transportVo);
        List<Map<String, String>> resultData = solution.resultData;//计算结果数据

        //对结果数据进行转置显示
        int row = resultData.size();
        int col = resultData.get(0).size();
        String[][] resultArr = new String[row-1][col-1];
        String bestValue = null;

        for (int i = 0; i < row; i++) {
            Map<String, String> map = resultData.get(i);
            if(i < resultData.size()-1){
                for (int j = 0; j < col-1; j++) {
                    if(j < col - 2){
                        resultArr[i][j] = map.get("B"+(j+1));
                    }else {
                        resultArr[i][j] = map.get("surplus");
                    }
                }
            }else {
                bestValue = map.get("B1");
            }
        }

        //转置二维数组
        String[][] transposeArr = CommonUtil.transpose(resultArr);

        //构建返回的结果数据
        List<Map<String, Object>> resultData2 = new ArrayList<>();
        for (int i = 0; i < transposeArr.length; i++) {
            Map<String,Object> map = new HashMap<>();
            if(i < transposeArr.length - 1){
                map.put("sales",saleName[i]);
            }else {
                map.put("sales","产量剩余");
            }
            for (int j = 0; j < transposeArr[0].length; j++) {
                if(j < transposeArr[0].length-1){
                    map.put("B"+(j+1),transposeArr[i][j]);
                }else {
                    map.put("output",transposeArr[i][j]);
                }
            }
            resultData2.add(map);
        }

        Map<String,Object> sumMap = new HashMap<>();
        sumMap.put("sales","总运输成本");
        sumMap.put("B1",bestValue);
        resultData2.add(sumMap);

        //构建结果表头
        List<Map<String,Object>> resultHead = new ArrayList<>();

        Map<String,Object> salesMap = new HashMap<>();
        salesMap.put("field","sales");
        salesMap.put("title","销地/产地");
        resultHead.add(salesMap);
        for (int i = 0; i < originNum; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("field","B"+(i+1));
            map.put("title",originName[i]);
            resultHead.add(map);
        }
        Map<String,Object> outputMap = new HashMap<>(); //销量剩余
        outputMap.put("field","output");
        outputMap.put("title","销量剩余");
        resultHead.add(outputMap);

        //构建产销可视化地图数据
        Map<String,Object> triMapData = new HashMap<>();

        Map<String, Object> mapListData = new HashMap<>(); //地图各点联系数据

        List<Map<String,String>> psSeriesLinks = new ArrayList<>();

        for (int i = 0; i < originNum; i++) { //从产地角度看
            List<Object> mapData = new ArrayList<>();
            Map<String, String> map = resultData.get(i);
            for (int j = 0; j < saleNum; j++) {
                List<Object> tempList = new ArrayList<>();
                Map<String,Object> map1 = new HashMap<>();
                Map<String,Object> map2 = new HashMap<>();
                Map<String, String> linkMap = new HashMap<>();
                map1.put("name",originName[i]);
                linkMap.put("source",originName[i]);
                if(Double.parseDouble(map.get("B"+(j+1))) != 0){
                    map2.put("name",saleName[j]);
                    map2.put("value",map.get("B"+(j+1)));
                    linkMap.put("target",saleName[j]);
                    linkMap.put("value",map.get("B"+(j+1)));
                    tempList.add(map1);
                    tempList.add(map2);
                    mapData.add(tempList);
                    psSeriesLinks.add(linkMap);
                }
            }

            mapListData.put(originName[i],mapData);
        }

        triMapData.put("pointData",mapListData);
        triMapData.put("legend",originName);


        ///////////////////////////
        //构建产销可视化图表数据     //
        //////////////////////////
        Map<String,Object> proSaleData = new HashMap<>();

        List<Map<String,Object>> psSeriesData = new ArrayList<>();

        for (int i = 0; i < saleNum+originNum; i++) {
            Map<String,Object> map = new HashMap<>();
            if(i < saleNum){
                map.put("name",saleName[i]);
                map.put("symbolSize",CommonUtil.retainDecimal(sales[i]/100,0));
                map.put("draggable",true);
                map.put("saleNum",sales[i]);
                map.put("originNum",0);
            }else{
                map.put("name",originName[i-saleNum]);
                map.put("symbolSize",CommonUtil.retainDecimal(output[i-saleNum]/100,0));
                map.put("category",originName[i-saleNum]);
                map.put("draggable",true);
                map.put("saleNum",0);
                map.put("originNum",output[i-saleNum]);
            }
            psSeriesData.add(map);
        }

        List<Map<String,Object>> psSeriesCategories = new ArrayList<>();
        for (int i = 0; i < originNum; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",originName[i]);
            psSeriesCategories.add(map);
        }

        proSaleData.put("legend",originName);
        proSaleData.put("seriesData",psSeriesData);
        proSaleData.put("seriesLinks",psSeriesLinks);
        proSaleData.put("seriesCategories",psSeriesCategories);



        //////////////////////////////
        //产地角度分析(带上产量情况) 堆叠图
        /////////////////////////////
        Map<String,Object> produceData = new HashMap<>();
        List<Map<String,Object>> proSeriesData = new ArrayList<>();

        String[] originBack = new String[originNum];

        for (int i = 0; i < saleNum + 1; i++) {
            Map<String,Object> seriesMap = new HashMap<>();
            String[] dataArr = new String[originNum];
            for (int j = 0; j < originNum; j++) {
                dataArr[j] = transposeArr[i][j];
            }
            if(i == saleNum){
                originBack = dataArr;
                seriesMap.put("name","产量剩余");
                seriesMap.put("type","line");
                seriesMap.put("yAxisIndex",1);
                seriesMap.put("smooth",true);
                seriesMap.put("data",dataArr);
            }else{
                seriesMap.put("name",saleName[i]);
                seriesMap.put("type","line");
                seriesMap.put("yAxisIndex",0);
                seriesMap.put("smooth",true);
                seriesMap.put("stack",saleName[i]);
                seriesMap.put("data",dataArr);
            }
            proSeriesData.add(seriesMap);
        }

        produceData.put("legend",saleName);
        produceData.put("xAxis",originName);
        produceData.put("series",proSeriesData);

        //////////////////////////////
        //销地角度分析(带上销量剩余) 折线图
        /////////////////////////////
        Map<String,Object> saleData = new HashMap<>();
        List<Map<String,Object>> salSeriesData = new ArrayList<>();

        String[] saleBack = new String[saleNum];

        for (int i = 0; i < originNum + 1; i++) {
            Map<String, String> map = resultData.get(i);
            Map<String,Object> seriesMap = new HashMap<>();
            String[] dataArr = new String[saleNum];
            for (int j = 0; j < saleNum; j++) {
                dataArr[j]  = map.get("B"+(j+1));
            }
            if(i == originNum){
                saleBack = dataArr;
                seriesMap.put("name","销量剩余");
                seriesMap.put("type","line");
                seriesMap.put("yAxisIndex",1);
                seriesMap.put("smooth",true);
                seriesMap.put("data",dataArr);
            }else{
                seriesMap.put("name",originName[i]);
                seriesMap.put("type","bar");
                seriesMap.put("yAxisIndex",0);
                seriesMap.put("smooth",true);
                seriesMap.put("stack","销量");
                seriesMap.put("data",dataArr);
            }
            salSeriesData.add(seriesMap);
        }

        saleData.put("legend",originName);
        saleData.put("xAxis",saleName);
        saleData.put("series",salSeriesData);


        ///////////////////////////
        //最小成本情况             //
        //////////////////////////
        Map<String,Object> costData = new HashMap<>();
        //计算总剩余销量和产量
        double saleSum = 0;
        for (int i = 0; i < saleBack.length; i++) {
            saleSum += Double.parseDouble(saleBack[i]);
        }
        double originSum = 0;
        for (int i = 0; i < originBack.length; i++) {
            originSum += Double.parseDouble(originBack[i]);
        }

        costData.put("saleSum",saleSum);
        costData.put("originSum",originSum);
        costData.put("bestValue",bestValue);

        //结果分析数据
        resData.put("resHead",resultHead);
        resData.put("resData",resultData2);
        resData.put("triMapData",triMapData); //地图可视化数据
        resData.put("proSaleData",proSaleData); //产销可视化数据
        resData.put("produceData",produceData); //产地角度数据
        resData.put("saleData",saleData); //销地角度数据
        resData.put("costData",costData); //最小成本数据


        ///////////////////////////
        //可视化展示上传的原始数据   //
        //////////////////////////
        Map<String,Object> oriDataMap = new HashMap<>();
        List<Map<String,Object>> oriSeriesList = new ArrayList<>();
        for (int i = 0; i < price.length; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",originName[i]);
            map.put("type","bar");
            map.put("stack","origin");
            map.put("xAxisIndex",0);
            map.put("yAxisIndex",0);
            map.put("data",price[i]);
            oriSeriesList.add(map);
        }

        oriDataMap.put("head",headMapList);
        oriDataMap.put("data",dataMapList);
        oriDataMap.put("legend",originName);
        oriDataMap.put("yAxis0",saleName);
        oriDataMap.put("xAxis1",saleName);
        oriDataMap.put("xAxis2",originName);
        oriDataMap.put("series",oriSeriesList);
        oriDataMap.put("originData",output); //销量
        oriDataMap.put("saleData",sales); //产量

        oriDataMap.put("fileName",fileName);


        ///////////////////////////
        //返回JSONResult数据      //
        //////////////////////////
        jsonResult.put("oriData",oriDataMap);
        jsonResult.put("resData",resData);

        CommonUtil.retState(jsonResult,200);
        return jsonResult;
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }
}
