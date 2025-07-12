package org.yunya.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.yunya.WeatherData;

@Service
public class WeatherConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherConsumerService.class);

    private final ObjectMapper objectMapper;
    private final WeatherAnalyticsService analyticsService;

    public WeatherConsumerService(ObjectMapper objectMapper, WeatherAnalyticsService analyticsService) {
        this.objectMapper = objectMapper;
        this.analyticsService = analyticsService;
    }

    @KafkaListener(topics = "${weather.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeWeatherData(String message) {
        try {
            WeatherData weatherData = objectMapper.readValue(message, WeatherData.class);
            analyticsService.addWeatherData(weatherData);
            
            logger.info("Received weather data: {} - {:.1f}°C, {}", 
                weatherData.getCity(), 
                weatherData.getTemperature(), 
                weatherData.getCondition());
                
        } catch (Exception e) {
            logger.error("Error parsing weather data: {}", e.getMessage());
        }
    }
}