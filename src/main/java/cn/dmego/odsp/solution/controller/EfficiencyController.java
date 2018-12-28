package cn.dmego.odsp.solution.controller;

import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.common.JsonResult;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class EfficiencyController {

    /**
     * 跳转到数据采集页面
     */
    @RequestMapping("/efCollect")
    public String efCollect(Model model){
        return "solution/efficiency/efCollect.html";
    }

    /**
     * 跳转到数据处理页面
     */
    @RequestMapping("/efProcess")
    public String efProcess(Model model){
        return "solution/efficiency/efProcess.html";
    }

    /**
     * 跳转到结果分析页面
     */
    @RequestMapping("/efAnalysis")
    public String efAnalysis(Model model){
        return "solution/efficiency/efAnalysis.html";
    }

    /**
     * 上传文件,解析文件并返回
     */
    @ResponseBody
    @RequestMapping("/upload")
    public JsonResult upload(@RequestParam("file") MultipartFile file){

        JsonResult jsonResult = new JsonResult();
        //1.先将文件保存
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //重新生成文件名
        fileName = UUID.randomUUID()+suffixName;

        String filePath = "src/main/resources/static/upload/temp/";
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath+fileName);
            out.write(file.getBytes());
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.error("文件上传失败!");
        }

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
     * 下载模板数据
     */
    @RequestMapping("/download")
    public String download(HttpServletRequest request, HttpServletResponse response){
        File file = new File("src/main/resources/static/upload/mould/Efficienty_Template_Data.xlsx");
        if(file.exists()){
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=Efficienty_Template_Data.xlsx");// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1){
                    os.write(buffer,0,i);
                    i = bis.read(buffer);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(bis != null){
                    try {
                        bis.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                if(fis != null){
                    try {
                        fis.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }



}
