package cn.dmego.odsp;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class test {

    public static void main(String[] args) {
        Map<Double, String> map = new TreeMap<>(
                new Comparator<Double>() {
                    public int compare(Double obj1, Double obj2) {
                        return obj1.compareTo(obj2);
                    }
                });
        map.put(3.5,"s");
        map.put(3.8,"a");
        map.put(3.2,"d");
        map.put(3.4,"b");

        for (Map.Entry entry: map.entrySet()) {
            System.out.println(entry.getKey() +":"+ entry.getValue());
        }

    }

    private static boolean judgeIfInt(double x) {
        double eps = 1e-10;
        return x - Math.floor(x) < eps;
    }


}
