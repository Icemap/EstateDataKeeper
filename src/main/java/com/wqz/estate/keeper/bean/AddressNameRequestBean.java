package com.wqz.estate.keeper.bean;

import java.util.List;

public class AddressNameRequestBean
{
	private List<String> lianjiaAddressList;
	private List<String> anjukeAddressList;
	
	public List<String> getLianjiaAddressList()
	{
		return lianjiaAddressList;
	}
	public void setLianjiaAddressList(List<String> lianjiaAddressList)
	{
		this.lianjiaAddressList = lianjiaAddressList;
	}
	public List<String> getAnjukeAddressList()
	{
		return anjukeAddressList;
	}
	public void setAnjukeAddressList(List<String> anjukeAddressList)
	{
		this.anjukeAddressList = anjukeAddressList;
	}
}
