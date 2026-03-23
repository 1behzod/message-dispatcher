package uz.behzod.message_dispatcher.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.behzod.message_dispatcher.service.MessageService;

import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageController {

    MessageService messageService;

    @PostMapping("/generate")
    public ResponseEntity<Void> generate(@RequestHeader("X-User-Id") UUID userId) {
        messageService.generate(userId);
        return ResponseEntity.ok().build();
    }
}