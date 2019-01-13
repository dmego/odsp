package cn.dmego.odsp.solution.controller;

import cn.dmego.odsp.algorithms.service.RiskService;
import cn.dmego.odsp.algorithms.service.UncertainService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.DecisionVo;
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
import java.util.*;

/**
 * class_name: DecisonController
 * package: cn.dmego.odsp.solution.controller
 * describe: 策略决策 Controller
 * creat_user: Dmego
 * creat_date: 2019/1/6
 * creat_time: 20:52
 **/
@Controller
@RequestMapping("/solution/decision")
public class DecisionController extends BaseController implements ErrorController {

    @Autowired
    private UncertainService uncertainService;

    @Autowired
    private RiskService riskService;

    /**
     * 跳转到案例简介页面
     */
    @RequestMapping("/deIntro")
    public String deIntro(){
        return "solution/decision/deIntro.html";
    }

    /**
     * 跳转到数据采集页面
     */
    @RequestMapping("/deCollect")
    public String deCollect(){
        return "solution/decision/deCollect.html";
    }

    /**
     * 跳转到数据处理页面
     */
    @RequestMapping("/deAnalysis")
    public String deAnalysis(){
        return "solution/decision/deAnalysis.html";
    }


    /**
     * 下载模板数据
     */
    @RequestMapping("/download")
    public String download(HttpServletRequest request, HttpServletResponse response){
        String filePath = "src/main/resources/static/upload/mould/策略决策模板.xlsx";
        String fileName = "策略决策模板";
        CommonUtil.downLoadTempFile(filePath,fileName,response);
        return null;
    }

    /**
     * 生成分析报告
     */
    @RequestMapping("/report")
    public String report(){
        return "solution/decision/deReport.html";
    }


