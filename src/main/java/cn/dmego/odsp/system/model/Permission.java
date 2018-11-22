package cn.dmego.odsp.system.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

/**
 * class_name: Permission
 * package: cn.dmego.odsp.system.model
 * describe: 权限表
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:05
 **/
@Data
@TableName("sys_permission")
public class Permission {

    @TableId
    private Integer permissionId; //权限ID

    private String permissionName; //权限名称

    private String mark; //权限标识(如果为空不会添加在 shiro 的权限列表中)

    private String menuUrl; //菜单URL

    private Integer parentId; //上级菜单

    private Integer isMenu; //是否为菜单

    private Integer orderNumber; //排序号

    private String menuIcon; //菜单图标

    private Date createDate; //创建时间

    private Date updateDate;//更新时间
}
