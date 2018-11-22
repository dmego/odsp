package cn.dmego.odsp.common.exception;

import lombok.Data;

/**
 * class_name: BusinessException
 * package: cn.dmego.odsp.common.exception
 * describe: 业务异常类
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 16:56
 **/
@Data
public class BusinessException extends IException{

    public BusinessException(){
        super();
    }

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(Integer code,String message){
        super(code,message);
    }

    @Override
    public Integer getCode() {
        Integer code  = super.getCode();
        if(code == null){
            code = 500;
        }
        return code;
    }

    @Override
    public String getMessage() {
      String message = super.getMessage();
      if(message == null){
          message = "系统繁忙";
      }
      return message;
    }
}
