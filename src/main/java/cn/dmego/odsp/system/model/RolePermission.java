package cn.dmego.odsp.system.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

/**
 * class_name: RolePermission
 * package: cn.dmego.odsp.system.model
 * describe: 角色权限表
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 21:16
 **/
@Data
@TableName("sys_role_permission")
public class RolePermission {

    @TableId
    private Integer id; //主键,这里也可以使用联合主建

    private Integer roleId; //角色ID

    private Integer permissionId; //权限ID

    private Date createDate; //创建时间
}
