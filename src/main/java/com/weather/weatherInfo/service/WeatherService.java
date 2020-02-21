package com.weather.weatherInfo.service;

import com.weather.database.dataSet.CurrentWeatherInfo;
import com.weather.weatherInfo.exeptions.WrongApiException;
import com.weather.weatherInfo.weatherSiteAnalizers.WeatherSiteAnalizer;
import com.weather.weatherInfo.weatherSiteAnalizers.WeatherbitWeatherAnalizer;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {
    private static final Map<String, WeatherSiteAnalizer> analizers = createNewMapOfAnalizers();
    private static int commonChacheTime;

    private static Map<String, WeatherSiteAnalizer> createNewMapOfAnalizers(){
        Map<String, WeatherSiteAnalizer> analizerMap = new HashMap<>();
        try {
            analizerMap.put("bit", WeatherbitWeatherAnalizer.GetWeatherbitWeatherAnalizer("7ad40a66bb224e9d9338823380f8d6c2"));
        } catch (WrongApiException e) {
            e.printStackTrace();
        }
        return analizerMap;
    }

    public void setChacheTime(int commonChacheTime){
        WeatherService.commonChacheTime = commonChacheTime;
        for(Map.Entry<String, WeatherSiteAnalizer> entry : WeatherService.analizers.entrySet()){
            entry.getValue().setCacheTime(WeatherService.commonChacheTime);
        }
    }

    public JSONObject getWeatherJson(String serviceName, String city){
        DecimalFormat decimalFormat = new DecimalFormat("###.###");
        JSONObject weatherJson = new JSONObject();
        WeatherSiteAnalizer analizer = analizers.get(serviceName);
        CurrentWeatherInfo currentWeatherInfo = analizer.getWeatherFromCity(city);

        try {
            weatherJson.put("provider", analizer.getProviderName());
            weatherJson.put("city", currentWeatherInfo.getCity());
            weatherJson.put("time", currentWeatherInfo.getTime());
            weatherJson.put("temp", decimalFormat.format(currentWeatherInfo.getTemp()));
            weatherJson.put("speedOfWind", decimalFormat.format(currentWeatherInfo.getSpeedOfWind()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherJson;
    }
}
