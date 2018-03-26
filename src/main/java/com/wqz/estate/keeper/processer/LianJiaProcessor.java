package com.wqz.estate.keeper.processer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wqz.estate.keeper.bean.LianJiaItemBean;
import com.wqz.estate.keeper.dao.ErrorInfoMapper;
import com.wqz.estate.keeper.dao.LianjiaAddressInfoMapper;
import com.wqz.estate.keeper.dao.LianjiaHouseDataMapper;
import com.wqz.estate.keeper.dao.ProcessorStartInfoMapper;
import com.wqz.estate.keeper.pojo.ErrorInfo;
import com.wqz.estate.keeper.pojo.LianjiaAddressInfo;
import com.wqz.estate.keeper.pojo.LianjiaHouseData;
import com.wqz.estate.keeper.pojo.ProcessorStartInfo;
import com.wqz.estate.keeper.utils.AMapPointsUtils;
import com.wqz.estate.keeper.utils.ByteBooleanUtils;
import com.wqz.estate.keeper.utils.ParamUtils;
import com.wqz.estate.keeper.utils.StringUtils;

@Service
@Transactional
public class LianJiaProcessor
{
	public final static String processorName = "LianJiaProcessor";
	public final static Long DELAY_TIME = 1L* 60 * 1000;
	public final static Long INTERVAL_TIME = 2L * 60 * 60 * 1000;
	
	@Autowired
	ErrorInfoMapper errorInfoMapper;
	@Autowired
	LianjiaHouseDataMapper lianjiaHouseDataMapper;
	@Autowired
	LianjiaAddressInfoMapper lianjiaAddressInfoMapper;
	@Autowired
	ProcessorStartInfoMapper processorStartInfoMapper;
	
	static enum BaseMultiDataType
	{
		RoomNum,
		Area,
		FaceTo,
		Decoration,
		HasLift
	}
	
	static enum BuildMultiDataType
	{
		Flood,
		BuildTime,
		Structure
	}
	
	@PostConstruct
	public void onInit()
	{
		Timer timer = new Timer();
		timer.schedule(new LianJiaProcessorTask(), DELAY_TIME, INTERVAL_TIME);
	}

	public class LianJiaProcessorTask extends TimerTask 
	{
	    public void run() 
	    {
	    	ProcessorStartInfo processorStartInfo = new ProcessorStartInfo();
	    	processorStartInfo.setDelayTime(DELAY_TIME.intValue());
	    	processorStartInfo.setIntervalTime(INTERVAL_TIME.intValue());
	    	processorStartInfo.setProcessTag(processorName);
	    	processorStartInfo.setStartTime(new Date());
	    	processorStartInfoMapper.insertSelective(processorStartInfo);
	    	
	        getAllPage();
	    }
	}
	
	public void getAllPage()
	{
		List<String> allDBData = lianjiaHouseDataMapper.selectAllContent();
		List<LianJiaItemBean> allPagesData = new ArrayList<>();
		for(int i = 0;i < 100; i++)
		{
			allPagesData.addAll(getLianJiaPage("https://" + 
					ParamUtils.cityPinYinSimple + ".lianjia.com/ershoufang/pg" + i));
		}
		
		//差异分析
		for(Iterator<LianJiaItemBean> it = allPagesData.iterator(); it.hasNext();) 
		{
		     LianJiaItemBean bean = it.next();
		     if(checkAndDeleteString(allDBData, bean))
		    	 it.remove();
		}
		
		//allDBData 为需要删除的集合， allPagesData为需要增加的集合
		lianjiaHouseDataMapper.deleteByContentUrls(allDBData);
		
		for(LianJiaItemBean item : allPagesData)
		{
			if(lianjiaHouseDataMapper.selectNumByContentUrl(item.contentUrl) == 0)
				lianjiaHouseDataMapper.insertSelective(
						lianJiaItemBean2LianjiaHouseData(item));
			
			if(lianjiaAddressInfoMapper.selectNumByName(item.address) == 0)
				lianjiaAddressInfoMapper.insertSelective(getLianJiaAddress(item.address));
		}
	}
	
	private Boolean checkAndDeleteString(List<String> allDBData, LianJiaItemBean bean)
	{
		if(allDBData.contains(bean.contentUrl))
		{
			allDBData.remove(allDBData.indexOf(bean.contentUrl));
			return true;
		}
		return false;
	}
	
	private LianjiaAddressInfo getLianJiaAddress(String addressName)
	{
		LianjiaAddressInfo lianjiaAddressInfo = new LianjiaAddressInfo();
		lianjiaAddressInfo.setName(addressName);
		return AMapPointsUtils.getLocation(lianjiaAddressInfo);
	}
	
	private List<LianJiaItemBean> getLianJiaPage(String url)
	{
		List<LianJiaItemBean> result = new ArrayList<>();
		Document doc = null;
		try
		{
			doc = Jsoup.connect(url).get();
		}
		catch (IOException e)
		{
			addError(e.getMessage());
			return result;
		} 
		
		Elements dataList = doc.select(".sellListContent");
		Elements dataItemList = dataList.select(".clear");
		
		for(int i = 0;i < dataItemList.size(); i++)
		{
			if(dataItemList.get(i).attr("class").contains("info"))
				continue;
			LianJiaItemBean item = getLianJiaInfo(dataItemList.get(i));
			
			if(item != null)
				result.add(item);
		}
		return result;
	}
	
