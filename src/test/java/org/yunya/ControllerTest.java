package org.yunya;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.yunya.controller.WeatherController;
import org.yunya.service.WeatherAnalyticsService;
import org.yunya.service.WeatherProducerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerTest {

    @Test
    void testHomePage() {
        WeatherAnalyticsService analyticsService = mock(WeatherAnalyticsService.class);
        WeatherProducerService producerService = mock(WeatherProducerService.class);
        
        WeatherController controller = new WeatherController(analyticsService, producerService);
        
        Model model = mock(Model.class);
        
        String result = controller.home(model);
        
        assertEquals("home", result);
    }

    @Test
    void testGenerateData() {
        WeatherAnalyticsService analyticsService = mock(WeatherAnalyticsService.class);
        WeatherProducerService producerService = mock(WeatherProducerService.class);
        
        WeatherController controller = new WeatherController(analyticsService, producerService);
        
        String result = controller.generateWeatherData();
        
        assertEquals("redirect:/", result);
        verify(producerService, times(1)).generateAndSendWeatherData();
    }
} 