package cn.dmego.odsp.solution.controller;

import cn.dmego.odsp.algorithms.service.DEAService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.DEAVo;
import cn.dmego.odsp.common.BaseController;
import cn.dmego.odsp.common.JsonResult;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * class_name: EfficiencyController
 * package: cn.dmego.odsp.solution.controller
 * describe: 效率评价 Controller
 * creat_user: Dmego
 * creat_date: 2018/12/24
 * creat_time: 4:13
 **/
@Controller
@RequestMapping("/solution/efficiency")
public class EfficiencyController extends BaseController implements ErrorController {

    @Autowired
    private DEAService deaService;

    /**
     * 跳转到案例简介页面
     */
    @RequestMapping("/efIntro")
    public String efIntro(){
        return "solution/efficiency/efIntro.html";
    }

    /**
     * 跳转到数据采集页面
     */
    @RequestMapping("/efCollect")
    public String efCollect(){
        return "solution/efficiency/efCollect.html";
    }

    /**
     * 跳转到数据处理页面
     */
    @RequestMapping("/efProcess")
    public String efProcess(){
        return "solution/efficiency/efProcess.html";
    }

    /**
     * 跳转到结果分析页面
     */
    @RequestMapping("/efAnalysis")
    public String efAnalysis(){
        return "solution/efficiency/efAnalysis.html";
    }


