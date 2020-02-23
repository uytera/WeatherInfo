package com.weather.weatherInfo.exeptions;

public class WrongCityException extends Exception {
    public WrongCityException(){
        super("entered city is incorrect");
    }
}
