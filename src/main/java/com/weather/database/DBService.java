package com.weather.database;


import com.weather.database.dataSet.CurrentWeatherInfo;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import com.weather.database.dao.WeatherCacheDAO;

public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update";

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getH2Configuration();
        sessionFactory = createSessionFactory(configuration);
    }

    private Configuration getH2Configuration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(CurrentWeatherInfo.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/world?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "root");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }


    public CurrentWeatherInfo getWeather(String provider, String city) {
        Session session = sessionFactory.openSession();
        WeatherCacheDAO dao = new WeatherCacheDAO(session);

        try {
            CurrentWeatherInfo dataSet = dao.getWeatherInfo(provider, city);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            session.close();
            throw null;
        }
    }

    public void addWeather(String provider, String city, float temp, float speedOfWind) {
        Session session = sessionFactory.openSession();
        WeatherCacheDAO dao = new WeatherCacheDAO(session);
        try {
            dao.insertWeatherInfo(provider, city, temp, speedOfWind);
            session.close();
        } catch (HibernateException e) {
            session.close();
            throw null;
        }
    }

    public void addWeather(CurrentWeatherInfo currentWeatherInfo) {
        Session session = sessionFactory.openSession();
        WeatherCacheDAO dao = new WeatherCacheDAO(session);
        Transaction transaction = session.beginTransaction();
        try {
            dao.insertWeatherInfo(currentWeatherInfo);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            transaction.rollback();
            session.close();
            System.out.println(e.getMessage());
            throw null;
        }
    }

    public void deleteWeather(CurrentWeatherInfo currentWeatherInfo) {
        Session session = sessionFactory.openSession();
        WeatherCacheDAO dao = new WeatherCacheDAO(session);
        Transaction transaction = session.beginTransaction();
        try {
            dao.deleteWeatherInfo(currentWeatherInfo);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            transaction.rollback();
            session.close();
            System.out.println(e.getMessage());
            throw null;
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
