package com.wqz.estate.keeper.bean;

import java.util.List;

import com.wqz.estate.keeper.pojo.AnjukeHouseData;
import com.wqz.estate.keeper.pojo.LianjiaHouseData;

public class HouseResponseBean
{
	private List<LianjiaHouseData> lianjiaHouseDataList;
	private List<AnjukeHouseData> anjukeHouseDataList;
	public List<LianjiaHouseData> getLianjiaHouseDataList()
	{
		return lianjiaHouseDataList;
	}
	public void setLianjiaHouseDataList(List<LianjiaHouseData> lianjiaHouseDataList)
	{
		this.lianjiaHouseDataList = lianjiaHouseDataList;
	}
	public List<AnjukeHouseData> getAnjukeHouseDataList()
	{
		return anjukeHouseDataList;
	}
	public void setAnjukeHouseDataList(List<AnjukeHouseData> anjukeHouseDataList)
	{
		this.anjukeHouseDataList = anjukeHouseDataList;
	}
}
