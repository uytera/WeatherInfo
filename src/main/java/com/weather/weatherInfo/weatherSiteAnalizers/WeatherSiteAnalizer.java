package com.weather.weatherInfo.weatherSiteAnalizers;

import com.weather.weatherInfo.exeptions.WrongApiException;
import com.weather.database.dataSet.CurrentWeatherInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public abstract class WeatherSiteAnalizer {
    protected long cacheTimeInSeconds = 600;
    protected String apiKey;
    protected String providerName;

    public String getProviderName(){
        return providerName;
    }

    public abstract void setApi(String apiKey) throws WrongApiException;

    public String getApi(){
        return apiKey;
    }

    public long getCacheTime(){
        return cacheTimeInSeconds;
    }

    public void setCacheTime(int cacheTimeInSeconds){
        this.cacheTimeInSeconds = cacheTimeInSeconds;
    }

    protected static String readAll(Reader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int curretChar;
        while ((curretChar = bufferedReader.read()) != -1) {
            stringBuilder.append((char) curretChar);
        }
        return stringBuilder.toString();
    }

    protected static JSONObject readJsonFromUrl(String url){
        JSONObject json = null;

        try(InputStream inputStream = new URL(url).openStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = readAll(bufferedReader);
            json = new JSONObject(jsonText);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public abstract CurrentWeatherInfo getWeatherFromCity(String cityName);

    protected abstract void pushWeatherIntoBase(String provider, String city, float temp, float speedOfWind);

    protected abstract void pushWeatherIntoBase(CurrentWeatherInfo currentWeatherInfo);

    protected abstract CurrentWeatherInfo getWeatherFromBase(String provider, String city);

    protected abstract void deleteWeatherFromBase(CurrentWeatherInfo currentWeatherInfo);
}
