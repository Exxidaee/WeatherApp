package org.yunya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.yunya.service.WeatherAnalyticsService;
import org.yunya.service.WeatherProducerService;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class WeatherController {

    private final WeatherAnalyticsService analyticsService;
    private final WeatherProducerService producerService;

    @Autowired
    public WeatherController(WeatherAnalyticsService analyticsService, WeatherProducerService producerService) {
        this.analyticsService = analyticsService;
        this.producerService = producerService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentTime", LocalDateTime.now());
        model.addAttribute("analytics", analyticsService.getAnalyticsSummary());
        model.addAttribute("detailedAnalytics", analyticsService.getDetailedAnalytics());
        return "home";
    }

    @PostMapping("/generate")
    public String generateWeatherData() {
        try {
            producerService.generateAndSendWeatherData();
        } catch (Exception e) {
            // Логируем ошибку, но не прерываем выполнение
            System.err.println("Ошибка при генерации данных: " + e.getMessage());
        }
        return "redirect:/";
    }
}