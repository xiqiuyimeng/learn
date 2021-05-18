package com.demo.learn.dao;

import com.demo.learn.model.Good;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GoodMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Good good);

    int insertSelective(Good good);

    Good selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Good good);

    int updateByPrimaryKey(Good good);

    List<Good> getGoodList();

    Good getGoodStore(@Param("goodName")String goodName);

    void resetStore(@Param("store") Integer store);
}