package cn.dmego.odsp.algorithms.model;

import lombok.Data;

/**
 * class_name: Graph
 * package: cn.dmego.odsp.algorithms.model
 * describe: 图
 * creat_user: Dmego
 * creat_date: 2018/12/13
 * creat_time: 4:23
 **/
@Data
public class Graph {

    private static double INF = Double.MAX_VALUE; //权值最大值
    private EData[] edges; //边数组

    //邻接表中对应的链表顶点
    private class ENode{
        int ivex;      //该边所指向顶点的位置
        double weight;    //该边的权
        ENode nextEdge;//指向下一条弧的指针
    }

    // 邻接表中表的顶点
    private class VNode {
        String data;          // 顶点信息
        ENode firstEdge;    // 指向第一条依附该顶点的弧
    }

    private int mEdgNum;    // 边的数量
    private VNode[] mVexs;  // 顶点数组


    public Graph(String[] vexs, EData[] edges,int gType) {

        this.edges = edges;

        // 初始化"顶点数"和"边数"
        int vlen = vexs.length;
        int elen = edges.length;

        // 初始化"顶点"
        mVexs = new VNode[vlen];
        for (int i = 0; i < mVexs.length; i++) {
            mVexs[i] = new VNode();
            mVexs[i].data = vexs[i];
            mVexs[i].firstEdge = null;
        }

        // 初始化"边"
        mEdgNum = elen;
        for (int i = 0; i < elen; i++) {
            // 读取边的起始顶点和结束顶点
            String c1 = edges[i].getStart();
            String c2 = edges[i].getEnd();
            double weight = edges[i].getWeight();

            // 读取边的起始顶点和结束顶点
            int p1 = getPosition(c1);
            int p2 = getPosition(c2);
            // 初始化node1
            ENode node1 = new ENode();
            node1.ivex = p2;
            node1.weight = weight;
            // 将node1链接到"p1所在链表的末尾"
            if(mVexs[p1].firstEdge == null)
                mVexs[p1].firstEdge = node1;
            else
                linkLast(mVexs[p1].firstEdge, node1);

            //如果是无向图,头和尾节点都链接到头节点链表.如果是有向图,只需要将尾节点链接到头节点链表(1:有向图,2:无向图)
            if(gType == 2){
                // 初始化node2
                ENode node2 = new ENode();
                node2.ivex = p1;
                node2.weight = weight;
                // 将node2链接到"p2所在链表的末尾"
                if(mVexs[p2].firstEdge == null)
                    mVexs[p2].firstEdge = node2;
                else
                    linkLast(mVexs[p2].firstEdge, node2);
            }
        }
    }

    /*
     * 将node节点链接到list的最后
     */
    private void linkLast(ENode list, ENode node) {
        ENode p = list;
        while(p.nextEdge!=null)
            p = p.nextEdge;
        p.nextEdge = node;
    }

    /*
     * 通过顶点名称返回顶点所在表头节点表中的下标
     */
    public int getPosition(String ch) {
        for(int i=0; i<mVexs.length; i++)
            if(mVexs[i].data.equals(ch))
                return i;
        return -1;
    }
    /*
     * 获取i的终点
     */
    public int getEnd(int[] vends, int i) {
        while (vends[i] != 0)
            i = vends[i];
        return i;
    }


    public String getByIndex(int i){
        return mVexs[i].data;
    }

    /*
     * 获取边 (start,end) 的权值,如果不连通,则返回无穷大
     */
    public double getWeight(int start, int end){
        if(start == end)
            return 0;
        ENode node = mVexs[start].firstEdge;
        while (node != null){
            if(end == node.ivex)
                return node.weight;
            node = node.nextEdge;
        }
        return INF;
    }

    /*
     * 深度优先搜索遍历图的递归实现
     */
    private void DFS(int i, boolean[] visited) {
        ENode node;
        visited[i] = true;
        node = mVexs[i].firstEdge;
        while (node != null) {
            if (!visited[node.ivex])
                DFS(node.ivex, visited);
            node = node.nextEdge;
        }
    }

    /*
     * 深度优先搜索遍历图
     * 返回连通分量,判断图是否连通
     */
    public int DFS() {
        boolean[] visited = new boolean[mVexs.length];// 顶点访问标记

        // 初始化所有顶点都没有被访问
        for (int i = 0; i < mVexs.length; i++)
            visited[i] = false;
        int count = 0;
        for (int i = 0; i < mVexs.length; i++) {
            if (!visited[i])
                count++;
            DFS(i, visited);
        }
        return count;
    }

}
