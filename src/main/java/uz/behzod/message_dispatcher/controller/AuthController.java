package uz.behzod.message_dispatcher.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.behzod.message_dispatcher.dto.auth.ChangePasswordDTO;
import uz.behzod.message_dispatcher.dto.auth.LoginDTO;
import uz.behzod.message_dispatcher.dto.auth.RegisterDTO;
import uz.behzod.message_dispatcher.service.AuthService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, UUID>> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(Map.of("userId", authService.login(loginDTO)));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@RequestParam UUID id, ChangePasswordDTO changePasswordDTO) {
        authService.changePassword(id, changePasswordDTO);
        return ResponseEntity.ok().build();
    }
}