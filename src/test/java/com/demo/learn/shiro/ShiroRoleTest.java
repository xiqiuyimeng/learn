package com.demo.learn.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * shiro 账户角色认证测试
 */
public class ShiroRoleTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser() {
        // 添加用户的时候，同时添加角色
        simpleAccountRealm.addAccount("jeet", "jeet-123", "admin", "user");
    }

    @Test
    public void testAuth() {
        // 创建security manager，设置realm
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        // 设置security manager
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        // 获取subject
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("jeet", "jeet-123");
        // subject 提交请求给 security manager
        subject.login(token);

        System.out.println(subject.isAuthenticated());

        // 校验角色
        subject.checkRoles("admin");
    }
}
