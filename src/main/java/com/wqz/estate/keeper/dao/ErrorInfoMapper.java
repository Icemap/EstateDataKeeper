package com.wqz.estate.keeper.dao;

import com.wqz.estate.keeper.pojo.ErrorInfo;

public interface ErrorInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ErrorInfo record);

    int insertSelective(ErrorInfo record);

    ErrorInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErrorInfo record);

    int updateByPrimaryKey(ErrorInfo record);
}