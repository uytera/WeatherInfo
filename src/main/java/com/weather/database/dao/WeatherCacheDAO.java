package com.weather.database.dao;

import com.weather.database.dataSet.CurrentWeatherInfo;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class WeatherCacheDAO {
    private Session session;

    public WeatherCacheDAO(Session session) {
        this.session = session;
    }

    public CurrentWeatherInfo getWeatherInfo(String provider, String city) throws HibernateException {
        Criteria criteria = session.createCriteria(CurrentWeatherInfo.class);
        return ((CurrentWeatherInfo) criteria.add(Restrictions.eq("provider", provider)).add(Restrictions.eq("city", city)).uniqueResult());
    }

    public void deleteWeatherInfo(CurrentWeatherInfo currentWeatherInfo) throws HibernateException {
        session.delete(currentWeatherInfo);
    }

    public String insertWeatherInfo(String provider, String city, float temp, float speedOfWind) throws HibernateException {
        return (String) session.save(new CurrentWeatherInfo(provider,  city, temp, speedOfWind));
    }

    public long insertWeatherInfo(CurrentWeatherInfo currentWeatherInfo) throws HibernateException {
        return (long) session.save(currentWeatherInfo);
    }
}

