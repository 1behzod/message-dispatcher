package uz.behzod.message_dispatcher.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
@Lazy
public class RabbitMQConfig {

    private final ObjectMapper objectMapper;

    public RabbitMQConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Primary
    @Bean
    @Lazy
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        return buildSimpleRabbitListenerContainerFactory(connectionFactory, 4, 8);
    }

    @Bean
    @Lazy
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactoryMin(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        return buildSimpleRabbitListenerContainerFactory(connectionFactory, 1, 1);
    }

    @Bean
    @Lazy
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactoryMax(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        return buildSimpleRabbitListenerContainerFactory(connectionFactory, 10, 20);
    }

    private SimpleRabbitListenerContainerFactory buildSimpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory, Integer concurrentConsumers, Integer maxConcurrentConsumers) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(producerJackson2MessageConverter());
        factory.setConcurrentConsumers(concurrentConsumers);
        factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
        return factory;
    }

    @Bean
    @Lazy
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter(objectMapper);
        jackson2JsonMessageConverter.setClassMapper(classMapper());
        return jackson2JsonMessageConverter;
    }

    @Bean
    @Lazy
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }

    @Bean
    @Lazy
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("map", HashMap.class);
        classMapper.setIdClassMapping(idClassMapping);
        classMapper.setTrustedPackages("*");
        return classMapper;
    }
}