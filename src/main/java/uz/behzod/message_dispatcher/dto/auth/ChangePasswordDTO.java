package uz.behzod.message_dispatcher.dto.auth;

import jakarta.validation.constraints.NotNull;

public record ChangePasswordDTO(@NotNull String currentPassword, @NotNull String newPassword) {
}
