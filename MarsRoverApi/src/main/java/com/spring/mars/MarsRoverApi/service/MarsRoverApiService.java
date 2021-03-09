package com.spring.mars.MarsRoverApi.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.spring.mars.MarsRoverApi.dto.HomeDTO;
import com.spring.mars.MarsRoverApi.repository.PreferencesRepository;
import com.spring.mars.MarsRoverApi.response.MarsPhoto;
import com.spring.mars.MarsRoverApi.response.MarsRoverApiResponse;

@Service
public class MarsRoverApiService {

	 private static final String API_KEY = "scpGuZB1sOvBfrUxdlPqiaYdzx2Ss4ym5BnNoals";
	 
	 private Map<String, List<String>> validCameras = new HashMap<>();
	 @Autowired
	  private PreferencesRepository preferencesRepo;

	  public MarsRoverApiService () {
	    validCameras.put("Opportunity", Arrays.asList("FHAZ", "RHAZ", "NAVCAM", "PANCAM", "MINITES"));
	    validCameras.put("Curiosity", Arrays.asList("FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI", "MARDI", "NAVCAM"));
	    validCameras.put("Spirit", Arrays.asList("FHAZ", "RHAZ", "NAVCAM", "PANCAM", "MINITES"));
	  }
	public MarsRoverApiResponse getRoverData(HomeDTO homeDto) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	
		RestTemplate rt= new RestTemplate();
	
		
		List<String> apiUrlEnpoints = getApiUrlEnpoints(homeDto);
	    List<MarsPhoto> photos = new ArrayList<>();
	    MarsRoverApiResponse response = new MarsRoverApiResponse();

	    apiUrlEnpoints.stream()
	                  .forEach(url -> { 
	                    MarsRoverApiResponse apiResponse = rt.getForObject(url, MarsRoverApiResponse.class);
	                    photos.addAll(apiResponse.getPhotos());
	                  });

	    response.setPhotos(photos);

	    return response;
	
}
	public List<String> getApiUrlEnpoints (HomeDTO homeDto) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	    List<String> urls = new ArrayList<>();
	    
	    Method[] methods = homeDto.getClass().getMethods();

	    // This code will grab all getCamera* methods and (if value returns true) then we will build a API URL to
	    //  call in order to fetch pictures for a given rover / camera / sol.
	    for (Method method : methods) {
	      if (method.getName().indexOf("getCamera") > -1 && Boolean.TRUE.equals(method.invoke(homeDto))) {
	        String cameraName = method.getName().split("getCamera")[1].toUpperCase();
	        if (validCameras.get(homeDto.getMarsApiRoverData()).contains(cameraName)) {
	          urls.add("https://api.nasa.gov/mars-photos/api/v1/rovers/"+homeDto.getMarsApiRoverData()+"/photos?sol="+homeDto.getMarsSol()+"&api_key=" + API_KEY + "&camera=" + cameraName);
	        }
	      }
	}
	    return urls;
	  }
	public Map<String, List<String>> getValidCameras() {
	    return validCameras;
	  }
	public HomeDTO save(HomeDTO homeDto) {
	    return preferencesRepo.save(homeDto);
	  }

	  public HomeDTO findByUserId(Long userId) {
	    return preferencesRepo.findByUserId(userId);
	  }
	}

