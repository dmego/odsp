package cn.dmego.odsp.solution.controller;

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


        for (int i = 0; i < list.size(); i++) {
            Map<String,String> headMap = new HashMap<>();
            if(i > 0 && i < list.size()-1){
                originName[i-1] = list.get(i);
            }
            headMap.put("field","sale_"+i);
            headMap.put("title",list.get(i));
            headMapList.add(headMap);
        }

        //生成表数据
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

        JsonResult calculate = transportService.calculate(transportVo);
        List<Map<String, String>> mapList = (List<Map<String, String>>) calculate.get("result");

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

        CommonUtil.retState(jsonResult,200);
        return jsonResult;
    }







    @Override
    public String getErrorPath() {
        return "/error";
    }
}
