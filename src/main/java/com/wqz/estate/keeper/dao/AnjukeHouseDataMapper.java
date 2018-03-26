package com.wqz.estate.keeper.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
    
    List<AnjukeHouseData> selectByParam(
    		@Param("addressList")List<String> addressList,
    		@Param("price")Double price, 
    		@Param("unitPrice")Double unitPrice,
    		@Param("roomNum")String roomNum, 
    		@Param("buildTime")Integer buildTime);
    
    List<String> selectAllContent();
    
    int deleteByContentUrls(@Param("contentUrls")List<String> contentUrls);
}