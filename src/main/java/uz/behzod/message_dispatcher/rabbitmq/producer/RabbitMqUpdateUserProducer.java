package uz.behzod.message_dispatcher.rabbitmq.producer;

import org.springframework.stereotype.Component;
import uz.behzod.message_dispatcher.dto.payload.UpdateUserPayload;

@Component
public class RabbitMqUpdateUserProducer extends RabbitMqBaseProducer {

    public void updateUserName(UpdateUserPayload payload) {
        sendToQueue(payload, "UPDATE_USER_NAME_QUEUE", "UPDATE_USER_NAME_PAYLOAD", 1);
    }

    public void changePassword(UpdateUserPayload payload) {
        sendToQueue(payload, "CHANGE_USER_PASSWORD_QUEUE", "CHANGE_USER_PASSWORD_PAYLOAD", 1);
    }

}
