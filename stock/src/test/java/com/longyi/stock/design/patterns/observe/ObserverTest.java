package com.longyi.stock.design.patterns.observe;

import org.junit.Test;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/20
 */
public class ObserverTest {


    @Test
    public void observerTest(){
        WeatherData weatherData=new WeatherData();
        CurrentConditionsDisplay currentConditionsDisplay=new CurrentConditionsDisplay(weatherData);
        ForecastDisplay forecastDisplay=new ForecastDisplay(weatherData);
        weatherData.setMeasurements(90,20,90);
        weatherData.setMeasurements(82,89,29f);

    }

}    
   