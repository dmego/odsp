package cn.dmego.odsp.system.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;


/**
 * class_name: Role
 * package: cn.dmego.odsp.system.model
 * describe: 角色表
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 20:48
 **/
@Data
@TableName("sys_role")
public class Role {

    @TableId
    private Integer roleId; //角色ID

    private String roleName; //角色名称

    private String describes; //角色描述

    private int status; //角色状态

    private Date createDate; //创建日期

    private Date updateDate; //更新日期

    public  Role(){}

    public Role(Integer roleId){
        setRoleId(roleId);
    }

    public Role(Integer roleId, String roleName) {
        setRoleId(roleId);
        setRoleName(roleName);
    }
}
