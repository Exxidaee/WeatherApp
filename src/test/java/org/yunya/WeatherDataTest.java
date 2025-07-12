package org.yunya;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeatherDataTest {

    @Test
    void testCreateWeatherData() {
        WeatherData weather = new WeatherData();
        
        weather.setCity("Москва");
        weather.setTemperature(25.5);
        weather.setCondition("солнечно");
        
        assertEquals("Москва", weather.getCity());
        assertEquals(25.5, weather.getTemperature());
        assertEquals("солнечно", weather.getCondition());
    }

    @Test
    void testTemperatureRange() {
        WeatherData weather = new WeatherData();
        
        weather.setTemperature(-5.0);
        assertEquals(-5.0, weather.getTemperature());
        
        weather.setTemperature(30.0);
        assertEquals(30.0, weather.getTemperature());
    }

    @Test
    void testWeatherConditions() {
        WeatherData weather = new WeatherData();
        
        weather.setCondition("солнечно");
        assertEquals("солнечно", weather.getCondition());
        
        weather.setCondition("облачно");
        assertEquals("облачно", weather.getCondition());
        
        weather.setCondition("дождь");
        assertEquals("дождь", weather.getCondition());
    }
} 