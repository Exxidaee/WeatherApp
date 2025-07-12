package org.yunya.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.yunya.WeatherData;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherAnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherAnalyticsService.class);

    private final Map<String, List<WeatherData>> cityWeatherData = new ConcurrentHashMap<>();

    public void addWeatherData(WeatherData weatherData) {
        cityWeatherData.computeIfAbsent(weatherData.getCity(), k -> new ArrayList<>())
                      .add(weatherData);
    }

    @Scheduled(fixedRate = 10000)
    public void printAnalytics() {
        logger.info("=== АНАЛИТИКА ПОГОДЫ ===");
        
        for (Map.Entry<String, List<WeatherData>> entry : cityWeatherData.entrySet()) {
            String city = entry.getKey();
            List<WeatherData> data = entry.getValue();
            
            if (data.isEmpty()) continue;
            
            long sunnyDays = data.stream().filter(w -> "солнечно".equals(w.getCondition())).count();
            long rainyDays = data.stream().filter(w -> "дождь".equals(w.getCondition())).count();
            long cloudyDays = data.stream().filter(w -> "облачно".equals(w.getCondition())).count();
            
            double avgTemp = data.stream().mapToDouble(WeatherData::getTemperature).average().orElse(0.0);
            double maxTemp = data.stream().mapToDouble(WeatherData::getTemperature).max().orElse(0.0);
            double minTemp = data.stream().mapToDouble(WeatherData::getTemperature).min().orElse(0.0);
            
            WeatherData hottestDay = data.stream()
                .max(Comparator.comparingDouble(WeatherData::getTemperature))
                .orElse(null);
            
            WeatherData coldestDay = data.stream()
                .min(Comparator.comparingDouble(WeatherData::getTemperature))
                .orElse(null);
            
            logger.info("Город: {}", city);
            logger.info("  - Всего записей: {}", data.size());
            logger.info("  - Солнечных дней: {}", sunnyDays);
            logger.info("  - Дождливых дней: {}", rainyDays);
            logger.info("  - Облачных дней: {}", cloudyDays);
            logger.info("  - Средняя температура: {}°C", String.format("%.1f", avgTemp));
            logger.info("  - Максимальная температура: {}°C", String.format("%.1f", maxTemp));
            logger.info("  - Минимальная температура: {}°C", String.format("%.1f", minTemp));
            
            if (hottestDay != null) {
                logger.info("  - Самая жаркая погода: {}°C {} {}", 
                    String.format("%.1f", hottestDay.getTemperature()), hottestDay.getCondition(), hottestDay.getWeatherDate());
            }
            
            if (coldestDay != null) {
                logger.info("  - Самая холодная погода: {}°C {} {}", 
                    String.format("%.1f", coldestDay.getTemperature()), coldestDay.getCondition(), coldestDay.getWeatherDate());
            }
            
            logger.info("");
        }
        
        if (!cityWeatherData.isEmpty()) {
            logger.info("=== ОБЩАЯ СТАТИСТИКА ===");
            
            String cityWithMostRain = cityWeatherData.entrySet().stream()
                .max(Comparator.comparingLong(entry -> 
                    entry.getValue().stream().filter(w -> "дождь".equals(w.getCondition())).count()))
                .map(Map.Entry::getKey)
                .orElse("нет данных");
            
            String cityWithHighestAvgTemp = cityWeatherData.entrySet().stream()
                .max(Comparator.comparingDouble(entry -> 
                    entry.getValue().stream().mapToDouble(WeatherData::getTemperature).average().orElse(0.0)))
                .map(Map.Entry::getKey)
                .orElse("нет данных");
            
            String cityWithLowestAvgTemp = cityWeatherData.entrySet().stream()
                .min(Comparator.comparingDouble(entry -> 
                    entry.getValue().stream().mapToDouble(WeatherData::getTemperature).average().orElse(0.0)))
                .map(Map.Entry::getKey)
                .orElse("нет данных");
            
            logger.info("Город с самым большим количеством дождливых дней: {}", cityWithMostRain);
            logger.info("Город с самой высокой средней температурой: {}", cityWithHighestAvgTemp);
            logger.info("Город с самой низкой средней температурой: {}", cityWithLowestAvgTemp);
            logger.info("=====================================");
        }
    }

    public Map<String, Object> getAnalyticsSummary() {
        Map<String, Object> summary = new HashMap<>();
        
        if (cityWeatherData.isEmpty()) {
            summary.put("message", "Нет данных для анализа");
            return summary;
        }
        
        long totalRecords = cityWeatherData.values().stream().mapToLong(List::size).sum();
        long totalCities = cityWeatherData.size();
        
        long totalSunny = cityWeatherData.values().stream()
            .flatMap(List::stream)
            .filter(w -> "солнечно".equals(w.getCondition()))
            .count();
        
        long totalRainy = cityWeatherData.values().stream()
            .flatMap(List::stream)
            .filter(w -> "дождь".equals(w.getCondition()))
            .count();
        
        long totalCloudy = cityWeatherData.values().stream()
            .flatMap(List::stream)
            .filter(w -> "облачно".equals(w.getCondition()))
            .count();
        
        double overallAvgTemp = cityWeatherData.values().stream()
            .flatMap(List::stream)
            .mapToDouble(WeatherData::getTemperature)
            .average()
            .orElse(0.0);
        
        summary.put("totalRecords", totalRecords);
        summary.put("totalCities", totalCities);
        summary.put("totalSunny", totalSunny);
        summary.put("totalRainy", totalRainy);
        summary.put("totalCloudy", totalCloudy);
        summary.put("overallAvgTemperature", overallAvgTemp);
        summary.put("lastUpdated", LocalDateTime.now());
        
        return summary;
    }

    public Map<String, Object> getDetailedAnalytics() {
        Map<String, Object> detailed = new HashMap<>();
        
        if (cityWeatherData.isEmpty()) {
            detailed.put("message", "Нет данных для анализа");
            return detailed;
        }
        
        List<Map<String, Object>> cityAnalytics = new ArrayList<>();
        
        for (Map.Entry<String, List<WeatherData>> entry : cityWeatherData.entrySet()) {
            String city = entry.getKey();
            List<WeatherData> data = entry.getValue();
            
            if (data.isEmpty()) continue;
            
            long sunnyDays = data.stream().filter(w -> "солнечно".equals(w.getCondition())).count();
            long rainyDays = data.stream().filter(w -> "дождь".equals(w.getCondition())).count();
            long cloudyDays = data.stream().filter(w -> "облачно".equals(w.getCondition())).count();
            
            double avgTemp = data.stream().mapToDouble(WeatherData::getTemperature).average().orElse(0.0);
            double maxTemp = data.stream().mapToDouble(WeatherData::getTemperature).max().orElse(0.0);
            double minTemp = data.stream().mapToDouble(WeatherData::getTemperature).min().orElse(0.0);
            
            WeatherData hottestDay = data.stream()
                .max(Comparator.comparingDouble(WeatherData::getTemperature))
                .orElse(null);
            
            WeatherData coldestDay = data.stream()
                .min(Comparator.comparingDouble(WeatherData::getTemperature))
                .orElse(null);
            
            Map<String, Object> cityData = new HashMap<>();
            cityData.put("city", city);
            cityData.put("totalRecords", data.size());
            cityData.put("sunnyDays", sunnyDays);
            cityData.put("rainyDays", rainyDays);
            cityData.put("cloudyDays", cloudyDays);
            cityData.put("avgTemperature", String.format("%.1f", avgTemp));
            cityData.put("maxTemperature", String.format("%.1f", maxTemp));
            cityData.put("minTemperature", String.format("%.1f", minTemp));
            
            if (hottestDay != null) {
                cityData.put("hottestDay", String.format("%.1f°C %s", hottestDay.getTemperature(), hottestDay.getCondition()));
            }
            
            if (coldestDay != null) {
                cityData.put("coldestDay", String.format("%.1f°C %s", coldestDay.getTemperature(), coldestDay.getCondition()));
            }
            
            cityAnalytics.add(cityData);
        }
        
        // Общая статистика
        String cityWithMostRain = cityWeatherData.entrySet().stream()
            .max(Comparator.comparingLong(entry -> 
                entry.getValue().stream().filter(w -> "дождь".equals(w.getCondition())).count()))
            .map(Map.Entry::getKey)
            .orElse("нет данных");
        
        String cityWithHighestAvgTemp = cityWeatherData.entrySet().stream()
            .max(Comparator.comparingDouble(entry -> 
                entry.getValue().stream().mapToDouble(WeatherData::getTemperature).average().orElse(0.0)))
            .map(Map.Entry::getKey)
            .orElse("нет данных");
        
        String cityWithLowestAvgTemp = cityWeatherData.entrySet().stream()
            .min(Comparator.comparingDouble(entry -> 
                entry.getValue().stream().mapToDouble(WeatherData::getTemperature).average().orElse(0.0)))
            .map(Map.Entry::getKey)
            .orElse("нет данных");
        
        detailed.put("cityAnalytics", cityAnalytics);
        detailed.put("cityWithMostRain", cityWithMostRain);
        detailed.put("cityWithHighestAvgTemp", cityWithHighestAvgTemp);
        detailed.put("cityWithLowestAvgTemp", cityWithLowestAvgTemp);
        detailed.put("lastUpdated", LocalDateTime.now());
        
        return detailed;
    }
}