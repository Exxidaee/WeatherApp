package org.yunya;

import org.junit.jupiter.api.Test;
import org.yunya.service.WeatherAnalyticsService;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticsTest {

    @Test
    void testAddWeatherData() {
        WeatherAnalyticsService analytics = new WeatherAnalyticsService();
        
        WeatherData weather = new WeatherData();
        weather.setCity("Москва");
        weather.setTemperature(20.0);
        weather.setCondition("солнечно");
        weather.setWeatherDate(LocalDateTime.now());
        
        analytics.addWeatherData(weather);
        
        Map<String, Object> result = analytics.getAnalyticsSummary();
        
        assertEquals(1L, result.get("totalRecords"));
        assertEquals(1L, result.get("totalCities"));
    }

    @Test
    void testMultipleCities() {
        WeatherAnalyticsService analytics = new WeatherAnalyticsService();
        
        WeatherData moscow = new WeatherData();
        moscow.setCity("Москва");
        moscow.setTemperature(25.0);
        moscow.setCondition("солнечно");
        analytics.addWeatherData(moscow);
        
        WeatherData spb = new WeatherData();
        spb.setCity("Санкт-Петербург");
        spb.setTemperature(15.0);
        spb.setCondition("дождь");
        analytics.addWeatherData(spb);
        
        Map<String, Object> result = analytics.getAnalyticsSummary();
        
        assertEquals(2L, result.get("totalRecords"));
        assertEquals(2L, result.get("totalCities"));
        assertEquals(1L, result.get("totalSunny"));
        assertEquals(1L, result.get("totalRainy"));
    }

    @Test
    void testEmptyAnalytics() {
        WeatherAnalyticsService analytics = new WeatherAnalyticsService();
        
        Map<String, Object> result = analytics.getAnalyticsSummary();
        
        assertEquals("Нет данных для анализа", result.get("message"));
    }
} 