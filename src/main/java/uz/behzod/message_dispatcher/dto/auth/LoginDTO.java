package uz.behzod.message_dispatcher.dto.auth;

import jakarta.validation.constraints.NotNull;

public record LoginDTO(@NotNull String login, @NotNull String password) {
}
