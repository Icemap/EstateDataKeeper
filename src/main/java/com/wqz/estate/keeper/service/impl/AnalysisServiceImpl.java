package com.wqz.estate.keeper.service.impl;

import java.util.ArrayList;
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
import com.wqz.estate.keeper.utils.CalcUtils;
import com.wqz.estate.keeper.utils.CoodUtils;
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
		List<AddressBean> resultList = AMapPointsUtils.multiThreadLimitByBusTime(estimatesList, mins * 60, origin);
		return resultList;
	}

	@Override
	public List<AddressBean> getTransferLimitAddressList(Integer transferNum, PointBean origin)
	{
		List<AddressBean> estimatesList = getRectLimitAddressList(
				origin.lon - ParamUtils.EstimatesTransferLatLonDelta,
				origin.lon + ParamUtils.EstimatesTransferLatLonDelta, 
				origin.lat - ParamUtils.EstimatesTransferLatLonDelta, 
				origin.lat + ParamUtils.EstimatesTransferLatLonDelta);
		
		//AMap API精算
		List<AddressBean> resultList = AMapPointsUtils.multiThreadLimitByTransferNum(estimatesList, transferNum, origin);
		return resultList;
	}

	@Override
	public List<AddressBean> getTransferAndBusTimeLimitAddressList(Integer transferNum, Integer mins, PointBean origin)
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
		List<AddressBean> resultList = AMapPointsUtils.multiThreadLimitByBusTimeAndTransferNum
				(estimatesList, transferNum, mins * 60, origin);
		return resultList;
	}
	
	//length(m)
	@Override
	public List<AddressBean> getLengthLimitAddressList(Integer length, PointBean origin)
	{
		PointBean originMoc = CoodUtils.lonLatToMercator(origin.lon, origin.lat);
		PointBean right = CoodUtils.mercatorToLonLat(originMoc.lon + length, originMoc.lat);
		PointBean left = CoodUtils.mercatorToLonLat(originMoc.lon - length, originMoc.lat);
		PointBean bottom = CoodUtils.mercatorToLonLat(originMoc.lon, originMoc.lat - length);
		PointBean top = CoodUtils.mercatorToLonLat(originMoc.lon, originMoc.lat + length);
		
		//矩形粗算
		List<AddressBean> estimatesList = getRectLimitAddressList(
				left.lon, right.lon, bottom.lat, top.lat);
		
		//圆形精算
		List<AddressBean> resultList = new ArrayList<>();
		for(AddressBean bean : estimatesList)
		{
			if(length > CalcUtils.getLength(originMoc, bean))
				resultList.add(bean);
		}
		return resultList;
	}

	@Override
	public List<AddressBean> getDrawLimitAddressList(List<PointBean> pointList)
	{
		Double eLonMin = pointList.get(0).lon;
		Double eLonMax = pointList.get(0).lon;
		Double eLatMin = pointList.get(0).lat;
		Double eLatMax = pointList.get(0).lat;
		
		for(PointBean point : pointList)
		{
			if(eLonMin > point.lon) eLonMin = point.lon;
			if(eLonMax < point.lon) eLonMax = point.lon;
			if(eLatMin > point.lat) eLatMin = point.lat;
			if(eLatMax < point.lat) eLatMax = point.lat;
		}
		
		List<AddressBean> estimatesList = getRectLimitAddressList(eLonMin, eLonMax, eLatMin, eLatMax);
		
		return CalcUtils.limitByPolygonAddressList(pointList, estimatesList);
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
