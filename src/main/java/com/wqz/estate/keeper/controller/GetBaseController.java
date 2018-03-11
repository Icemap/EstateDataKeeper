package com.wqz.estate.keeper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wqz.estate.keeper.processer.AnJuKeProcessor;

@Controller
@RequestMapping("/keepAlive")
public class GetBaseController
{
	@Autowired
	AnJuKeProcessor anJuKeProcessor;
	
	@ResponseBody
	@RequestMapping("/get")
	public Boolean alive()
	{
		return true;
	}
}
