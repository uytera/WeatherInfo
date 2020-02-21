package com.weather.weatherInfo.controller;

import com.weather.weatherInfo.service.WeatherService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//import org.springframework.web.bind.annotation.RestController;

@Controller
public class WeatherController {
    @Autowired
    WeatherService weatherService;

    @RequestMapping(path = "/weatherService/getWeather", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getWeather(@ModelAttribute("serviceName") String serviceName, @ModelAttribute("city") String city){
        JSONObject weatherJson = null;
        try {
            weatherJson = weatherService.getWeatherJson(serviceName, city);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(weatherJson);
    }

    @RequestMapping(path = "/weatherService/setChacheTime", method = RequestMethod.POST)
    public String setChache(@ModelAttribute("time") String text){
        weatherService.setChacheTime(Integer.parseInt(text));
        return "redirect:/weatherService/";
    }
}
