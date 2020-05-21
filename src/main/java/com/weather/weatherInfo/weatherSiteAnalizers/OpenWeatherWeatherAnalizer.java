package com.weather.weatherInfo.weatherSiteAnalizers;

import com.weather.database.DBService;
import com.weather.database.dataSet.CurrentWeatherInfo;
import com.weather.weatherInfo.exeptions.WrongApiException;
import com.weather.weatherInfo.exeptions.WrongCityException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class OpenWeatherWeatherAnalizer extends WeatherSiteAnalizer {
    private DBService dbService = new DBService();
    private OpenWeatherWeatherAnalizer(){}

    public static OpenWeatherWeatherAnalizer getOpenWeatherWeatherAnalizer(String apiKey) throws WrongApiException, JSONException{
        OpenWeatherWeatherAnalizer currentAnalizer = new OpenWeatherWeatherAnalizer();
        currentAnalizer.setApi(apiKey);
        currentAnalizer.providerName = "openWeather";
        return currentAnalizer;
    }
    @Override
    public void setApi(String apiKey) throws WrongApiException, JSONException {
        JSONObject json = null;
        try {
            json = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?q=Paris&appid=" + apiKey + "&units=metric");
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
    public CurrentWeatherInfo getWeatherFromCity(String cityName) throws WrongCityException, JSONException {
        CurrentWeatherInfo currentWeatherInfo = getWeatherFromChache(cityName);

        if(currentWeatherInfo != null) {
            return currentWeatherInfo;
        }
        System.out.println("http://api.openweathermap.org/data/2.5/weather?q=" + cityName +"&appid=" + apiKey);
        JSONObject json = null;
        json = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?q=" + cityName +"&appid=" + apiKey + "&units=metric");

        currentWeatherInfo = new CurrentWeatherInfo(getProviderName(), cityName,
                (float) json.getJSONObject("main").getDouble("temp"),
                (float) json.getJSONObject("wind").getInt("speed")
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
