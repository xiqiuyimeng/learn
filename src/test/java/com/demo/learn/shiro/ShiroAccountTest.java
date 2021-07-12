package com.demo.learn.shiro;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * shiro 账户认证测试
 */
public class ShiroAccountTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("jeet", "jeet-123");
    }

    @Test
    public void testAuth() {
        // 首先构建security manager，提供安全服务，所以需要先创建
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        // 设置realm
        securityManager.setRealm(simpleAccountRealm);

        // 获取向Security Manager提交请求的subject，而主体subject可以通过shiro提供的一个工具类SecurityUtils来获取
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();

        // 主体Subject提交请求给Security Manager -->  subject.login(token);
        UsernamePasswordToken token = new UsernamePasswordToken("jeet", "jeet-123");
        subject.login(token);

        // shiro提供了一个检查主体subject是否认证的方法isAuthenticated(),此方法的返回结果是一个boolean值
        System.out.println(subject.isAuthenticated());

        subject.logout();
        System.out.println(subject.isAuthenticated());
    }

}
