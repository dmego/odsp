package cn.dmego.odsp.common.exception;

/**
 * class_name: IException
 * package: cn.dmego.odsp.common.exception
 * describe: 自定义异常类
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 16:51
 **/
public abstract class IException extends RuntimeException{

    private Integer code;

    public IException(){}

    public IException(String massage){
        super(massage);
    }

    public IException(Integer code,String message){
        super(message);
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }

    public void setCode(Integer code){
        this.code = code;
    }


}
