package com.wqz.estate.keeper.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
		Integer resultTime = -1;
		
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
			return resultTime;
		
		for(JsonElement item : transitsArray)
		{
			String sTime = item.getAsJsonObject().get("duration").getAsString();
			if(resultTime > Integer.parseInt(sTime))
				resultTime = Integer.parseInt(sTime);
		}
		
		return resultTime;
	}
}
