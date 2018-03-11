package com.wqz.estate.keeper.service;

import java.util.List;

import com.wqz.estate.keeper.bean.AddressBean;
import com.wqz.estate.keeper.bean.PointBean;

public interface AnalysisService
{
	List<AddressBean> getBusTimeLimitAddressList(Integer mins, PointBean origin);
	List<AddressBean> getTransferLimitAddressList(Integer transferNum, PointBean origin);
	List<AddressBean> getLengthLimitAddressList(Integer length, PointBean origin);
	
	List<AddressBean> getDrawLimitAddressList(List<PointBean> pointList);
	List<AddressBean> getRectLimitAddressList(Double eLonMin, Double eLonMax,
    		Double eLatMin, Double eLatMax);
}
