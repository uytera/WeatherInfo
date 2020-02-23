package com.weather.weatherInfo.exeptions;

public class WrongProviderException extends Exception {
    public WrongProviderException(){
        super("entered provider is incorrect");
    }
}
