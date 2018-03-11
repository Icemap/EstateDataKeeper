package com.wqz.estate.keeper.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wqz.estate.keeper.bean.AddressBean;
import com.wqz.estate.keeper.bean.PointBean;
import com.wqz.estate.keeper.dao.AnjukeAddressInfoMapper;
import com.wqz.estate.keeper.dao.LianjiaAddressInfoMapper;
import com.wqz.estate.keeper.pojo.AnjukeAddressInfo;
import com.wqz.estate.keeper.pojo.LianjiaAddressInfo;
import com.wqz.estate.keeper.service.AnalysisService;
import com.wqz.estate.keeper.utils.AMapPointsUtils;
import com.wqz.estate.keeper.utils.ParamUtils;

@Service
@Transactional
public class AnalysisServiceImpl implements AnalysisService
{
	@Autowired
	AnjukeAddressInfoMapper anjukeAddressInfoMapper;
	@Autowired
	LianjiaAddressInfoMapper lianjiaAddressInfoMapper;
	
	@Override
	public List<AddressBean> getBusTimeLimitAddressList(Integer mins, PointBean origin)
	{
		//估算
		Double _eLength = ParamUtils.EstimatesBusSpeed * (mins / 60.0) * ParamUtils.EstimatesCoefficient;
		Double _eLon = _eLength / ParamUtils.EstimatesLon;
		Double _eLat = _eLength / ParamUtils.EstimatesLat;
		
		Double eLonMin = origin.lon - _eLon;
		Double eLonMax = origin.lon + _eLon;
		Double eLatMin = origin.lat - _eLat;
		Double eLatMax = origin.lat + _eLat;
		
		List<AddressBean> estimatesList = getRectLimitAddressList(eLonMin, eLonMax, eLatMin, eLatMax);
		
		//高德API精算
		for (Iterator<AddressBean> iter = estimatesList.iterator(); iter.hasNext();) 
		{
			AddressBean address = iter.next();
			if(mins * 60 < AMapPointsUtils.getBusTime(
					origin, new PointBean(address.getLon(), address.getLat())))
				iter.remove();
		}
		return estimatesList;
	}

	@Override
	public List<AddressBean> getTransferLimitAddressList(Integer transferNum, PointBean origin)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AddressBean> getLengthLimitAddressList(Integer length, PointBean origin)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AddressBean> getDrawLimitAddressList(List<PointBean> pointList)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AddressBean> getRectLimitAddressList(Double eLonMin, Double eLonMax, Double eLatMin, Double eLatMax)
	{
		List<AnjukeAddressInfo> anjukeAddressList = 
				anjukeAddressInfoMapper.selectByRect(eLonMin, eLonMax, eLatMin, eLatMax);
		List<LianjiaAddressInfo> lianjiaAddressList = 
				lianjiaAddressInfoMapper.selectByRect(eLonMin, eLonMax, eLatMin, eLatMax);
		
		List<AddressBean> result = new ArrayList<>();
		for(AnjukeAddressInfo item : anjukeAddressList)
			result.add(new AddressBean(item));
		for(LianjiaAddressInfo item : lianjiaAddressList)
			result.add(new AddressBean(item));
		
		return result;
	}
}
