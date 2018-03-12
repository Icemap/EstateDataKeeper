package com.wqz.estate.keeper.utils;

import com.wqz.estate.keeper.bean.PointBean;

public class CoodUtils
{
	public static double a = 6378245.0D;
    public static double ee = 0.006693421622965943D;
    
    public static PointBean mercatorToLonLat(double lon, double lat)
    {
        double toX = lon / 20037508.34D * 180.0D;
        double toY = lat / 20037508.34D * 180.0D;
        toY = 57.295779513082323D * (2.0D * Math.atan(Math
                .exp(toY * 3.141592653589793D / 180.0D)) - 1.570796326794897D);
        return new PointBean(toX, toY);
    }

    public static PointBean lonLatToMercator(double lon, double lat)
    {
        double toX = lon * 20037508.34D / 180.0D;
        double toY = Math.log(Math
                .tan((90.0D + lat) * 3.141592653589793D / 360.0D)) / 0.0174532925199433D;
        toY = toY * 20037508.34D / 180.0D;

        return new PointBean(toX, toY);
    }

    public static PointBean lonLatToGoogleMercator(double lon, double lat)
    {
        double toX = lon * 20037508.34D / 180.0D + 20037508.34D;
        double toY = Math.log(Math
                .tan((90.0D + lat) * 3.141592653589793D / 360.0D)) / 0.0174532925199433D;
        toY = toY * 20037508.34D / 180.0D;
        toY = 20037508.34D - toY;
        return new PointBean(toX, toY);
    }
    
    public static PointBean mercatorToGcj(double lon, double lat)
    {
    	PointBean latLngInfo = mercatorToLonLat(lon, lat);
        return gps84_To_Gcj02(latLngInfo.lon, latLngInfo.lat);
    }

    public static PointBean gcjToMercator(double lon, double lat)
    {
    	PointBean latLngInfo = gcj_To_Gps84(lon, lat);
        return lonLatToMercator(latLngInfo.lon, latLngInfo.lat);
    }
    
    public static PointBean gps84_To_Gcj02(double lon, double lat)
    {
        if (outOfChina(lon, lat))
        {
            return new PointBean(lon, lat);
        }
        double dLat = transformLat(lon - 105.0D, lat - 35.0D);
        double dLon = transformLon(lon - 105.0D, lat - 35.0D);
        double radLat = lat / 180.0D * 3.141592653589793D;
        double magic = Math.sin(radLat);
        magic = 1.0D - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = dLat * 180.0D
                / (a * (1.0D - ee) / (magic * sqrtMagic) * 3.141592653589793D);
        dLon = dLon * 180.0D
                / (a / sqrtMagic * Math.cos(radLat) * 3.141592653589793D);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new PointBean(mgLon, mgLat);
    }

    public static PointBean gcj_To_Gps84(double lon, double lat)
    {
    	PointBean gps = transform(lon, lat);
        double lontitude = lon * 2.0D - gps.lon;
        double latitude = lat * 2.0D - gps.lat;
        return new PointBean(lontitude, latitude);
    }
    
    public static PointBean transform(double lon, double lat)
    {
    	if (outOfChina(lon, lat))
        {
            return new PointBean(lon, lat);
        }
        double dLat = transformLat(lon - 105.0D, lat - 35.0D);
        double dLon = transformLon(lon - 105.0D, lat - 35.0D);
        double radLat = lat / 180.0D * 3.141592653589793D;
        double magic = Math.sin(radLat);
        magic = 1.0D - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = dLat * 180.0D
                / (a * (1.0D - ee) / (magic * sqrtMagic) * 3.141592653589793D);
        dLon = dLon * 180.0D
                / (a / sqrtMagic * Math.cos(radLat) * 3.141592653589793D);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new PointBean(mgLon, mgLat);
    }
    
    public static boolean outOfChina(double lon, double lat)
    {
        if ((lon < 72.004000000000005D) || (lon > 137.8347D))
            return true;
        if ((lat < 0.8293D) || (lat > 55.827100000000002D))
            return true;
        return false;
    }
    
    public static double transformLat(double x, double y)
    {
        double ret = -100.0D + 2.0D * x + 3.0D * y + 0.2D * y * y + 0.1D * x
                * y + 0.2D * Math.sqrt(Math.abs(x));

        ret += (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math
                .sin(2.0D * x * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(y * 3.141592653589793D) + 40.0D * Math
                .sin(y / 3.0D * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (160.0D * Math.sin(y / 12.0D * 3.141592653589793D) + 320.0D * Math
                .sin(y * 3.141592653589793D / 30.0D)) * 2.0D / 3.0D;
        return ret;
    }

    public static double transformLon(double x, double y)
    {
        double ret = 300.0D + x + 2.0D * y + 0.1D * x * x + 0.1D * x * y + 0.1D
                * Math.sqrt(Math.abs(x));

        ret += (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math
                .sin(2.0D * x * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(x * 3.141592653589793D) + 40.0D * Math
                .sin(x / 3.0D * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (150.0D * Math.sin(x / 12.0D * 3.141592653589793D) + 300.0D * Math
                .sin(x / 30.0D * 3.141592653589793D)) * 2.0D / 3.0D;

        return ret;
    }
}
