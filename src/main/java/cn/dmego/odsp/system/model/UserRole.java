package cn.dmego.odsp.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

/**
 * class_name: UserRole
 * package: cn.dmego.odsp.system.model
 * describe: 用户角色表
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:25
 **/
@Data
@TableName("sys_user_role")
public class UserRole {

    @TableId
    private Integer id; //主键

    private Integer userId; //用户ID

    private Integer roleId; //角色ID

    private Date createDate; //创建时间

    @TableField(exist = false) //不是表字段
    private String roleName; //角色名称
}
