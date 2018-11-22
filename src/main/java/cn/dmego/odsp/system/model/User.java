package cn.dmego.odsp.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * class_name: User
 * package: cn.dmego.odsp.system.model
 * describe: 用户表
 * creat_user: Dmego
 * creat_date: 2018/10/21
 * creat_time: 22:01
 **/
@Data
@TableName("sys_user")
public class User {

    @TableId
    private Integer userId; //主键ID

    private String userName; //姓名

    private String password; //密码

    private String nickName; //昵称

    private String avatar; //头像路径

    private String sex; //性别

    private String phone; //电话

    private String email; //邮箱

    private Integer state; //状态

    private Date createDate; //创建时间

    private Date updateDate; //更新时间

    @TableField(exist = false)//表示不是表字段
    private List<Role> roles; //角色

}
