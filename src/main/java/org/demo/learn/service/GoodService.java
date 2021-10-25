package org.demo.learn.service;

import org.demo.learn.model.Good;

import java.util.List;


public interface GoodService {

    Good getGood(Integer id);

    void addGood(Good record);

    void deleteGood(Integer id);

    void editGood(Good record);

    List<Good> getGoodList();

    boolean buy(String goodName);

    void resetStore(Integer store);

    String getGoodByName(String goodName);
}