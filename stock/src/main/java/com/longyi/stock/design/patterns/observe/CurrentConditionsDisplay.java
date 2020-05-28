package com.longyi.stock.design.patterns.observe;

import java.util.Observable;
import java.util.Observer;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/20
 */
public class CurrentConditionsDisplay implements Observer {
   Observable observable;
   private float temperature;
   private float humidity;

   public CurrentConditionsDisplay(Observable observable){
       this.observable=observable;
       observable.addObserver(this);
   }


    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof  WeatherData) {
            WeatherData weatherData = (WeatherData) o;
            this.temperature = weatherData.getTemperature();
            this.humidity = weatherData.getHumidity();
            disPlay();
        }
    }

    public void disPlay(){
        System.out.println("Current conditions:"+temperature+":degress and"+humidity);
    }
}
   