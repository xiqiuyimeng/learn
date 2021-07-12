package com.demo.learn.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.*;


/**
 * 自定义realm，需要继承AuthorizingRealm
 */
public class CustomRealm extends AuthorizingRealm {

    private PasswordService passwordService;

    Map<String, String> userMap = new HashMap<>(2);
    {
        userMap.put("jeet", "jeet-123");
        super.setName("customRealm");
    }

    /**
     * 用来做授权，checkRole，checkPermission 会用到
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取用户名
        String userName = (String)principals.getPrimaryPrincipal();
        // 获取用户角色权限数据
        Set<String> roles = getRolesByUserName(userName);
        Set<String> permissions = getPermissionsByUserName(userName);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    private Set<String> getRolesByUserName(String userName) {
        Set<String> rolesSet = new HashSet<>(2);
        rolesSet.add("admin");
        rolesSet.add("user");
        return rolesSet;
    }

    private Set<String> getPermissionsByUserName(String userName) {
        Set<String> permissionSet = new HashSet<>(2);
        permissionSet.add("user:add");
        permissionSet.add("user:del");
        return permissionSet;
    }

    /**
     * 用来做认证，login 会用到
     * @param token subject 传过来的验证信息
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 通过subject 获取用户名
        String userName = (String) token.getPrincipal();
        // 通过数据库查询
        Optional<String> passwordOptional = getPasswordByUserName(userName);
        if (passwordOptional.isPresent()) {
            String password = passwordOptional.get();
            // 查到用户密码，返回authenticationInfo对象
            return new SimpleAuthenticationInfo("jeet", password, ByteSource.Util.bytes("jeet"),"customRealm");
        }
        return null;
    }

    /**
     * 模拟从数据库查询
     * @param userName
     * @return
     */
    private Optional<String> getPasswordByUserName(String userName) {
        return Optional.ofNullable(userMap.get(userName));
    }
}
