package com.longyi.stock.design.patterns.observe;

import java.util.Observable;
import java.util.Observer;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/20
 */
public class ForecastDisplay implements Observer {

    private Observable observable;
    private float currentPressure = 22.92f;
    private float lastPressure;

    public ForecastDisplay(Observable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) o;
            this.currentPressure = weatherData.getPressure();
            disPlay();
        }
    }

    public void disPlay() {
        System.out.println("current press is:" + currentPressure);
    }
}
   