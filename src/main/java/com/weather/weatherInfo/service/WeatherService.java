package com.weather.weatherInfo.service;

import com.weather.database.dataSet.CurrentWeatherInfo;
import com.weather.weatherInfo.exeptions.WrongApiException;
import com.weather.weatherInfo.weatherSiteAnalizers.OpenWeatherWeatherAnalizer;
import com.weather.weatherInfo.weatherSiteAnalizers.WeatherSiteAnalizer;
import com.weather.weatherInfo.weatherSiteAnalizers.WeatherbitWeatherAnalizer;
import com.weather.weatherInfo.weatherSiteAnalizers.WorldWeatherOnlineWeatherAnalizer;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public final class WeatherService {
    private static final Map<String, WeatherSiteAnalizer> analizers = createNewMapOfAnalizers();
    private static int commonChacheTime;

    private static Map<String, WeatherSiteAnalizer> createNewMapOfAnalizers(){
        Map<String, WeatherSiteAnalizer> analizerMap = new HashMap<>();
        try {
            analizerMap.put("bit", WeatherbitWeatherAnalizer.getWeatherbitWeatherAnalizer("7ad40a66bb224e9d9338823380f8d6c2"));
            analizerMap.put("world", WorldWeatherOnlineWeatherAnalizer.getWorldWeatherOnlineWeatherAnalizer("ed13eb9cd3af45e9a25111238202202"));
            analizerMap.put("open", OpenWeatherWeatherAnalizer.getOpenWeatherWeatherAnalizer("18d82c5a0c9f97611a9864ef7b3c2d34"));
        } catch (WrongApiException | JSONException e) {
            System.out.println(e.getMessage());
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

    public JSONObject getWeatherJson(String serviceName, String city) throws JSONException{
        DecimalFormat decimalFormat = new DecimalFormat("###.###");
        JSONObject weatherJson = new JSONObject();
        WeatherSiteAnalizer analizer = analizers.get(serviceName);
        CurrentWeatherInfo currentWeatherInfo = analizer.getWeatherFromCity(city);

        weatherJson.put("provider", analizer.getProviderName());
        weatherJson.put("city", currentWeatherInfo.getCity());
        weatherJson.put("time", currentWeatherInfo.getTime());
        weatherJson.put("temp(C)", decimalFormat.format(currentWeatherInfo.getTemp()));
        weatherJson.put("speedOfWind(mps)", decimalFormat.format(currentWeatherInfo.getSpeedOfWind()));

        return weatherJson;
    }
}
