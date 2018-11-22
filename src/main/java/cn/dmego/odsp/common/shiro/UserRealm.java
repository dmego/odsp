package cn.dmego.odsp.common.shiro;

import cn.dmego.odsp.common.utils.StringUtil;
import cn.dmego.odsp.system.model.Permission;
import cn.dmego.odsp.system.model.Role;
import cn.dmego.odsp.system.model.User;
import cn.dmego.odsp.system.service.PermissionService;
import cn.dmego.odsp.system.service.RoleService;
import cn.dmego.odsp.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * class_name: UserRealm
 * package: cn.dmego.odsp.common.shiro
 * describe: Shiro 认证与授权
 * creat_user: Dmego
 * creat_date: 2018/10/25
 * creat_time: 0:34
 **/
public class UserRealm extends AuthorizingRealm{

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private RoleService roleService;

    @Autowired
    @Lazy
    private PermissionService permissionService;

    /**
     * 用于用户授权的方法
     * @param principals 主身份信息
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用 shiro 权限控制有三种
     *      1、通过 xml 配置资源的权限
     *      2、通过 shiro 标签控制权限
     *      3、通过 shiro 注解控制权限
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //获取用户所拥有的角色code集合
        List<Role> roleList = roleService.getByUserId(user.getUserId());
        Set<String> roles = new HashSet<>();
        for (int i = 0; i < roleList.size(); i++) {
            roles.add(String.valueOf(roleList.get(i).getRoleId()));
        }
        authorizationInfo.setRoles(roles);

        //获取用户所有的权限 code 集合
        List<Permission> permissionList = permissionService.getByUserId(user.getUserId());
        Set<String> permissions = new HashSet<>();
        for (int i = 0; i < permissionList.size(); i++) {
            //获取权限标记
            String mark = permissionList.get(i).getMark();
            if(StringUtil.isNotBlank(mark)){
                permissions.add(mark);
            }
        }
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 用于用户认证的方法
     * @param token 用户输入的用户名和密码
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal(); //获取到用户名
        User user = userService.getByUserName(username); //根据用户名查询出该用户
        if(user == null){
            throw new UnknownAccountException(); //如果用户不存在,抛出账号不存在异常
        }
        if(user.getState() != 0){
            throw new LockedAccountException(); //如果账号被锁,抛出账号被锁异常
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,user.getPassword(), ByteSource.Util.bytes(user.getUserName()),getName());
        return authenticationInfo;
    }
}
