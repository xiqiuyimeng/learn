package com.demo.learn.model;

import java.util.Date;

import lombok.Data;

@Data
public class Good{

    /**
     * 订单id
     */
    private Integer id;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 库存量
     */
    private Integer store;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}