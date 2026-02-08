package ua.edu.ucu.de.fp.monitoring.ingestion.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    @Value("${ingestion.queue.name}")
    private String queueName;
    
    @Bean
    public Queue telegramEventsQueue() {
        return new Queue(queueName, true);
    }
}
