package uz.behzod.message_dispatcher.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import uz.behzod.message_dispatcher.dto.payload.UpdateUserPayload;

@Component
@Slf4j
public class RabbitMqUpdateUserConsumer {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(durable = "false", value = "UPDATE_USER_NAME_QUEUE"),
                    exchange = @Exchange(durable = "false", value = "delayedExchange", delayed = "true"),
                    key = "UPDATE_USER_NAME_QUEUE"
            ),
            containerFactory = "rabbitListenerContainerFactoryMin"
    )
    public void updateUserName(UpdateUserPayload payload) {
        try {
            //          TODO logic for update user name notification
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("Update user name error: " + e.getMessage());
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(durable = "false", value = "CHANGE_USER_PASSWORD_QUEUE"),
                    exchange = @Exchange(durable = "false", value = "delayedExchange", delayed = "true"),
                    key = "CHANGE_USER_PASSWORD_QUEUE"
            ),
            containerFactory = "rabbitListenerContainerFactoryMin"
    )
    public void changePassword(UpdateUserPayload payload) {
        try {
            //          TODO logic for change password
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("Update user name error: " + e.getMessage());
        }
    }
}