    /**
     * 上传策略决策模板,解析文件,计算优化结果,将解析数据与优化数据返回
     */
    @ResponseBody
    @RequestMapping("/upload")
    public JsonResult upload(@RequestParam("file") MultipartFile file) {
        String filePath = "src/main/resources/static/upload/temp/decision/" + getLoginUser().getUserName() + "/";
        JsonResult jsonResult = new JsonResult();

        //存储上传的文件,返回存储的文件名
        String fileName = CommonUtil.saveFile(filePath, file);
        //使用EasyExcel读取文件并返回数据
        List<Object> sheet1 = CommonUtil.easyExcel(filePath+fileName,1); //sheet1的数据

        //2.解析读取的数据
        List<Map<String,Object>> headMapList1 = new ArrayList<>(); //一级表头
        List<Map<String,Object>> headMapList2 = new ArrayList<>(); //二级表头
        List<Map<String,Object>> headMapList3 = new ArrayList<>(); //三级表头
        List<Map<String,Object>> headMapList4 = new ArrayList<>(); //四级表头

        List<Map<String,Object>> dataMapList = new ArrayList<>(); //表数据

        double happy = 0.0; //乐观系数
        double utility = 0.0; //效用曲线系数


        //生成原始数据的表头
        List<String> list1 = (List<String>) sheet1.get(0); //读取第一行数据
        headMapList1.add(rendHead("strategy",list1.get(0),4,1));
        headMapList1.add(rendHead("sale",list1.get(1),1,5));

        List<String> list2 = (List<String>) sheet1.get(1); //读取第二行数据
        String[] stateName = new String[list2.size() -1]; //销售状况
        for (int i = 1; i < list2.size(); i++) {
            stateName[i-1] = list2.get(i);
            headMapList2.add(rendHead("saleNum"+i,list2.get(i),1,1));
        }

        List<String> list3 = (List<String>) sheet1.get(2); //读取第三行数据
        headMapList3.add(rendHead("chance",list3.get(1),1,5));

        List<String> list4 = (List<String>) sheet1.get(3); //读取第四行数据
        double[] chance = new double[list4.size()-1]; //概率数组
        for (int i = 1; i < list4.size(); i++) {
            chance[i-1] = Double.parseDouble(list4.get(i));
            headMapList4.add(rendHead("chanceNum"+i,list4.get(i),1,1));
        }

        int state = list2.size() -1;
        int action = sheet1.size() -6; //(前4行是表头,后两行是乐观系数和效用曲线系数)

        String[] actionName = new String[action]; //策略名称
        Double[][] matrix = new Double[action][state];
        //生成原始数据表数据
        for (int i = 4; i < sheet1.size(); i++) {
            List<String> list = (List<String>) sheet1.get(i);
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("strategy",list.get(0)); //第一列是策略名称
            if(i < sheet1.size() - 2){
                actionName[i-4] = list.get(0);
            }
            if(list.get(0).equals("乐观系数")){
                happy = Double.parseDouble(list.get(1));
            }
            if(list.get(0).equals("效用曲线系数")) {
                utility = Double.parseDouble(list.get(1));
            }
            for (int j = 1; j < list.size(); j++) {
                String jdata = list.get(j);
                if(i < sheet1.size() - 2){
                    matrix[i-4][j-1] = Double.parseDouble(jdata);
                }
                if(jdata == null){
                    jdata = "";
                }
                dataMap.put("saleNum"+j,jdata);
            }

            dataMapList.add(dataMap);
        }

        Map<String,Object> decOriData = new HashMap<>(); //计算前的结果数据
        Map<String,Object> decResData = new HashMap<>(); //计算后的结果数据

        decOriData.put("head1",headMapList1);
        decOriData.put("head2",headMapList2);
        decOriData.put("head3",headMapList3);
        decOriData.put("head4",headMapList4);
        decOriData.put("data",dataMapList);
        decOriData.put("fileName",fileName);


        ///////////////////////////
        //生成原始数据展示图数据     //
        //////////////////////////
        String[] legend = new String[action];
        for (int i = 0; i < action; i++) {
            legend[i] = "生产 "+actionName[i]+" (万台)";
        }
        String[] xAxis = new String[state];
        for (int i = 0; i < state; i++) {
            xAxis[i] = "销售 "+stateName[i]+" (万台)";
        }

       List<Map<String,Object>> oriSeriesData = new ArrayList<>();
        for (int i = 0; i < action; i++) {
            Map<String,Object> map = new HashMap<>();
            double[] data = new double[state];
            for (int j = 0; j < state; j++) {
                data[j] = matrix[i][j];
            }
            map.put("name", "生产 "+actionName[i]+" (万台)");
            map.put("type","bar");
            map.put("yAxisIndex",0);
            map.put("data",data);
            oriSeriesData.add(map);
        }
        Map<String,Object> chanceMap = new HashMap<>();
        chanceMap.put("name","销售概率");
        chanceMap.put("type","line");
        chanceMap.put("yAxisIndex",1);
        chanceMap.put("data",chance);
        oriSeriesData.add(chanceMap);

        decOriData.put("legend",legend);
        decOriData.put("xAxis",xAxis);
        decOriData.put("series",oriSeriesData);


        //////////////////////////////////
        //计算结果并返回保存到浏览器session中//
        /////////////////////////////////

        //先计算不确定型决策法
        DecisionVo uncertainVo = new DecisionVo();
        uncertainVo.setFunctions(Arrays.asList(1,2,3,4,5));//方法数组
        uncertainVo.setAction(action);
        uncertainVo.setState(state);
        uncertainVo.setHopeful(happy); //乐观系数
        uncertainVo.setMatrix(matrix); //矩阵数组

        JsonResult uncertainRes =  uncertainService.calculate(uncertainVo); //计算结果

        Map<String,Integer> bestUncertain = (Map<String, Integer>) uncertainRes.get("bestMap");//最优策略Map

        //再计算风险决策法
        Double[][] matrix2 = new Double[action+1][state];
        for (int i = 0; i < action + 1; i++) {
            for (int j = 0; j < state; j++) {
                if(i == 0){
                    matrix2[i][j] = chance[j];
                }else {
                    matrix2[i][j] = matrix[i-1][j];
                }
            }
        }

        DecisionVo riskVo = new DecisionVo();
        riskVo.setFunctions(Arrays.asList(1,2,3,4));//方法数组
        riskVo.setAction(action);
        riskVo.setState(state);
        riskVo.setUtility(utility);//效用曲线系数
        riskVo.setMatrix(matrix2); //矩阵数组

        JsonResult riskRes = riskService.calculate(riskVo);

        Map<String,Integer> bestRisk = (Map<String, Integer>) riskRes.get("bestMap");//最优策略Map


        ///////////////////////////
        //构建最优策略图示数据     //
        //////////////////////////
        Map<String,Object> graph1 = new HashMap<>();

        String[] aCategorys = new String[4+5+2];
        aCategorys[0] = "";
        aCategorys[1] = "悲观决策法";aCategorys[2] = "乐观决策法";aCategorys[3] = "最小机会损失准则";aCategorys[4] = "等概率准则决策";aCategorys[5] = "折中主义准则";
        aCategorys[6] = "EMV决策法";aCategorys[7] = "EOL决策法";aCategorys[8] = "EPVI决策法";aCategorys[9] = "EUV决策法";
        aCategorys[10] = "";

        //y轴为销售状况
        //x轴为决策模型
        String[] aSeriesData = new String[4+5+2];
        aSeriesData[0] = "";
        aSeriesData[1] = actionName[bestUncertain.get("maxMin")];
        aSeriesData[2] = actionName[bestUncertain.get("maxMax")];
        aSeriesData[3] = actionName[bestUncertain.get("savage")];
        aSeriesData[4] = actionName[bestUncertain.get("laplace")];
        aSeriesData[5] = actionName[bestUncertain.get("eclecticism")];

        aSeriesData[6] = actionName[bestRisk.get("EMV")];
        aSeriesData[7] = actionName[bestRisk.get("EOL")];
        aSeriesData[8] = actionName[bestRisk.get("EPVI")];
        aSeriesData[9] = actionName[bestRisk.get("EUV")];
        aSeriesData[10] = "";

        graph1.put("aCategorys",aCategorys);
        graph1.put("aSeriesData",aSeriesData);

        ///////////////////////////
        //构建策略占比图示数据     //
        //////////////////////////
        Map<String,Object> graph2 = new HashMap<>();
        List<Map<String,Object>> graph2Series = new ArrayList<>();
        String[] graph2Legend = new String[action];
        int[] proportion = new int[action];
        int[] uncertPro = new int[action];
        int[] riskPro = new int[action];
        for (int i = 1; i < aSeriesData.length-1; i++) {
            for (int j = 0; j < action; j++) {
                if(aSeriesData[i].equals(actionName[j])){
                    proportion[j]++;
                }
                if(i <= 5){
                    if(aSeriesData[i].equals(actionName[j])){
                        uncertPro[j]++;
                    }
                }else {
                    if(aSeriesData[i].equals(actionName[j])){
                        riskPro[j]++;
                    }
                }
            }
        }
        for (int i = 0; i < action; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name","生产 "+actionName[i]+" (万台)");
            graph2Legend[i] = "生产 "+actionName[i]+" (万台)";
            map.put("value",CommonUtil.retainDecimal((proportion[i] / 9.0)*100,2));
            graph2Series.add(map);
        }

        graph2.put("series",graph2Series);
        graph2.put("legend",graph2Legend);




        ///////////////////////////
        //构建策略分布分析         //
        //////////////////////////
        Map<String,Object> graph3 = new HashMap<>();
        String[][] tempArr = new String[action][9];
        for (int i = 0; i < action; i++) {

            if(bestUncertain.get("maxMin").equals(i))
                tempArr[i][0] = actionName[i];
            else
                tempArr[i][0] = "";
            if(bestUncertain.get("maxMax").equals(i))
                tempArr[i][1] = actionName[i];
            else
                tempArr[i][1] = "";
            if(bestUncertain.get("savage").equals(i))
                tempArr[i][2] = actionName[i];
            else
                tempArr[i][2] = "";
            if(bestUncertain.get("laplace").equals(i))
                tempArr[i][3] = actionName[i];
            else
                tempArr[i][3] = "";
            if(bestUncertain.get("eclecticism").equals(i))
                tempArr[i][4] = actionName[i];
            else
                tempArr[i][4] = "";
            if(bestRisk.get("EMV").equals(i))
                tempArr[i][5] = actionName[i];
            else
                tempArr[i][5] = "";
            if(bestRisk.get("EOL").equals(i))
                tempArr[i][6] = actionName[i];
            else
                tempArr[i][6] = "";
            if(bestRisk.get("EPVI").equals(i))
                tempArr[i][7] = actionName[i];
            else
                tempArr[i][7] = "";
            if( bestRisk.get("EUV").equals(i))
                tempArr[i][8] = actionName[i];
            else
                tempArr[i][8] = "";
        }


        List<String> list = Arrays.asList(aCategorys);
        List<String> graph3XAsix = new ArrayList<>();
        if(list.contains("")){
            graph3XAsix = new ArrayList<String>(list);
            graph3XAsix.remove("");
        }

        Map<String,Object> seriesData = new HashMap<>();
        for (int i = 0; i < action; i++) {
            String[][] dataArr = new String[4+5][2];
            for (int j = 0; j < 9; j++) {
                dataArr[j][0] = graph3XAsix.get(j);
                dataArr[j][1] = tempArr[i][j];
            }
            seriesData.put(actionName[i],dataArr);
        }

        graph3.put("legend",graph2Legend);
        graph3.put("xAsix",graph3XAsix);
        graph3.put("seriesData",seriesData);


        int unb = CommonUtil.bigValueIndex(uncertPro);
        int rib = CommonUtil.bigValueIndex(riskPro);
        int bestb = unb > rib  ?  unb : rib;


        decResData.put("graph1",graph1);
        decResData.put("graph2",graph2);
        decResData.put("graph3",graph3);
        decResData.put("unb","生产 "+actionName[unb]+" (万台)");
        decResData.put("rib","生产 "+actionName[rib]+" (万台)");
        decResData.put("bestb","生产 "+actionName[bestb]+" (万台)");

        ///////////////////////////
        //返回JSONResult数据      //
        //////////////////////////
        jsonResult.put("decOriData",decOriData);
        jsonResult.put("decResData",decResData);

        CommonUtil.retState(jsonResult,200);
        return jsonResult;
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

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
