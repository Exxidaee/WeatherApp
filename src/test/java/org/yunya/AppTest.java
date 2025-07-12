package org.yunya;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testWeatherDataCreation() {
        WeatherData weather = new WeatherData();
        
        assertNotNull(weather);
        
        weather.setCity("Екатеринбург");
        weather.setTemperature(18.5);
        weather.setCondition("облачно");
        
        assertEquals("Екатеринбург", weather.getCity());
        assertEquals(18.5, weather.getTemperature());
        assertEquals("облачно", weather.getCondition());
    }

    @Test
    void testTemperatureFormatting() {
        double temp1 = 25.678;
        double temp2 = 15.0;
        
        String formatted1 = String.format(java.util.Locale.US, "%.1f", temp1);
        String formatted2 = String.format(java.util.Locale.US, "%.1f", temp2);
        
        assertEquals("25.7", formatted1);
        assertEquals("15.0", formatted2);
    }

    @Test
    void testWeatherConditions() {
        String[] conditions = {"солнечно", "облачно", "дождь"};
        
        for (String condition : conditions) {
            WeatherData weather = new WeatherData();
            weather.setCondition(condition);
            assertEquals(condition, weather.getCondition());
        }
    }

    @Test
    void testCities() {
        String[] cities = {"Москва", "Санкт-Петербург", "Екатеринбург"};
        
        for (String city : cities) {
            WeatherData weather = new WeatherData();
            weather.setCity(city);
            assertEquals(city, weather.getCity());
        }
    }
} 