package cn.dmego.odsp.common;

import java.util.HashMap;

/**
 * class_name: JsonResult
 * package: cn.dmego.odsp.common
 * describe: 返回 JSON 结果对象
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 22:02
 **/
public class JsonResult extends HashMap<String,Object> {

    public JsonResult() {}

    /**
     * 返回成功
     * @return
     */
    public static JsonResult ok(){
        return ok("操作成功");
    }

    /**  ,
     * 返回成功
     * @param message
     * @return
     */
    public static JsonResult ok(String message){
        return ok(200,message);
    }

    /**
     * 返回成功
     * @param code
     * @param message
     * @return
     */
    public static JsonResult ok(int code,String message){
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("code",code);
        jsonResult.put("msg",message);
        return jsonResult;
    }

    /**
     * 返回失败
     * @return
     */
    public static JsonResult error(){
        return error("操作失败");
    }

    /**
     * 返回失败
     * @param message
     * @return
     */
    public static JsonResult error(String message){
        return error(500,message);
    }

    /**
     * 返回失败
     * @param code
     * @param message
     * @return
     */
    public static JsonResult error(int code,String message){
        return ok(code,message);
    }

    /**
     * 设置 code
     * @param code
     * @return
     */
    public JsonResult setCode(int code){
        super.put("code",code);
        return this;
    }

    /**
     * 设置 message
     * @param message
     * @return
     */
    public JsonResult setMessage(String message){
        super.put("msg",message);
        return this;
    }

    /**
     * 重写 put 方法, 返回一个 JOSNresut 对象
     * @param key
     * @param value
     * @return
     */
    @Override
    public JsonResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
