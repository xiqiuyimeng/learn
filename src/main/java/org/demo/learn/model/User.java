package org.demo.learn.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User{

    private int userId;

    private String name;

    private Integer age;

    /**
     * 性别
     */
    private String sex;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime contractBeginDate;

}