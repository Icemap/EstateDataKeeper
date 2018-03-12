package com.wqz.estate.keeper.utils;

import java.util.ArrayList;
import java.util.List;

import com.wqz.estate.keeper.bean.AddressBean;
import com.wqz.estate.keeper.bean.PointBean;

public class CalcUtils
{
	public static Double getLength(PointBean origin, AddressBean addressDest)
	{
		PointBean dest = CoodUtils.lonLatToMercator(
				addressDest.getLon(), addressDest.getLat());
		return Math.sqrt(Math.pow(dest.lon - origin.lon, 2)
				+ Math.pow(dest.lat - origin.lat, 2));
	}
	
	public static Boolean isInPolygon(List<PointBean> polygon, AddressBean addressDest)
	{
		//求address向x轴正方向引出的射线与polygon的交点个数
		Integer crossPointNum = 0;
		for(int i = 0;i < polygon.size() - 1;i++)
		{
			//查看线段两点y值与减去address的y值是否异号
			Double _y1 = polygon.get(i).lat - addressDest.getLat();
			Double _y2 = polygon.get(i + 1).lat - addressDest.getLat();
			Double sign = _y1 * _y2;
			
			//同号,无交点
			if(sign >= 0) continue;
			
			//计算交点x值
			Double _x1Rate = Math.abs((addressDest.getLon() - polygon.get(i).lon)
					/ polygon.get(i + 1).lon - polygon.get(i).lon);
			Double _x2Rate = Math.abs((addressDest.getLon() - polygon.get(i + 1).lon)
					/ polygon.get(i + 1).lon - polygon.get(i).lon);
			Double x = polygon.get(i).lon * _x1Rate + polygon.get(i + 1).lon * _x2Rate;
			
			if(x > addressDest.getLon())
				crossPointNum ++;
		}
		
		//若为单数，点在面内；否则为面外
		if(crossPointNum / 2 == 0)
			return false;
		else
			return true;
					
	}
	
	public static List<AddressBean> limitByPolygonAddressList(List<PointBean> polygon,
			List<AddressBean> estimatesList)
	{
		List<AddressBean> result = new ArrayList<>();
		for(AddressBean bean : estimatesList)
		{
			if(isInPolygon(polygon, bean))
				result.add(bean);
		}
		
		return result;
	}
}
