package uz.behzod.message_dispatcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uz.behzod.message_dispatcher.domain.Message;
import uz.behzod.message_dispatcher.enums.MessageStatus;
import uz.behzod.message_dispatcher.repository.MessageRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageStatusService {

    private final MessageRepository messageRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markSent(Message message) {
        message.setStatus(MessageStatus.SENT);
        message.setLockedAt(null);
        messageRepository.save(message);
        log.info("Message {} marked as SENT", message.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(Message message) {
        message.setStatus(MessageStatus.FAILED);
        message.setLockedAt(null);
        messageRepository.save(message);
        log.warn("Message {} marked as FAILED", message.getId());
    }
}
