package uz.behzod.message_dispatcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import uz.behzod.message_dispatcher.config.AppProperties;
import uz.behzod.message_dispatcher.domain.Message;
import uz.behzod.message_dispatcher.enums.MessageStatus;
import uz.behzod.message_dispatcher.repository.MessageRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final ExecutorService messageSenderThreadPool;
    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    // ── Called by the job ──────────────────────────────────────────────────

    public void processNextBatch() {
        List<Message> batch = fetchAndLock();
        if (batch.isEmpty()) {
            log.debug("No pending messages found.");
            return;
        }

        log.info("Processing batch of {} messages", batch.size());

        List<CompletableFuture<Void>> futures = batch.stream()
                .map(m -> CompletableFuture
                        .runAsync(() -> send(m), messageSenderThreadPool)
                        .exceptionally(ex -> {
                            log.error("Unexpected error sending message {}: {}", m.getId(), ex.getMessage());
                            return null;
                        }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    // ── Fetch and lock batch (own transaction) ─────────────────────────────

    @Transactional
    public List<Message> fetchAndLock() {
        List<Message> batch = messageRepository.findAndLockPending(appProperties.getMessage().getBatchSize());
        if (!batch.isEmpty()) {
            Instant now = Instant.now();
            batch.forEach(m -> m.setLockedAt(now));
            messageRepository.saveAll(batch);
        }
        return batch;
    }

    // ── Send with retry (each message its own transaction) ────────────────

    public void send(Message message) {
        int maxRetries = appProperties.getMessage().getMaxRetries();
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                restTemplate.postForEntity(
                        message.getUrl(),
                        Map.of("payload", message.getName()),
                        Void.class
                );
                markSent(message);
                log.info("Message {} sent successfully on attempt {}", message.getId(), attempt);
                return;
            } catch (Exception e) {
                log.warn("Message {} attempt {}/{} failed: {}",
                        message.getId(), attempt, maxRetries, e.getMessage());
                if (attempt == maxRetries) {
                    markFailed(message);
                }
            }
        }
    }

    // ── Status updates — each in its own NEW transaction ──────────────────

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markSent(Message message) {
        message.setStatus(MessageStatus.SENT);
        message.setLockedAt(null);
        message.setRetryCount(message.getRetryCount() + 1);
        messageRepository.save(message);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(Message message) {
        message.setStatus(MessageStatus.FAILED);
        message.setLockedAt(null);
        message.setRetryCount(message.getRetryCount() + 1);
        messageRepository.save(message);
    }

    // ── Generate 1000 messages ─────────────────────────────────────────────

    @Transactional
    public void generate(Long userId) {
        List<Message> messages = IntStream.rangeClosed(1, 1000)
                .mapToObj(number -> {
                    Message message = new Message();
                    message.setName("Message " + number);
                    message.setStatus(MessageStatus.NEW);
                    message.setUrl(appProperties.getMessage().getTargetUrl());
                    message.setAppId(appProperties.getMessage().getAppId());
                    return message;
                }).toList();
        messageRepository.saveAll(messages);
        log.info("Generated 1000 messages for user {}", userId);
    }
}
