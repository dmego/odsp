package cn.dmego.odsp.common.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * class_name: EndecryptUtil
 * package: cn.dmego.odsp.common.shiro
 * describe: 加密解密工具类 (依赖 shiro 的加密)
 * creat_user: Dmego
 * creat_date: 2018/10/24
 * creat_time: 15:36
 **/
public class EndecryptUtil {

    /**
     * md5 加密(没有盐值)
     * @param password 要加密的密码字符串
     * @return
     */
    public static String encrytMd5(String password){
        return new Md5Hash(password).toHex();
    }

    /**
     * 指定盐值 md5 加密
     * @param password
     * @param salt
     * @param hashInterations
     * @return
     */
    public static String encrytMd5(String password,String salt, int hashInterations){
        return new Md5Hash(password,salt,hashInterations).toHex();
    }


}
