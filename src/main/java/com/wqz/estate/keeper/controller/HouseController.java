package com.wqz.estate.keeper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wqz.estate.keeper.pojo.AnjukeHouseData;
import com.wqz.estate.keeper.pojo.LianjiaHouseData;
import com.wqz.estate.keeper.service.impl.HouseServiceImpl;

@Controller
@RequestMapping("/house")
public class HouseController
{
	@Autowired
	HouseServiceImpl houseServiceImpl;
	
	@ResponseBody
	@RequestMapping("/anjuke")
	public List<AnjukeHouseData> getAnJuKeHouseList(String address)
	{
		return houseServiceImpl.getAnJuKeHouseList(address);
	}
	
	@ResponseBody
	@RequestMapping("/lianjia")
	public List<LianjiaHouseData> getLianJiaHouseList(String address)
	{
		return houseServiceImpl.getLianJiaHouseList(address);
	}
}
