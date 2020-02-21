package com.weather.weatherInfo.exeptions;

public class WrongApiException extends Exception {
    public WrongApiException(){
        super("entered API is incorrect");
    }
}
