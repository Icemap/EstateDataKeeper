package com.wqz.estate.keeper.dao;

import com.wqz.estate.keeper.pojo.ProcessorStartInfo;

public interface ProcessorStartInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProcessorStartInfo record);

    int insertSelective(ProcessorStartInfo record);

    ProcessorStartInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProcessorStartInfo record);

    int updateByPrimaryKey(ProcessorStartInfo record);
}