package com.wqz.estate.keeper.dao;

import java.util.List;

import com.wqz.estate.keeper.pojo.AnjukeHouseData;

public interface AnjukeHouseDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AnjukeHouseData record);

    int insertSelective(AnjukeHouseData record);

    AnjukeHouseData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnjukeHouseData record);

    int updateByPrimaryKey(AnjukeHouseData record);

    Integer selectNumByContentUrl(String contentUrl);
    
    List<AnjukeHouseData> selectByAddress(String address);
}