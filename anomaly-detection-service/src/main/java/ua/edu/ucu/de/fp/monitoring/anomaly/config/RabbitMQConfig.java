package ua.edu.ucu.de.fp.monitoring.anomaly.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    @Value("${detection.source-queue}")
    private String sourceQueue;
    
    @Value("${detection.target-queue}")
    private String targetQueue;
    
    @Bean
    public Queue sourceQueue() {
        return new Queue(sourceQueue, true);
    }
    
    @Bean
    public Queue targetQueue() {
        return new Queue(targetQueue, true);
    }
}
