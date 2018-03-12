package com.wqz.estate.keeper.dao;

import java.util.List;

import com.wqz.estate.keeper.pojo.LianjiaHouseData;

public interface LianjiaHouseDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LianjiaHouseData record);

    int insertSelective(LianjiaHouseData record);

    LianjiaHouseData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LianjiaHouseData record);

    int updateByPrimaryKey(LianjiaHouseData record);
    
    Integer selectNumByContentUrl(String contentUrl);

    List<LianjiaHouseData> selectByAddress(String address);
}