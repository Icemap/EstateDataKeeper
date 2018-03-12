package com.wqz.estate.keeper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wqz.estate.keeper.bean.AddressBean;
import com.wqz.estate.keeper.bean.PointBean;
import com.wqz.estate.keeper.service.impl.AnalysisServiceImpl;

@Controller
@RequestMapping("/analysis")
public class AddressAnalysisController
{
	@Autowired
	AnalysisServiceImpl analysisServiceImpl;
	
	@ResponseBody
	@RequestMapping("/limit/busTime")
	public List<AddressBean> getBusTimeLimitAddressList(Integer mins,
			Double originLon, Double originLat)
	{
		return analysisServiceImpl.getBusTimeLimitAddressList(
				mins, new PointBean(originLon, originLat));
	}
	
	@ResponseBody
	@RequestMapping("/limit/transfer")
	public List<AddressBean> getTransferLimitAddressList(
			Integer transferNum, Double originLon, Double originLat)
	{
		return analysisServiceImpl.getTransferLimitAddressList(
				transferNum, new PointBean(originLon, originLat));
	}

	@ResponseBody
	@RequestMapping("/limit/transferAndBusTime")
	public List<AddressBean> getTransferAndBusTimeLimitAddressList(
			Integer transferNum, Integer mins, Double originLon, Double originLat)
	{
		return analysisServiceImpl.getTransferAndBusTimeLimitAddressList(
				transferNum, mins, new PointBean(originLon, originLat));
	}

	@ResponseBody
	@RequestMapping("/limit/length")
	public List<AddressBean> getLengthLimitAddressList(Integer length,
			Double originLon, Double originLat)
	{
		return analysisServiceImpl.getLengthLimitAddressList(
				length, new PointBean(originLon, originLat));
	}

	@ResponseBody
	@RequestMapping("/limit/polygon")
	public List<AddressBean> getDrawLimitAddressList(List<PointBean> pointList)
	{
		return analysisServiceImpl.getDrawLimitAddressList(pointList);
	}
	
	@ResponseBody
	@RequestMapping("/rect")
	public List<AddressBean> getRectLimitAddressList(Double eLonMin,
			Double eLonMax, Double eLatMin, Double eLatMax)
	{
		return analysisServiceImpl.getRectLimitAddressList(
				eLonMin, eLonMax, eLatMin, eLatMax);
	}
}
