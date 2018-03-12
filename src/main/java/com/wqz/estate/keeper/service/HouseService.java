package com.wqz.estate.keeper.service;

import java.util.List;

import com.wqz.estate.keeper.pojo.AnjukeHouseData;
import com.wqz.estate.keeper.pojo.LianjiaHouseData;

public interface HouseService
{
	List<AnjukeHouseData> getAnJuKeHouseList(String address);
	List<LianjiaHouseData> getLianJiaHouseList(String address);
}
