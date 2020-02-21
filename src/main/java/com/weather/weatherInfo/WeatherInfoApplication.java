package com.weather.weatherInfo;

import com.weather.weatherInfo.exeptions.WrongApiException;
import com.weather.weatherInfo.weatherSiteAnalizers.WeatherSiteAnalizer;
import com.weather.weatherInfo.weatherSiteAnalizers.WeatherbitWeatherAnalizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherInfoApplication.class, args);
	}

}