	//魔法代码，不要改，就是这么神奇……
	private LianJiaItemBean getLianJiaInfo(Element dataItem)
	{
		LianJiaItemBean bean = new LianJiaItemBean();
		try
		{
			bean.contentUrl = dataItem.select(".img").attr("href");
			bean.imgUrl = dataItem.select(".img").select(".lj-lazy").attr("data-original");
			bean.title = dataItem.select(".title").select("a").text();
			bean.address = dataItem.select(".houseInfo").select("a").text();
			
			String[] multiData = dataItem.select(".houseInfo").text().split("\\|");
			multiData[0] = "";
			bean.roomNum = getBaseMultiDataItem(BaseMultiDataType.RoomNum, multiData);
			bean.area = getBaseMultiDataItem(BaseMultiDataType.Area, multiData);
			bean.faceTo = getBaseMultiDataItem(BaseMultiDataType.FaceTo, multiData);
			bean.decoration = getBaseMultiDataItem(BaseMultiDataType.Decoration, multiData);
			bean.hasLift = getBaseMultiDataItem(BaseMultiDataType.HasLift, multiData);
			
			String buildData = dataItem.select(".positionInfo").text();
			bean.flood = getBuildMultiDataItem(BuildMultiDataType.Flood, buildData);
			bean.buildTime = getBuildMultiDataItem(BuildMultiDataType.BuildTime, buildData);
			bean.structure = getBuildMultiDataItem(BuildMultiDataType.Structure, buildData);
			
			bean.district = dataItem.select(".positionInfo").select("a").text();
			bean.taxFree = dataItem.select(".taxfree").text();
			bean.totalPrice = dataItem.select(".totalPrice").select("span").text();
			bean.unitPrice = dataItem.select(".unitPrice").attr("data-price");
		}
		catch(Exception e)
		{
			addError(dataItem.toString());
			return null;
		}
		return bean;
	}
	
	private String getBaseMultiDataItem(BaseMultiDataType type, String[] multiData)
	{
		List<String> targetList = new ArrayList<>();
		
		switch (type)
		{
		case RoomNum:
			targetList.add("室");
			targetList.add("厅");
			break;
		case Area:
			targetList.add("平米");
			break;
		case FaceTo:
			targetList.add("东");
			targetList.add("南");
			targetList.add("西");
			targetList.add("北");
			break;
		case Decoration:
			targetList.add("装");
			targetList.add("其他");
			break;
		case HasLift:
			targetList.add("电梯");
			break;
		}
		
		for(String data : multiData)
		{
			for(String target : targetList)
			{
				if(data.contains(target))
					return data.trim();
			}
		}
		
		return "";
	}
	
	private String getBuildMultiDataItem(BuildMultiDataType type, String buildData)
	{
		Boolean hasBrace = buildData.contains(")");
		
		switch(type)
		{
		case Flood:
			if(hasBrace)
				return buildData.split("\\)")[0] + ")";
			else
				return buildData.split("层")[0] + "层";
		case BuildTime:
			if(hasBrace)
			{
				if(!buildData.contains("年")) return "";
				return buildData.split("\\)")[1].split("年")[0];
			}
			else
			{
				if(!buildData.contains("年")) return "";
				return buildData.split("层")[1].split("年")[0];
			}
		case Structure:
			if(!buildData.contains("年"))
				return buildData.split("\\)")[1].split("-")[0].trim();
			return buildData.split("建")[1].split("-")[0].trim();
		}
		return "";
	}
	
	private LianjiaHouseData lianJiaItemBean2LianjiaHouseData(LianJiaItemBean item)
	{
		LianjiaHouseData houseData = new LianjiaHouseData();
		houseData.setAddress(item.address);
		houseData.setArea(Double.parseDouble(StringUtils.getNumWithPoint(item.area)));
		houseData.setBuildtime(StringUtils.isNullOrEmpty(item.buildTime)?
				0 : Integer.parseInt(item.buildTime));
		houseData.setContentUrl(item.contentUrl);
		houseData.setDecoration(item.decoration);
		houseData.setDistrict(item.district);
		houseData.setFaceto(item.faceTo);
		houseData.setFlood(item.flood);
		houseData.setHaslift(ByteBooleanUtils.boolean2Byte(item.hasLift.equals("有楼梯")));
		houseData.setImgUrl(item.imgUrl);
		houseData.setRoomNum(item.roomNum);
		houseData.setStructure(item.structure);
		houseData.setTaxfree(item.taxFree);
		houseData.setTitle(item.title);
		houseData.setTotalprice(Double.parseDouble(item.totalPrice));
		houseData.setUnitprice(Double.parseDouble(item.unitPrice));
		
		return houseData;
	}
	
	private void addError(String error)
	{
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorDate(new Date());
		errorInfo.setProcessorName(processorName);
		errorInfo.setErrorData(error.length() > 2000 ? 
				error.substring(0, 2000) : error);
		errorInfoMapper.insertSelective(errorInfo);
	}
}
