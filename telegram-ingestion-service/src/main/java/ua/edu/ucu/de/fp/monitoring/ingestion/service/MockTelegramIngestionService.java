package ua.edu.ucu.de.fp.monitoring.ingestion.service;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import tools.jackson.databind.json.JsonMapper;
import ua.edu.ucu.de.fp.monitoring.ingestion.model.TelegramMessage;

/**
 * Mock service that generates random Telegram messages from Ukrainian locations.
 * This simulates data ingestion without requiring actual Telegram API integration.
 * 
 * Note: For production, consider using TDLight Java library for Telegram API:
 * https://github.com/tdlight-team/tdlight-java
 */
@Service
@ConditionalOnProperty(name = "ingestion.mock.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class MockTelegramIngestionService {
    
    private final RabbitTemplate rabbitTemplate;
    private final JsonMapper jsonMapper;
    
    @Value("${ingestion.queue.name}")
    private String queueName;
    
    @Value("${ingestion.mock.interval-seconds}")
    private int intervalSeconds;
    
    private final Random random = new Random();
    
    // Mock Ukrainian cities with coordinates
    private final List<GroupLocation> ukrainianGroups = List.of(
        new GroupLocation("Kyiv Tech Community", "https://t.me/kyiv_tech", 50.4501, 30.5234),
        new GroupLocation("Lviv Developers", "https://t.me/lviv_dev", 49.8397, 24.0297),
        new GroupLocation("Odesa IT Hub", "https://t.me/odesa_it", 46.4825, 30.7233),
        new GroupLocation("Kharkiv Coders", "https://t.me/kharkiv_code", 49.9935, 36.2304),
        new GroupLocation("Dnipro DevOps", "https://t.me/dnipro_devops", 48.4647, 35.0462),
        new GroupLocation("Zaporizhzhia JS", "https://t.me/zp_js", 47.8388, 35.1396),
        new GroupLocation("Vinnytsia Python", "https://t.me/vn_python", 49.2328, 28.4681),
        new GroupLocation("Chernivtsi Tech", "https://t.me/cv_tech", 48.2916, 25.9356)
    );
    
    private final List<String> mockMessages = List.of(
        "Check out this new project!",
        "Meeting tomorrow at 10 AM",
        "Great discussion today!",
        "Warning: server maintenance scheduled",
        "New tutorial on reactive programming",
        "Alert: high CPU usage detected",
        "Code review session at 3 PM",
        "Urgent: deployment issue found",
        "Congratulations on the release!",
        "Crisis: database connection lost"
    );
    
    // Functional supplier for generating random messages
    private final Supplier<TelegramMessage> messageGenerator = () -> {
        GroupLocation group = ukrainianGroups.get(random.nextInt(ukrainianGroups.size()));
        String content = mockMessages.get(random.nextInt(mockMessages.size()));
        
        // Add some random variation to coordinates
        double latVariation = (random.nextDouble() - 0.5) * 0.1;
        double lonVariation = (random.nextDouble() - 0.5) * 0.1;
        
        return TelegramMessage.create(
            group.name(),
            group.link(),
            group.latitude() + latVariation,
            group.longitude() + lonVariation,
            content
        );
    };
    
    @PostConstruct
    public void startIngestion() {
        log.info("Starting mock Telegram ingestion service with interval {} seconds", intervalSeconds);
        
        // Reactive stream: infinite flux with intervals
        Flux.interval(Duration.ofSeconds(intervalSeconds))
            .map(tick -> messageGenerator.get())
            .doOnNext(message -> log.info("Generated message: {} from {}", 
                                         message.content(), message.groupName()))
            .map(this::toJson)
            .filter(json -> json != null)
            .subscribe(
                json -> rabbitTemplate.convertAndSend(queueName, json),
                error -> log.error("Error in ingestion stream", error)
            );
    }
    
    private String toJson(TelegramMessage message) {
        try {
            return jsonMapper.writeValueAsString(message);
        } catch (Exception e) {
            log.error("Error serializing message", e);
            return null;
        }
    }
    
    private record GroupLocation(String name, String link, Double latitude, Double longitude) {}
}
