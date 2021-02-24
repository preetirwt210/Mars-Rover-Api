package com.spring.mars.MarsRoverApi;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String getHomeView(ModelMap theModel) {
		theModel.put("name", "Preeti Rawat");
		return "index";
	}
}
