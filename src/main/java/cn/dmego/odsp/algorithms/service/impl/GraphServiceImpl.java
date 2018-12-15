package cn.dmego.odsp.algorithms.service.impl;

import cn.dmego.odsp.algorithms.model.EData;
import cn.dmego.odsp.algorithms.model.Graph;
import cn.dmego.odsp.algorithms.service.GraphService;
import cn.dmego.odsp.algorithms.utils.CommonUtil;
import cn.dmego.odsp.algorithms.vo.GraphVo;
import cn.dmego.odsp.common.JsonResult;
import org.springframework.stereotype.Service;

import java.util.*;

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

    private static double INF = Double.MAX_VALUE; //权值最大值

    @Override
    public JsonResult calculate(GraphVo graphVo) {
        Integer fun = graphVo.getFun();
        JsonResult jsonResult = new JsonResult();

        Graph graph = graphVo.getGraph();
        EData[] edges = graph.getEdges();
        Integer arcNum = graphVo.getArcNum(); //边弧的数量
        Integer vexNum = graphVo.getVexNum(); //顶点的数量
        String start = graphVo.getStart();
        String end = graphVo.getEnd();

        List<Map<String, String>> mapList = new ArrayList<>();
        System.out.println(graph.DFS());
        if(graph.DFS() != 1){
            jsonResult = JsonResult.error(500, "不能构成连通图!请调整输入数据!");
            return jsonResult;
        }else{
            if(fun == 1){  //最短路径
                mapList = dijkstra(vexNum,edges,graph,start,end);
            }else if(fun == 2){ //最小生成树
                mapList = kruskal(arcNum,edges,graph);
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
    private static List<Map<String, String>> kruskal(Integer arcNum, EData[] edges, Graph graph){
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

    /**
     * 迪杰斯特拉求最短路径
     */
    private static List<Map<String, String>> dijkstra(Integer vexNum, EData[] edges, Graph graph,String start,String end){

        int[] prev = new int[vexNum]; //前驱顶点数组,prev[i] 是起点到顶点 i 的最短路径中, i 顶点的前一个顶点
        double[] dist = new double[vexNum]; //距离数组, 起点到 i 顶点的最短路径长度
        boolean[] flag = new boolean[vexNum]; //flag[i] = true 表示起点到 i 顶点的最短路径已经成功获取

        EData[] rets = new EData[vexNum]; //结果数组,保存最短路径的边
        double weights = 0.0; //最短距离

        //1.先获取起点和终点下标,然后进行初始化
        int s = graph.getPosition(start); //起点在表头节点表中的下标
        int e = graph.getPosition(end); //终点在表头节点表中的下标
        for (int i = 0; i < vexNum; i++) {
            flag[i] = false; //初始化没有找到最短路径
            prev[i] = s; //初始化前驱都为起点
            dist[i] = graph.getWeight(s,i); //起点到顶点 i 的最短路径初始化为边 (s,i) 的权,不连通为无穷大
        }
        //初始化自己到自己
        flag[s] = true;
        dist[s] = 0;

        //2.遍历,每次找出一个顶点的最短路径
        int k = 0;
        double min;
        for (int i = 1; i < vexNum; i++) {
            //在没有找到最短路径的节点中,找到离起点最近的顶点 k
            min = INF; //**关键点,
            for (int j = 0; j < vexNum; j++) {
                if(!flag[j] && dist[j] < min){
                    min = dist[j];
                    k = j;
                }
            }
            //标记 k 已经获取最短路径
            flag[k] = true;
            //更新没有找到最短路径的顶点的最短路径和前驱顶点
            for (int j = 0; j < vexNum; j++) {
                double temp = graph.getWeight(k,j); //获取边 (k,j)的权
                temp = (temp == INF ? INF : (min + temp)); //为了防止溢出
                if(!flag[j] && temp < dist[j]){ //如果 k顶点 和 j 顶点没有找到最短路径
                    dist[j] = temp;
                    prev[j] = k;
                }
            }
        }

        //3.获取起点到终点自己的最短路径距离和中间边
        for (int i = 0; i < vexNum; i++) {
            if(i == e){
                weights = dist[i]; //最短路径的距离
            }
        }

        int t = prev[e]; //终点的前驱
        List<Integer> list = new ArrayList<>(); //路径数组,倒序
        list.add(e);
        while(t != s) {
            list.add(t);
            t= prev[t];
        }
        list.add(s);

        //4.最短路径求得的结果封装成List<Map<String, String>>并返回
        List<Map<String,String>> mapList = new ArrayList<>();
        for (int i = list.size() - 1; i >=0 ; i--) {
            Map<String,String> map = new HashMap<>();
            if(i == 0){
                map.put("start", "从节点"+start+"到节点"+end+"的最短路径距离");
                map.put("end", String.valueOf(weights));
                map.put("distance", "");
            }else{
                map.put("start", graph.getByIndex(list.get(i)));
                map.put("end", graph.getByIndex(list.get(i-1)));
                map.put("distance", String.valueOf(graph.getWeight(list.get(i),list.get(i-1))));
            }
            mapList.add(map);
        }
        return mapList;
    }


}
