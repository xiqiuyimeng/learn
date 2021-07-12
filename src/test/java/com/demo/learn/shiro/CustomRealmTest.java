package com.demo.learn.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * 自定义realm 认证测试
 */
public class CustomRealmTest {

    @Test
    public void testAuth() {
        CustomRealm customRealm = new CustomRealm();

        DefaultSecurityManager manager = new DefaultSecurityManager();
        manager.setRealm(customRealm);

        SecurityUtils.setSecurityManager(manager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("jeet", "jeet-123");

        subject.login(token);
        System.out.println(subject.isAuthenticated());

        subject.checkRoles("admin");
        subject.checkPermission("user:del");
    }
}
