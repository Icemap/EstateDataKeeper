package com.wqz.estate.keeper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.wqz.estate.keeper.bean.AddressNameRequestBean;
import com.wqz.estate.keeper.bean.HouseResponseBean;
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
	
	@ResponseBody
	@RequestMapping("/getData/byParam")
	public HouseResponseBean getDataByParam(
			@RequestParam(value="jsonAddressList", required = true)String jsonAddressList, 
			@RequestParam(value="price", required = false)Double price,
			@RequestParam(value="unitPrice", required = false)Double unitPrice,
			@RequestParam(value="roomNum", required = false)String roomNum,
			@RequestParam(value="buildTime", required = false)Integer buildTime)
	{
		AddressNameRequestBean addressNameRequestBean
			= new Gson().fromJson(jsonAddressList, AddressNameRequestBean.class);
		
		return houseServiceImpl.getDataByParam(
				addressNameRequestBean, price, unitPrice,
				roomNum, buildTime);
	}
}
