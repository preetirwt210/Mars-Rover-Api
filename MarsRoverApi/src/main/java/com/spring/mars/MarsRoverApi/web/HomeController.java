package com.spring.mars.MarsRoverApi.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.util.StringUtils;

import com.spring.mars.MarsRoverApi.dto.HomeDTO;
import com.spring.mars.MarsRoverApi.response.MarsRoverApiResponse;
import com.spring.mars.MarsRoverApi.service.MarsRoverApiService;


@Controller
public class HomeController {

	@Autowired
	private MarsRoverApiService roverService;
	
	@GetMapping("/")
	public String getHomeView(ModelMap model,HomeDTO homeDto) {
		if(StringUtils.isEmpty(homeDto.getMarsApiRoverData())) {
			homeDto.setMarsApiRoverData("Opportunity");
		}
		if(homeDto.getMarsSol() ==null)
			homeDto.setMarsSol(1);
		MarsRoverApiResponse roverData= roverService.getRoverData(homeDto.getMarsApiRoverData() , homeDto.getMarsSol());
		model.put("roverData", roverData);
		model.put("homeDto", homeDto);
		return "index";
	}
	
	
}
