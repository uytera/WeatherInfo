package com.weather.weatherInfo.controller;

import com.weather.weatherInfo.exeptions.WrongCityException;
import com.weather.weatherInfo.exeptions.WrongProviderException;
import com.weather.weatherInfo.model.WeatherModel;
import com.weather.weatherInfo.service.WeatherService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public final class WeatherController {
    @Autowired
    WeatherService weatherService;

    @RequestMapping(path = "/weatherService/getWeather", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getWeather(@ModelAttribute("serviceName") String serviceName, @ModelAttribute("city") String city){
        JSONObject errorJson = new JSONObject();
        WeatherModel weatherJson = null;
        try {
            weatherJson = weatherService.getWeatherJson(serviceName, city);
        }
        catch (WrongCityException | JSONException e) {
            return new ResponseEntity<>(new Error("wrong city"), HttpStatus.BAD_REQUEST);
        }
        catch (WrongProviderException e) {
            return new ResponseEntity<>(new Error("wrong provider"), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new Error("Server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(weatherJson, HttpStatus.OK);
    }

    @RequestMapping(path = "/weatherService/setChacheTime", method = RequestMethod.POST)
    public String setChache(@ModelAttribute("time") String text){
        weatherService.setChacheTime(Integer.parseInt(text));
        return "redirect:/weatherService/";
    }

    class Error{
        private String error;

        Error(String error){
            this.error = error;
        }

        public String getError() { return error; }
        public void setError(String error){ this.error = error; }
    }
}
