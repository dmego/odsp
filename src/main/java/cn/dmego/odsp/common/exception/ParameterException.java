package cn.dmego.odsp.common.exception;

/**
 * class_name: ParameterException
 * package: cn.dmego.odsp.common.exception
 * describe: 参数异常类
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 17:07
 **/
public class ParameterException extends IException{

    public ParameterException() {
        super();
    }

    public ParameterException(String massage) {
        super(massage);
    }

    public ParameterException(Integer code, String message) {
        super(code, message);
    }

    @Override
    public Integer getCode() {
        Integer code = super.getCode();
        if(code == null){
            code = 400;
        }
        return code;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if(message == null){
            message = "参数错误";
        }
        return message;
    }
}

