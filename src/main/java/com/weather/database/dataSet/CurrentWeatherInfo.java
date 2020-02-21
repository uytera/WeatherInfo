package com.weather.database.dataSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name= "weatherChache")
public class CurrentWeatherInfo {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "provider")
    private String provider;
    @Column(name = "time")
    private Date time;
    @Column(name = "city")
    private String city;
    @Column(name = "temp")
    private float temp;
    @Column(name = "speedOfWind")
    private float speedOfWind;

    public CurrentWeatherInfo(){

    }

    public CurrentWeatherInfo(String provider, String city, float temp, float speedOfWind){
        this.id = provider.hashCode() ^ city.hashCode();
        this.provider = provider;
        this.time = new Date();
        this.city = city;
        this.temp = temp;
        this.speedOfWind = speedOfWind;
    }

    public long getId(){ return id; }
    public String getProvider(){ return provider; }
    public Date getTime(){ return time; }
    public String getCity(){ return city; }
    public float getTemp(){ return temp; }
    public float getSpeedOfWind(){ return speedOfWind; }
}
