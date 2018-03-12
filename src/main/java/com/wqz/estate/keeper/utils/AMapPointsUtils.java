package com.wqz.estate.keeper.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wqz.estate.keeper.bean.AddressBean;
import com.wqz.estate.keeper.bean.PointBean;
import com.wqz.estate.keeper.pojo.AnjukeAddressInfo;
import com.wqz.estate.keeper.pojo.LianjiaAddressInfo;

public class AMapPointsUtils
{
	public static String key = "d8619be6ae77395055841ae5daa0e467";
	
	public static LianjiaAddressInfo getLocation(LianjiaAddressInfo lianjiaAddressInfo)
	{
		Map<String, String> params = new HashMap<>();
		params.put("key", key);
		params.put("address", lianjiaAddressInfo.getName());
		params.put("city", ParamUtils.cityName);
		
		String urlGet = HttpUtils.URLGet("http://restapi.amap.com/v3/geocode/geo",
				params, HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
		JsonParser jsonParser = new JsonParser();
		JsonElement geoCodeElement = jsonParser.parse(urlGet);
		JsonArray geocodesArray = geoCodeElement.getAsJsonObject().getAsJsonArray("geocodes");
		if(geocodesArray.size() == 0)
			return lianjiaAddressInfo;
		
		JsonObject geocodes = geocodesArray.get(0).getAsJsonObject();
		String formatted_address = geocodes.get("formatted_address").getAsString();
		String sLocation = geocodes.get("location").getAsString();
		String[] sLocationArray = sLocation.split(",");

		Double lon = Double.parseDouble(sLocationArray[0]);
		Double lat = Double.parseDouble(sLocationArray[1]);
		
		lianjiaAddressInfo.setLon(lon);
		lianjiaAddressInfo.setLat(lat);
		lianjiaAddressInfo.setAddress(formatted_address);
		return lianjiaAddressInfo;
	}
	
	public static AnjukeAddressInfo getLocation(AnjukeAddressInfo anjukeAddressInfo)
	{
		Map<String, String> params = new HashMap<>();
		params.put("key", key);
		params.put("address", anjukeAddressInfo.getName());
		params.put("city", ParamUtils.cityName);
		
		String urlGet = HttpUtils.URLGet("http://restapi.amap.com/v3/geocode/geo",
				params, HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
		JsonParser jsonParser = new JsonParser();
		JsonElement geoCodeElement = jsonParser.parse(urlGet);
		JsonArray geocodesArray = geoCodeElement.getAsJsonObject().getAsJsonArray("geocodes");
		if(geocodesArray.size() == 0)
			return anjukeAddressInfo;
		
		JsonObject geocodes = geocodesArray.get(0).getAsJsonObject();
		String formatted_address = geocodes.get("formatted_address").getAsString();
		String sLocation = geocodes.get("location").getAsString();
		String[] sLocationArray = sLocation.split(",");

		Double lon = Double.parseDouble(sLocationArray[0]);
		Double lat = Double.parseDouble(sLocationArray[1]);
		
		anjukeAddressInfo.setLon(lon);
		anjukeAddressInfo.setLat(lat);
		anjukeAddressInfo.setAddress(formatted_address);
		return anjukeAddressInfo;
	}
	
	public static Integer getBusTime(PointBean origin, PointBean dest)
	{
		Integer resultTime = 10000000;
		
		Map<String, String> params = new HashMap<>();
		params.put("key", key);
		params.put("origin", origin.lon + "," + origin.lat);
		params.put("destination", dest.lon + "," + dest.lat);
		params.put("strategy", "0");
		params.put("city", ParamUtils.cityName);
		
		String urlGet = HttpUtils.URLGet("http://restapi.amap.com/v3/direction/transit/integrated",
				params, HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
		JsonParser jsonParser = new JsonParser();
		JsonElement geoCodeElement = jsonParser.parse(urlGet);
		JsonObject routeObj = geoCodeElement.getAsJsonObject().get("route").getAsJsonObject();
		JsonArray transitsArray = routeObj.getAsJsonObject().getAsJsonArray("transits");
		if(transitsArray.size() == 0)
			return -1;
		
		for(JsonElement item : transitsArray)
		{
			String sTime = item.getAsJsonObject().get("duration").getAsString();
			if(resultTime > Integer.parseInt(sTime))
				resultTime = Integer.parseInt(sTime);
		}
		
		return resultTime;
	}
	
	public static Integer getTransferNum(PointBean origin, PointBean dest)
	{
		Integer transferNum = 10000000;
		
		Map<String, String> params = new HashMap<>();
		params.put("key", key);
		params.put("origin", origin.lon + "," + origin.lat);
		params.put("destination", dest.lon + "," + dest.lat);
		params.put("strategy", "2");
		params.put("city", ParamUtils.cityName);
		
		String urlGet = HttpUtils.URLGet("http://restapi.amap.com/v3/direction/transit/integrated",
				params, HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
		JsonParser jsonParser = new JsonParser();
		JsonElement geoCodeElement = jsonParser.parse(urlGet);
		JsonObject routeObj = geoCodeElement.getAsJsonObject().get("route").getAsJsonObject();
		JsonArray transitsArray = routeObj.getAsJsonObject().getAsJsonArray("transits");
		if(transitsArray.size() == 0)
			return -1;
		
		for(JsonElement item : transitsArray)
		{
			JsonArray segments = item.getAsJsonObject().getAsJsonArray("segments");
			Integer thisTransferNum = 0;
			for(JsonElement segItem : segments)
			{
				thisTransferNum += segItem.getAsJsonObject()
						.get("bus").getAsJsonObject().getAsJsonArray("buslines").size();
				
			}
			if(transferNum > thisTransferNum)
				transferNum = thisTransferNum;
		}
		
		return transferNum;
	}
	
	/**
	 * 双限
	 * @param origin
	 * @param dest
	 * @param limitBusTime
	 * @param limitTransferNum
	 * @return true分析结果，false剔除结果
	 */
	public static Boolean limitByBusTimeAndTransferNum(PointBean origin, PointBean dest,
			Integer limitBusTime, Integer limitTransferNum)
	{
		Integer transferNum = 10000000;
		Integer busTime = 10000000;
		
		Map<String, String> params = new HashMap<>();
		params.put("key", key);
		params.put("origin", origin.lon + "," + origin.lat);
		params.put("destination", dest.lon + "," + dest.lat);
		params.put("city", ParamUtils.cityName);
		
		String urlGet = HttpUtils.URLGet("http://restapi.amap.com/v3/direction/transit/integrated",
				params, HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
		JsonParser jsonParser = new JsonParser();
		JsonElement geoCodeElement = jsonParser.parse(urlGet);
		JsonObject routeObj = geoCodeElement.getAsJsonObject().get("route").getAsJsonObject();
		JsonArray transitsArray = routeObj.getAsJsonObject().getAsJsonArray("transits");
		if(transitsArray.size() == 0)
			return false;

		//得到TransferNum
		for(JsonElement item : transitsArray)
		{
			JsonArray segments = item.getAsJsonObject().getAsJsonArray("segments");
			Integer thisTransferNum = 0;
			for(JsonElement segItem : segments)
			{
				thisTransferNum += segItem.getAsJsonObject()
						.get("bus").getAsJsonObject().getAsJsonArray("buslines").size();
				
			}
			if(transferNum > thisTransferNum)
				transferNum = thisTransferNum;
		}
		
		//得到公交时间
		for(JsonElement item : transitsArray)
		{
			String sTime = item.getAsJsonObject().get("duration").getAsString();
			if(busTime > Integer.parseInt(sTime))
				busTime = Integer.parseInt(sTime);
		}
		
		if(busTime <= limitBusTime && transferNum <= limitTransferNum)
			return true;
		else
			return false;
	}
	
	
	//--------------Multi API--------------
	public static List<AddressBean> multiThreadLimitByTransferNum(List<AddressBean> pointList,
			Integer transferNum, PointBean origin)
	{
		List<AddressBean> resultList = new ArrayList<>();
		
		// 使用线程安全的Vector
		Vector<Thread> threads = new Vector<Thread>();
		for (int i = 0; i < pointList.size(); i++)
		{
			final int index = i;
			Thread iThread = new Thread(new Runnable()
			{
				public void run()
				{
					if(getTransferNum(origin, new PointBean(
							pointList.get(index).getLon(), 
							pointList.get(index).getLat())) <= transferNum)
							resultList.add(pointList.get(index));
				}
			});
			threads.add(iThread);
			iThread.start();
		}

		for (Thread iThread : threads)
		{
			try
			{
				// 等待所有线程执行完毕
				iThread.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		return resultList;
	}
	
	public static List<AddressBean> multiThreadLimitByBusTime(List<AddressBean> pointList,
			Integer seconds, PointBean origin)
	{
		List<AddressBean> resultList = new ArrayList<>();
		
		// 使用线程安全的Vector
		Vector<Thread> threads = new Vector<Thread>();
		for (int i = 0; i < pointList.size(); i++)
		{
			final int index = i;
			Thread iThread = new Thread(new Runnable()
			{
				public void run()
				{
					if(getBusTime(origin, new PointBean(
							pointList.get(index).getLon(), 
							pointList.get(index).getLat())) < seconds)
							resultList.add(pointList.get(index));
				}
			});
			threads.add(iThread);
			iThread.start();
		}

		for (Thread iThread : threads)
		{
			try
			{
				// 等待所有线程执行完毕
				iThread.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		return resultList;
	}

	public static List<AddressBean> multiThreadLimitByBusTimeAndTransferNum(
			List<AddressBean> pointList, Integer transferNum, 
			Integer seconds, PointBean origin)
	{
		List<AddressBean> resultList = new ArrayList<>();
		
		// 使用线程安全的Vector
		Vector<Thread> threads = new Vector<Thread>();
		for (int i = 0; i < pointList.size(); i++)
		{
			final int index = i;
			Thread iThread = new Thread(new Runnable()
			{
				public void run()
				{
					if(limitByBusTimeAndTransferNum(origin, 
							new PointBean(pointList.get(index).getLon(),
								pointList.get(index).getLat()), seconds, transferNum))
						resultList.add(pointList.get(index));
				}
			});
			threads.add(iThread);
			iThread.start();
		}

		for (Thread iThread : threads)
		{
			try
			{
				// 等待所有线程执行完毕
				iThread.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		return resultList;
	}
}
