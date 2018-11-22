package cn.dmego.odsp.common.utils;
/**
 * class_name: StringUtil
 * package: cn.dmego.odsp.common.utils
 * describe: 字符串工具类
 * creat_user: Dmego
 * creat_date: 2018/10/25
 * creat_time: 1:19
 **/
public class StringUtil {

    /**
     * 是否为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        if(str == null || str.isEmpty() || str.replaceAll(" ","").isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * 是否为空 多个字符串
     * @param strs
     * @return
     */
    public static boolean isBlank(String... strs){
        for (int i = 0; i < strs.length; i++) {
            if(isBlank(strs[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否不为空
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }

    /**
     * 如果一个字符串为空 返回 "" 否则返回其本身
     * @param str
     * @return
     */
    public static String getStr(String str){
        return str == null ? "" : str;
    }

    /**
     * 判断字符串中是否包含 key
     * @param str
     * @param key
     * @return
     */
    public static boolean contains(String str, String key){
        if(str != null && str.contains(key)){
            return true;
        }
        return false;
    }

    /**
     * 判断多个字符串是否有包含 key 的
     * @param strs 数组前面都是字符串,最后一个的是 key
     * @return
     */
    public static boolean contains(String... strs){
        for (int i = 0; i < strs.length - 1; i++) {
            if(contains(strs[i],strs[strs.length - 1])){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断一个数组中是否有元素包含 key
     * @param strs
     * @param key
     * @return
     */
    public static boolean contains(String[] strs, String key){
        for (int i = 0; i < strs.length; i++) {
            if(contains(strs[i],key)){
                return true;
            }
        }
        return false;
    }

    /**
     * 将字符串首字母大写
     * @param str
     * @return
     */
    public static String upperHeadChar(String str){
        String head = str.substring(0,1);
        return head.toUpperCase() + str.substring(1,str.length());
    }
}
