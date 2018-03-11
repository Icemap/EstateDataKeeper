package com.wqz.estate.keeper.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wqz.estate.keeper.pojo.AnjukeAddressInfo;

public interface AnjukeAddressInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AnjukeAddressInfo record);

    int insertSelective(AnjukeAddressInfo record);

    AnjukeAddressInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnjukeAddressInfo record);

    int updateByPrimaryKey(AnjukeAddressInfo record);

    Integer selectNumByName(String name);
    
    List<AnjukeAddressInfo> selectByRect(@Param("eLonMin")Double eLonMin, @Param("eLonMax")Double eLonMax,
    		@Param("eLatMin")Double eLatMin, @Param("eLatMax")Double eLatMax);
}