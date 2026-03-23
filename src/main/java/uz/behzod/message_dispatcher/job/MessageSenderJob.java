package uz.behzod.message_dispatcher.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.behzod.message_dispatcher.service.MessageService;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSenderJob {

    private final MessageService messageService;

 //   @Scheduled(fixedDelayString = "${scheduling.fixed-delay-ms:30000}")
    public void process() {
        log.info("MessageSenderJob triggered");
        messageService.processNextBatch();
    }
}