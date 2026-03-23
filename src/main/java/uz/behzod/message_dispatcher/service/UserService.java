package uz.behzod.message_dispatcher.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.behzod.message_dispatcher.domain.User;
import uz.behzod.message_dispatcher.dto.payload.UpdateUserPayload;
import uz.behzod.message_dispatcher.dto.user.UserDetailDTO;
import uz.behzod.message_dispatcher.rabbitmq.producer.RabbitMqUpdateUserProducer;
import uz.behzod.message_dispatcher.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;
    RabbitMqUpdateUserProducer producer;

    public UserDetailDTO getDetailById(UUID id) {
        return userRepository.findById(id)
                .map(user -> new UserDetailDTO(user.getFullName(), user.getLogin()))
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional
    public void updateName(UUID id, String fullName) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        UpdateUserPayload payload = new UpdateUserPayload();
        payload.setOld(user.getFullName());
        payload.setUpdated(fullName);
        user.setFullName(fullName);
        userRepository.save(user);
        producer.updateUserName(payload);
    }
}
