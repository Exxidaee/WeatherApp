package org.yunya.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.yunya.WeatherData;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class WeatherProducerService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherProducerService.class);

    @Value("${weather.topic}")
    private String weatherTopic;

    private final List<String> cities = Arrays.asList(
        "Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург", "Казань",
        "Нижний Новгород", "Самара", "Тюмень", "Краснодар", "Сочи"
    );

    private final List<String> conditions = Arrays.asList("солнечно", "облачно", "дождь");

    @Value("${weather.temperature.min:0}")
    private double minTemperature;

    @Value("${weather.temperature.max:35}")
    private double maxTemperature;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Random random;

    public WeatherProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.random = new Random();
    }

    @Scheduled(fixedRate = 2000)
    public void generateAndSendWeatherData() {
        try {
            String city = cities.get(random.nextInt(cities.size()));
            double temperature = minTemperature + random.nextDouble() * (maxTemperature - minTemperature);
            String condition = conditions.get(random.nextInt(conditions.size()));
            
            LocalDateTime startDate = LocalDateTime.now().minusDays(7);
            LocalDateTime weatherDate = startDate.plusMinutes(random.nextInt(7 * 24 * 12));
            
            WeatherData weatherData = new WeatherData(city, temperature, condition, weatherDate);
            String jsonData = objectMapper.writeValueAsString(weatherData);
            
            try {
                CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(weatherTopic, city, jsonData);
                
                future.whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Weather data sent for {}: {:.1f}°C, {}", 
                            city, temperature, condition);
                    } else {
                        logger.warn("Could not send to Kafka for {}: {:.1f}°C, {} - {}", 
                            city, temperature, condition, ex.getMessage());
                    }
                });
            } catch (Exception e) {
                logger.warn("Kafka not available, but data generated: {} - {:.1f}°C, {}", 
                    city, temperature, condition);
            }
            
        } catch (JsonProcessingException e) {
            logger.error("Error serializing weather data: {}", e.getMessage());
        }
    }
}