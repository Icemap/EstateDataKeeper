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

import com.wqz.estate.keeper.bean.AnJuKeItemBean;
import com.wqz.estate.keeper.dao.AnjukeAddressInfoMapper;
import com.wqz.estate.keeper.dao.AnjukeHouseDataMapper;
import com.wqz.estate.keeper.dao.ErrorInfoMapper;
import com.wqz.estate.keeper.dao.ProcessorStartInfoMapper;
import com.wqz.estate.keeper.pojo.AnjukeAddressInfo;
import com.wqz.estate.keeper.pojo.AnjukeHouseData;
import com.wqz.estate.keeper.pojo.ErrorInfo;
import com.wqz.estate.keeper.pojo.ProcessorStartInfo;
import com.wqz.estate.keeper.utils.AMapPointsUtils;
import com.wqz.estate.keeper.utils.ParamUtils;
import com.wqz.estate.keeper.utils.StringUtils;

@Service
@Transactional
public class AnJuKeProcessor
{
	public final static String processorName = "AnJuKeProcessor";
	public final static Long DELAY_TIME = 1L * 60 * 60 * 1000;
	public final static Long INTERVAL_TIME = 2L * 60 * 60 * 1000;
	
	@Autowired
	ProcessorStartInfoMapper processorStartInfoMapper;
	@Autowired
	ErrorInfoMapper errorInfoMapper;
	
	@Autowired
	AnjukeAddressInfoMapper anjukeAddressInfoMapper;
	@Autowired
	AnjukeHouseDataMapper anjukeHouseDataMapper; 
	
	@PostConstruct
	public void onInit()
	{
		Timer timer = new Timer();
		timer.schedule(new AnJuKeProcessorTask(), DELAY_TIME, INTERVAL_TIME);
	}
	
	public class AnJuKeProcessorTask extends TimerTask 
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
		List<String> allDBData = anjukeHouseDataMapper.selectAllContent();
		List<AnJuKeItemBean> allPagesData = new ArrayList<>();
		for(int i = 0;i < 50; i++)
		{
			allPagesData.addAll(getAnJuKePage("https://" +
					ParamUtils.cityPinYin + ".anjuke.com/sale/p" + i));
		}
		
		//差异分析
		for(Iterator<AnJuKeItemBean> it = allPagesData.iterator(); it.hasNext();) 
		{
			AnJuKeItemBean bean = it.next();
		    if(checkAndDeleteString(allDBData, bean))
		    	 it.remove();
		}
		
		//allDBData 为需要删除的集合， allPagesData为需要增加的集合
		anjukeHouseDataMapper.deleteByContentUrls(allDBData);
		for(AnJuKeItemBean item : allPagesData)
		{
			if(anjukeHouseDataMapper.selectNumByContentUrl(item.contentUrl) == 0)
				anjukeHouseDataMapper.insertSelective(
						anjukeItemBean2AnjukeHouseData(item));
			
			if(anjukeAddressInfoMapper.selectNumByName(item.address) == 0)
				anjukeAddressInfoMapper.insertSelective(getLianJiaAddress(item.address));
		}
	}
	
	private Boolean checkAndDeleteString(List<String> allDBData, AnJuKeItemBean bean)
	{
		if(allDBData.contains(bean.contentUrl))
		{
			allDBData.remove(allDBData.indexOf(bean.contentUrl));
			return true;
		}
		return false;
	}
	
	private List<AnJuKeItemBean> getAnJuKePage(String url)
	{
		List<AnJuKeItemBean> result = new ArrayList<>();
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
		
		Elements rootUL = doc.select(".houselist-mod-new");
		Elements itemsList = rootUL.select(".list-item");
		
		for(int i = 0;i < itemsList.size(); i++)
		{
			AnJuKeItemBean item = getAnJuKeInfo(itemsList.get(i));
			
			if(item != null)
				result.add(item);
		}
		return result;
	}
	
	//又是一个魔法，怕不怕……
	private AnJuKeItemBean getAnJuKeInfo(Element item)
	{
		AnJuKeItemBean bean = new AnJuKeItemBean();
		try
		{
			bean.title = item.select(".house-title").select("a").attr("title");
			bean.imgUrl = item.select(".item-img").select("img").attr("src");
			bean.contentUrl = item.select(".house-title").select("a").attr("href").split("\\?")[0];
	
			bean.roomNum = item.select(".details-item").select("span").get(0).text();
			bean.area = item.select(".details-item").select("span").get(1).text();
			bean.flood = item.select(".details-item").select("span").get(2).text();
			bean.buildTime = item.select(".details-item").select("span").get(3).text();
			
			bean.address = item.select(".comm-address").text().split("\\s")[0];
			bean.district = item.select(".comm-address").text().split("\\s")[1];
			
			bean.totalPrice = item.select(".price-det").select("strong").text();
			bean.unitPrice = item.select(".unit-price").text().split("元")[0];
			
			bean.goodAt = margeEleListString(item.select(".tags-bottom").select("span"));
		}
		catch(Exception e)
		{
			addError(item.toString());
			return null;
		}
		return bean;
	}
	
	private String margeEleListString(Elements eleList)
	{
		if(eleList.size() == 0)
			return "";
		
		String result = "";
		for(Element ele : eleList)
		{
			result += ele.text();
			result += "|";
		}
		return result.substring(0, result.length() - 1);
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

	private AnjukeHouseData anjukeItemBean2AnjukeHouseData(AnJuKeItemBean item)
	{
		AnjukeHouseData anjukeHouseData = new AnjukeHouseData();
		anjukeHouseData.setAddress(item.address);
		anjukeHouseData.setArea(Double.parseDouble(StringUtils.getNumWithPoint(item.area)));
		anjukeHouseData.setBuildtime(Integer.parseInt(StringUtils.getNumWithPoint(item.buildTime)));
		anjukeHouseData.setContenturl(item.contentUrl);
		anjukeHouseData.setDistrict(item.district);
		anjukeHouseData.setFlood(item.flood);
		anjukeHouseData.setGoodat(item.goodAt);
		anjukeHouseData.setImgurl(item.imgUrl);
		anjukeHouseData.setRoomnum(item.roomNum);
		anjukeHouseData.setTitle(item.title);
		anjukeHouseData.setTotalprice(Double.parseDouble(item.totalPrice));
		anjukeHouseData.setUnitprice(Double.parseDouble(item.unitPrice));
		
		return anjukeHouseData;
	}

	private AnjukeAddressInfo getLianJiaAddress(String addressName)
	{
		AnjukeAddressInfo anjukeAddressInfo = new AnjukeAddressInfo();
		anjukeAddressInfo.setName(addressName);
		return AMapPointsUtils.getLocation(anjukeAddressInfo);
	}
}
