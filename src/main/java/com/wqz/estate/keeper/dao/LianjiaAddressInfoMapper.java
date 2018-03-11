package com.wqz.estate.keeper.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wqz.estate.keeper.pojo.LianjiaAddressInfo;

public interface LianjiaAddressInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LianjiaAddressInfo record);

    int insertSelective(LianjiaAddressInfo record);

    LianjiaAddressInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LianjiaAddressInfo record);

    int updateByPrimaryKey(LianjiaAddressInfo record);
    
    Integer selectNumByName(String name);
    
    List<LianjiaAddressInfo> selectByRect(@Param("eLonMin")Double eLonMin, @Param("eLonMax")Double eLonMax,
    		@Param("eLatMin")Double eLatMin, @Param("eLatMax")Double eLatMax);
}