    /**
     * 上传文件,解析文件并返回
     */
    @ResponseBody
    @RequestMapping("/upload")
    public JsonResult upload(@RequestParam("file") MultipartFile file){
        String filePath = "src/main/resources/static/upload/temp/efficiency/"+getLoginUser().getUserName()+"/";
        JsonResult jsonResult = new JsonResult();
        //1.先将文件保存
        String fileName = CommonUtil.saveFile(filePath, file);
        if(fileName != null){
            //2.使用Alibaba的easyExecl解析execl文件
            InputStream inputStream = null;
            List<Map<String,String>> headMapList = new ArrayList<>(); //表头
            List<Map<String,String>> dataMapList = new ArrayList<>(); //表数据

            List<String> legendList = new ArrayList<>();
            List<String> xAxisList = new ArrayList<>();
            List<Map<String,Object>> seriesMapList = new ArrayList<>(); //可视化展示数据
            try {
                inputStream = new FileInputStream(filePath+fileName);
                List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(1, 0));

                //生成表头数据
                List<String> list1 = (List<String>) data.get(0);
                List<String> list2 = (List<String>) data.get(1);
                for (int i = 0; i < list1.size(); i++) {
                    Map<String,String> headMap = new HashMap<>();
                    headMap.put("field",list2.get(i));
                    headMap.put("title",list1.get(i));
                    if(i > 0){
                        legendList.add(list1.get(i));
                    }
                    headMapList.add(headMap);
                }

                //生成表数据
                for (int i = 2; i < data.size(); i++) {
                    Map<String,String> dataMap = new HashMap<>();
                    List<String> nameList = (List<String>) data.get(1);
                    List<String> dataList = (List<String>) data.get(i);
                    for (int j = 0; j < dataList.size(); j++) {
                        dataMap.put(nameList.get(j),dataList.get(j));
                        if(nameList.get(j).equals("AREA")){ //区域
                            xAxisList.add(dataList.get(j));
                        }
                    }
                    dataMapList.add(dataMap);
                }

                //3.构造可视化展示数据
                List<String> nameList = (List<String>) data.get(0);
                for (int i = 1; i < nameList.size(); i++) {
                    Map<String,Object> seriesMap = new HashMap<>();
                    List<String> list = new ArrayList<>();
                    seriesMap.put("name",nameList.get(i));
                    seriesMap.put("type","bar");
                    seriesMap.put("stack","yearbook");
                    for (int j = 2; j < data.size(); j++) {
                        List<String> dataList = (List<String>) data.get(j);
                        list.add(dataList.get(i));
                    }
                    seriesMap.put("data",list);
                    seriesMapList.add(seriesMap);
                }

                jsonResult.put("head",headMapList);
                jsonResult.put("data",dataMapList);
                jsonResult.put("legend",legendList);
                jsonResult.put("xAxis",xAxisList);
                jsonResult.put("series",seriesMapList);

                jsonResult.put("fileName",fileName);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if(inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        CommonUtil.retState(jsonResult,"上传并解析",200);
        return jsonResult;
    }

    /**
     * 下载模板数据
     */
    @RequestMapping("/download")
    public String download(HttpServletRequest request, HttpServletResponse response){
        String filePath = "src/main/resources/static/upload/mould/效率评价模板.xlsx";
        String fileName = "效率评价模板";
        CommonUtil.downLoadTempFile(filePath,fileName,response);
        return null;
    }


    /**
     * 获取当前用户目录下的效率评价的数据表
     */
    @ResponseBody
    @RequestMapping("/getDataTables")
    public JsonResult getDataTables(){
        String filePath = "src/main/resources/static/upload/temp/efficiency/"+getLoginUser().getUserName()+"/";
        JsonResult jsonResult = new JsonResult();
        List<String> tableNameList = new ArrayList<>();
        File file = new File(filePath);
        if(file.exists()){
            File[] files = file.listFiles();
            if(files == null || files.length == 0){
                tableNameList = null;
            }else {
                for (File f: files) {
                    tableNameList.add(f.getName());
                }
            }
        }
        Collections.reverse(tableNameList); //倒序

        jsonResult.put("list",tableNameList);
        CommonUtil.retState(jsonResult,200);
        return jsonResult;
    }

    /**
     * 根据表名称选择数据表,读取并将数据返回
     */
    @ResponseBody
    @RequestMapping("/selectTableByName")
    public JsonResult selectTableByName(String tableName){
        JsonResult jsonResult = new JsonResult();
        String filePath = "src/main/resources/static/upload/temp/efficiency/"+getLoginUser().getUserName()+"/";
        //然后读取该表(使用Alibaba的easyExecl解析execl文件)
        InputStream inputStream = null;
        List<Map<String,Object>> areaMapList = new ArrayList<>();

        List<Map<String,Object>> targetMapList = new ArrayList<>();
        List<Map<String,Object>> inputMapList = new ArrayList<>();
        List<Map<String,Object>> outputMapList = new ArrayList<>();
        try {
            if(tableName != null){
                inputStream = new FileInputStream(filePath+tableName);
                List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(1, 0));

                //获取投入产出数据
                List<String> list1 = (List<String>) data.get(0);
                List<String> list2 = (List<String>) data.get(1);
                for (int i = 1; i < list1.size(); i++) {
                    Map<String,Object> dataMap = new HashMap<>();
                    dataMap.put("name",list1.get(i));
                    dataMap.put("logogram",list2.get(i)); //简写

                    targetMapList.add(dataMap);
                }

                //获取区域数据
                for (int i = 2; i < data.size(); i++) {
                    List<String> nameList = (List<String>) data.get(1);
                    List<String> dataList = (List<String>) data.get(i);
                    for (int j = 0; j < dataList.size(); j++) {
                        Map<String,Object> dataMap = new HashMap<>();
                        if(nameList.get(j).equals("AREA")){ //区域
                            dataMap.put("name",dataList.get(j));
                            dataMap.put("value",dataList.get(j));
                            areaMapList.add(dataMap);
                        }
                    }
                }

                jsonResult.put("target",targetMapList);
                jsonResult.put("input",inputMapList);
                jsonResult.put("output",outputMapList);
                jsonResult.put("area",areaMapList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        CommonUtil.retState(jsonResult,200);
        return jsonResult;
    }


    /**
     * 计算
     */
    @ResponseBody
    @RequestMapping("/calculate")
    public JsonResult calculate(String fileName, @RequestParam("funs[]") String[] funs,
                                @RequestParam("input[]") String[] input, @RequestParam("output[]") String[] output, @RequestParam("dmuNames[]") String[] dmuNames,
                                @RequestParam("varCHNames[]") String[] varCHNames,@RequestParam("variableNames[]") String[] variableNames){

        JsonResult jsonResult = new JsonResult();

        DEAVo deaVo = new DEAVo(funs,fileName,dmuNames,varCHNames,variableNames,input,output);

        //根据文件名,获取数据表文件
        String filePath = "src/main/resources/static/upload/temp/efficiency/"+getLoginUser().getUserName()+"/";
        InputStream inputStream = null;
        double[][] matrix = new double[dmuNames.length][variableNames.length];
        try {
            if(fileName != null){
                inputStream = new FileInputStream(filePath+fileName);
                List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(1, 0));


                //根据选择的指标, 查找数据表并构建数据二维数组
                for (int index1 = 0; index1 < dmuNames.length; index1++) {
                    int line = 0; //行号
                    for (int index2 = 2; index2 < data.size(); index2++) {
                        List<String> dataList = (List<String>) data.get(index2);
                        if(dmuNames[index1].equals(dataList.get(0))){
                            line = index2;break;
                        }
                    }

                    //获取第 index1 个 决策变量所在行数据
                    List<String> dataDum = (List<String>) data.get(line);

                    for (int index3 = 0; index3 < variableNames.length; index3++) {
                        List<String> nameList = (List<String>) data.get(1); //简写所在行数据
                        int col = 0;
                        for (int index4 = 0; index4 < nameList.size(); index4++) {
                            if(variableNames[index3].equals(nameList.get(index4))){
                                col = index4;break;
                            }
                        }
                        matrix[index1][index3] = Double.valueOf(dataDum.get(col));
                    }
                }

                deaVo.setMatrix(matrix);

                //调用DEA求解器求解结果
                jsonResult = deaService.calFromSo(deaVo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonResult;
    }


    /**
     * 获取自己目录下的所有数据表
     */
    private List<File> getOwnFiles(){
        String filePath = "src/main/resources/static/upload/temp/efficiency/"+getLoginUser().getUserName()+"/";
        List<File> fileList = new ArrayList<>();
        File file = new File(filePath);
        if(file.exists()){
            File[] files = file.listFiles();
            if(files == null || files.length == 0){
                fileList = null;
            }else {
                for (File f: files) {
                    fileList.add(f);
                }
            }
        }
        Collections.reverse(fileList); //倒序
        return fileList;
    }



    @Override
    public String getErrorPath() {
        return "/error";
    }
}
