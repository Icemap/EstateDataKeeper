package com.wqz.estate.keeper.utils;

public class StringUtils
{
	public static String getNumWithPoint(String srcStr)
	{
		return srcStr.replaceAll("[^\\d+\\.?\\d*]","");
	}
	
	public static Boolean isNullOrEmpty(String s)
	{
		return (s == null || s.isEmpty());
	}
}
