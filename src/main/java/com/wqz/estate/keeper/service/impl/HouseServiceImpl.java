package com.wqz.estate.keeper.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wqz.estate.keeper.dao.AnjukeHouseDataMapper;
import com.wqz.estate.keeper.dao.LianjiaHouseDataMapper;
import com.wqz.estate.keeper.pojo.AnjukeHouseData;
import com.wqz.estate.keeper.pojo.LianjiaHouseData;
import com.wqz.estate.keeper.service.HouseService;

@Service
@Transactional
public class HouseServiceImpl implements HouseService
{
	@Autowired
	AnjukeHouseDataMapper anjukeHouseDataMapper;
	@Autowired
	LianjiaHouseDataMapper lianjiaHouseDataMapper;
	
	@Override
	public List<AnjukeHouseData> getAnJuKeHouseList(String address)
	{
		return anjukeHouseDataMapper.selectByAddress(address);
	}

	@Override
	public List<LianjiaHouseData> getLianJiaHouseList(String address)
	{
		return lianjiaHouseDataMapper.selectByAddress(address);
	}
}
