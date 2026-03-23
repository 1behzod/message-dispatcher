package uz.behzod.message_dispatcher.dto.user;

public record UserEvent(Long userId, String type, String payload) {
}
