package com.wqz.estate.keeper.utils;

public class ParamUtils
{
	public static final String cityName = "广州";
	public static final String cityPinYin = "guangzhou";
	public static final String cityPinYinSimple = "gz";

	//估算车速
	public static final Double EstimatesCarSpeed = 60D;
	//估算公交车速
	public static final Double EstimatesBusSpeed = 40D;
	//将估算裁切该倍数车行距离范围的小区
	public static final Double EstimatesCoefficient = 1.1D;
	//估算换乘允许范围
	public static final Double EstimatesTransferLatLonDelta = 0.2D;
	//经度估算：110km/经度
	public static final Double EstimatesLon = 110D;
	//纬度估算：110km/纬度
	public static final Double EstimatesLat = 110D;
}
