package com.spring.mars.MarsRoverApi.web;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.mars.MarsRoverApi.dto.HomeDTO;
import com.spring.mars.MarsRoverApi.response.MarsRoverApiResponse;
import com.spring.mars.MarsRoverApi.service.MarsRoverApiService;


@Controller
public class HomeController {

	@Autowired
	private MarsRoverApiService roverService;
	
	@GetMapping("/")
	public String getHomeView (ModelMap model, Long userId, Boolean createUser) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	    HomeDTO homeDto = createDefaultHomeDto(userId);

	    if (Boolean.TRUE.equals(createUser) && userId == null) {
	      homeDto = roverService.save(homeDto);
	    } else {
	      homeDto = roverService.findByUserId(userId); 
	      if (homeDto == null) {
	          homeDto = createDefaultHomeDto(userId);
	        }
	    }
		
		MarsRoverApiResponse roverData= roverService.getRoverData(homeDto);
		model.put("roverData", roverData);
		model.put("homeDto", homeDto);
		 model.put("validCameras", roverService.getValidCameras().get(homeDto.getMarsApiRoverData()));
		 if (!Boolean.TRUE.equals(homeDto.getRememberPreferences()) && userId != null) {
		      HomeDTO defaultHomeDto = createDefaultHomeDto(userId);
		      roverService.save(defaultHomeDto);
		    }
		 return "index";
	}
	 @GetMapping("/savedPreferences")
	  @ResponseBody
	  public HomeDTO getSavedPreferences (Long userId) {
	    if (userId != null)
	      return roverService.findByUserId(userId);
	    else
	      return createDefaultHomeDto(userId);
	  }
	 private HomeDTO createDefaultHomeDto(Long userId) {
		    HomeDTO homeDto = new HomeDTO();
		    homeDto.setMarsApiRoverData("Opportunity");
		    homeDto.setMarsSol(1);
		    homeDto.setUserId(userId);
		    return homeDto;
		  }
	 @PostMapping("/")
	  public String postHomeView (HomeDTO homeDto) {
		 homeDto = roverService.save(homeDto);
		    return "redirect:/?userId="+homeDto.getUserId();
	  }
	
}
