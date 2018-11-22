package cn.dmego.odsp.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

/**
 * class_name: Log
 * package: cn.dmego.odsp.system.model
 * describe: 日志表
 * creat_user: Dmego
 * creat_date: 2018/10/29
 * creat_time: 21:29
 **/
@Data
@TableName("sys_log")
public class Log {

    @TableId
    private Integer id; //主键

    private Integer userId; //用户Id

    private String osName; //操作系统

    private String device; //设备型号

    private String browserType; //浏览器类型

    private String ipAddress; //IP地址

    private Date createDate; //登录时间

    @TableField(exist = false)
    private String nickName; //用户昵称

    @TableField(exist = false)
    private String userName; //账号名称

}
