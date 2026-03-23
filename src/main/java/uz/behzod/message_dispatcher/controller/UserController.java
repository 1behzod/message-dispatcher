package uz.behzod.message_dispatcher.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.behzod.message_dispatcher.dto.user.UserDetailDTO;
import uz.behzod.message_dispatcher.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {

    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDTO> getDetailById(@RequestParam UUID id) {
        return ResponseEntity.ok(userService.getDetailById(id));
    }

    @PutMapping("/{id}/full-name")
    public ResponseEntity<Void> updateName(@PathVariable UUID id, @RequestParam String fullName) {
        userService.updateName(id, fullName);
        return ResponseEntity.ok().build();
    }
}
