package com.longyi.stock.design.patterns.observe;

import lombok.Data;

import java.util.Observable;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/20
 */
@Data
public class WeatherData extends Observable {

    private float temperature;
    private float humidity;
    private float pressure;

    public void measurementsChanged(){
        setChanged();
        notifyObservers();
    }
    public void setMeasurements(float temperature,float humidity,float pressure){
        this.temperature=temperature;
        this.humidity=humidity;
        this.pressure=pressure;
        measurementsChanged();
    }
}    
   