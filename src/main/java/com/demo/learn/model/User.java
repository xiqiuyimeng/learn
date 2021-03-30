package com.demo.learn.model;

import lombok.Data;

@Data
public class User{

    private int userId;

    private String name;

    private Integer age;

    /**
     * 性别
     */
    private String sex;

}