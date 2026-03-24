package uz.behzod.message_dispatcher.rabbitmq.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class RabbitMqBaseProducer {

    private static final String X_DELAY = "x-delay";
    private static final String TYPE_ID = "__TypeId__";
    //   private static final String AUTHORIZATION = "Authorization";
    private static final String DELAY_EXCHANGE_NAME = "delayedExchange";

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    protected void sendToQueue(Object object, String queueName, String payload) {
        this.sendToQueue(object, queueName, payload, 2);
    }

    protected void sendToQueue(Object object, String queueName, String payload, int delay) {
        try {
            String payloadString = objectMapper.writeValueAsString(object);
            Message jsonMessage = MessageBuilder
                    .withBody(payloadString.getBytes())
                    .andProperties(
                            MessagePropertiesBuilder
                                    .newInstance()
                                    .setContentType(MediaType.APPLICATION_JSON_VALUE)
                                    .setHeader(X_DELAY, (1000 * delay))
                                    .setHeader(TYPE_ID, payload)
//                                    .setHeader(AUTHORIZATION, SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getCredentials() : null)
                                    .build()
                    )
                    .build();
            rabbitTemplate.send(DELAY_EXCHANGE_NAME, queueName, jsonMessage);
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("Send data to the Rabbit MQ error: " + e.getMessage());
        }
    }
}
