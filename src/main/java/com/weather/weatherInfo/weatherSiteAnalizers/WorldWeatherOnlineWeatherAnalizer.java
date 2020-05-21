package com.weather.weatherInfo.weatherSiteAnalizers;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.weather.database.DBService;
import com.weather.database.dataSet.CurrentWeatherInfo;
import com.weather.weatherInfo.exeptions.WrongApiException;
import com.weather.weatherInfo.exeptions.WrongCityException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class WorldWeatherOnlineWeatherAnalizer extends WeatherSiteAnalizer {
    private DBService dbService = new DBService();

    private WorldWeatherOnlineWeatherAnalizer(){}

    public static WorldWeatherOnlineWeatherAnalizer getWorldWeatherOnlineWeatherAnalizer(String apiKey) throws WrongApiException, JSONException{
        WorldWeatherOnlineWeatherAnalizer currentAnalizer = new WorldWeatherOnlineWeatherAnalizer();
        currentAnalizer.setApi(apiKey);
        currentAnalizer.providerName = "worldWeatherOnline";
        return currentAnalizer;
    }

    @Override
    public void setApi(String apiKey) throws WrongApiException, JSONException {
        JSONObject json = null;
        try {
            json = readJsonFromUrl("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=" + apiKey + "&q=Paris&format=json&extra=localObsTime");
        } catch (WrongCityException e) {
            e.printStackTrace();
        }
        if(json == null){
            throw new WrongApiException();
        }
        this.apiKey = apiKey;
        return;
    }

    @Override
    public CurrentWeatherInfo getWeatherFromCity(String cityName) throws JSONException, WrongCityException {
        CurrentWeatherInfo currentWeatherInfo = getWeatherFromChache(cityName);

        if(currentWeatherInfo != null) {
            return currentWeatherInfo;
        }
        System.out.println("https://api.weatherbit.io/v2.0/current?city="+ cityName +"&key=" + apiKey);
        JSONObject json = readJsonFromUrl("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=" + apiKey + "&q=" + cityName + "&format=json&extra=localObsTime");
        JSONObject currentWeatherJson = json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0);

        currentWeatherInfo = new CurrentWeatherInfo(getProviderName(), cityName,
                Float.parseFloat(currentWeatherJson.getString("temp_C")),
                Float.parseFloat(currentWeatherJson.getString("windspeedKmph"))/3600*1000
        );

        pushWeatherIntoBase(currentWeatherInfo);
        return currentWeatherInfo;
    }

    @Override
    protected void pushWeatherIntoBase(String provider, String city, float temp, float speedOfWind) {
        dbService.addWeather(provider, city, temp, speedOfWind);
    }

    @Override
    protected void pushWeatherIntoBase(CurrentWeatherInfo currentWeatherInfo) {
        dbService.addWeather(currentWeatherInfo);
    }

    @Override
    protected CurrentWeatherInfo getWeatherFromBase(String provider, String city) {
        return dbService.getWeather(provider, city);
    }

    @Override
    protected void deleteWeatherFromBase(CurrentWeatherInfo currentWeatherInfo) {
        dbService.deleteWeather(currentWeatherInfo);
    }

    @Override
    protected CurrentWeatherInfo getWeatherFromChache(String cityName) {
        CurrentWeatherInfo currentWeatherInfo = getWeatherFromBase(getProviderName(), cityName);

        if(currentWeatherInfo != null) {
            Date currentWeatherDate = currentWeatherInfo.getTime();
            Date currentDate = new Date();
            long timeDifference = (currentDate.getTime() - currentWeatherDate.getTime())/1000;
            if(timeDifference <= cacheTimeInSeconds){
                return currentWeatherInfo;
            }
            deleteWeatherFromBase(currentWeatherInfo);
        }
        return null;
    }
}
