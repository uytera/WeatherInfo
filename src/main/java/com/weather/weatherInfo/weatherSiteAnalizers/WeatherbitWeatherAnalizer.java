package com.weather.weatherInfo.weatherSiteAnalizers;

import com.weather.database.DBService;
import com.weather.database.dataSet.CurrentWeatherInfo;
import com.weather.weatherInfo.exeptions.WrongApiException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class WeatherbitWeatherAnalizer extends WeatherSiteAnalizer {
    private DBService dbService = new DBService();

    private WeatherbitWeatherAnalizer(){
    }

    public static WeatherbitWeatherAnalizer GetWeatherbitWeatherAnalizer(String apiKey) throws WrongApiException{
        WeatherbitWeatherAnalizer currentAnalizer = new WeatherbitWeatherAnalizer();
        currentAnalizer.setApi(apiKey);
        currentAnalizer.providerName = "weatherbit";
        return currentAnalizer;
    }


    @Override
    public void setApi(String apiKey) throws WrongApiException {
        JSONObject json = readJsonFromUrl("https://api.weatherbit.io/v2.0/current?city=Moscow&key=" + apiKey);
        if(json == null){
            throw new WrongApiException();
        }
        this.apiKey = apiKey;
        return;
    }

    @Override
    public CurrentWeatherInfo getWeatherFromCity(String cityName) {
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

        System.out.println("https://api.weatherbit.io/v2.0/current?city="+ cityName +"&key=" + apiKey);
        JSONObject json = readJsonFromUrl("https://api.weatherbit.io/v2.0/current?city="+ cityName +"&key=" + apiKey);

        try {
            currentWeatherInfo = new CurrentWeatherInfo(getProviderName(), cityName,
                    Float.parseFloat(json.getJSONArray("data").getJSONObject(0).get("temp").toString()),
                    Float.parseFloat(json.getJSONArray("data").getJSONObject(0).get("wind_spd").toString())
            );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

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
}
