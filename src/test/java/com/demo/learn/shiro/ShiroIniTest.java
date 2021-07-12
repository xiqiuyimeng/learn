package com.demo.learn.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * shiro 读取ini配置文件认证测试
 */
public class ShiroIniTest {

    @Test
    public void testAuth() {
        IniRealm iniRealm = new IniRealm("classpath:shiro-user.ini");

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("jeet2", "jeet-123");
        subject.login(token);
        // role 和 permission 都是定义的字符串标志，可以任意定义，只要在校验的时候使用相同的就可以校验通过
        subject.checkRoles("admin");
        subject.checkPermission("user:update");
    }
}
