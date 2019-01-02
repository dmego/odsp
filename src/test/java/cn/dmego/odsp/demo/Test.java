package cn.dmego.odsp.demo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Test {
    public static void main(String[] args) {
        Map<String, Integer> map = new TreeMap<String, Integer>();
        map.put("a", 2);
        map.put("c", 3);
        map.put("d", 3);
        map.put("b", 1);

        //这里将map.entrySet()转换成list
        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
            //升序排序
            public int compare(Entry<String, Integer> o1,
                               Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });

        for(Map.Entry<String,Integer> mapping:list){
            System.out.println(mapping.getKey()+":"+mapping.getValue());
        }
    }
}
