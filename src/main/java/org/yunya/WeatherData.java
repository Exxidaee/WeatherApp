package org.yunya;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

import java.time.LocalDateTime;

public class WeatherData {
    @NotNull
    private String city;
    
    @DecimalMin(value = "-50.0")
    @DecimalMax(value = "50.0")
    private double temperature;
    
    @NotNull
    private String condition;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime weatherDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public WeatherData() {}

    public WeatherData(String city, double temperature, String condition, LocalDateTime weatherDate) {
        this.city = city;
        this.temperature = temperature;
        this.condition = condition;
        this.weatherDate = weatherDate;
        this.timestamp = LocalDateTime.now();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public LocalDateTime getWeatherDate() {
        return weatherDate;
    }

    public void setWeatherDate(LocalDateTime weatherDate) {
        this.weatherDate = weatherDate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        WeatherData that = (WeatherData) o;
        
        if (Double.compare(that.temperature, temperature) != 0) return false;
        if (!city.equals(that.city)) return false;
        if (!condition.equals(that.condition)) return false;
        return weatherDate != null ? weatherDate.equals(that.weatherDate) : that.weatherDate == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = city.hashCode();
        temp = Double.doubleToLongBits(temperature);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + condition.hashCode();
        result = 31 * result + (weatherDate != null ? weatherDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "city='" + city + '\'' +
                ", temperature=" + temperature +
                ", condition='" + condition + '\'' +
                ", weatherDate=" + weatherDate +
                ", timestamp=" + timestamp +
                '}';
    }
}