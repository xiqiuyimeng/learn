package org.demo.learn.util;

import org.demo.learn.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;

/**
 * 使用 jdbc template 连接数据库，适用于单独调试，main方法调试
 * @author luwt-a
 * @date 2023/4/20
 */
public class JdbcTemplateUtil {

    private static final JdbcTemplate JDBC_TEMPLATE;

    static {
        // mysql驱动
        String driver = "com.mysql.jdbc.Driver";
        // 连接地址
        String url = "jdbc:mysql://server:3306/test";
        // 用户
        String user = "root";
        // 密码
        String password = "yyy-admin";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        JDBC_TEMPLATE = new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) {
        // 访问数据库，例如查询
        String sql = "select * from user";
        // 使用 BeanPropertyRowMapper 完成orm映射
        List<User> users = JDBC_TEMPLATE.query(sql, new BeanPropertyRowMapper<>(User.class));
        System.out.println(users);
    }

}
