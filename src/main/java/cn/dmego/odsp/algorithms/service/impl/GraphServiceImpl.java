package cn.dmego.odsp.algorithms.service.impl;

import cn.dmego.odsp.algorithms.model.EData;
import cn.dmego.odsp.algorithms.model.Graph;
import cn.dmego.odsp.algorithms.service.GraphService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.GraphVo;
import cn.dmego.odsp.common.JsonResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class_name: GraphServiceImpl
 * package: cn.dmego.odsp.algorithms.service.impl
 * describe: 图与网络分析 Service 实现类
 * creat_user: Dmego
 * creat_date: 2018/12/6
 * creat_time: 23:33
 **/
@Service
public class GraphServiceImpl implements GraphService {


    @Override
    public JsonResult calculate(GraphVo graphVo) {
        Integer fun = graphVo.getFun();
        JsonResult jsonResult = new JsonResult();

        Graph graph = graphVo.getGraph();
        EData[] edges = graph.getEdges();
        Integer arcNum = graphVo.getArcNum(); //边弧的数量
        String start = graphVo.getStart();
        String end = graphVo.getEnd();

        List<Map<String, String>> mapList = new ArrayList<>();
        if(graph.DFS() != 1){
            jsonResult = JsonResult.error(500, "不能构成连通图!请调整输入数据!");
            return jsonResult;
        }else{
            if(fun == 1){  //最短路径

            }else if(fun == 2){ //最小生成树
                mapList = kruskal(arcNum,edges,graph,start,end);
            }else if(fun == 3){ //最大流

            }else if(fun == 4){ //最小费用最大流

            }
        }

        jsonResult.put("result",mapList);
        CommonUtil.retState(jsonResult,200);
        return jsonResult;
    }

    /**
     * 对边按照权值从小到大排序
     */
    private static void sortEdges(EData[] edges,int elen){
        for (int i = 0; i < elen; i++) {
            for (int j = i+1; j < elen; j++) {
                if(edges[i].getWeight() > edges[j].getWeight()){
                    //交换位置上的值,将最小的值移到最数组前面
                    EData temp = edges[i];
                    edges[i] = edges[j];
                    edges[j] = temp;
                }
            }
        }
    }

    /**
     * 克鲁斯卡尔计算最小生成树
     */
    private static List<Map<String, String>> kruskal(Integer arcNum, EData[] edges, Graph graph, String start, String end){
        int index = 0; //rets数组的索引
        int[] vends = new int[arcNum]; //辅助数组, 用于保存 "已有最小生成树" 中每个顶点在该最小生成中的终点
        EData[] rets = new EData[arcNum]; //结果数组,保存最小生成树的边
        double weights = 0.0; //最小生成树的距离
        //1.对边数组进行排序
        sortEdges(edges,arcNum);

        //2.for循环, 对已排好序的边依次加入到结果数组中
        for (int i = 0; i < arcNum; i++) {
            int s = graph.getPosition(edges[i].getStart()); //获取第 i 条边起点的序号
            int e = graph.getPosition(edges[i].getEnd()); //获取第 i 条边终点的序号

            int m = graph.getEnd(vends, s); //获取 s 在 已有最小生成树 中的终点
            int n = graph.getEnd(vends, e); //获取 e 在 已有最小生成树 中的终点
            if(m != n){ //如果 m == n ,说明两个终点相等,构成了环
                vends[m] = n; //将两个顶点的终点设置相同,说明两个顶点在一个连通分量中
                rets[index++] = edges[i]; //将该边加入到结果数组中去
                weights+=edges[i].getWeight();
            }
        }

        //3.最小生成树求得的结果封装成List<Map<String, String>>并返回
        List<Map<String,String>> mapList = new ArrayList<>();
        for (int i = 0; i <= index; i++) {
            Map<String,String> map = new HashMap<>();
            if(i == index){
                map.put("start", "此问题的最小生成树距离");
                map.put("end", String.valueOf(weights));
                map.put("distance", "");
            }else{
                map.put("start", rets[i].getStart());
                map.put("end", rets[i].getEnd());
                map.put("distance", String.valueOf(rets[i].getWeight()));
            }
            mapList.add(map);
        }
        return mapList;
    }
}
