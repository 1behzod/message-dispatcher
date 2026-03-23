package uz.behzod.message_dispatcher.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uz.behzod.message_dispatcher.domain.User;
import uz.behzod.message_dispatcher.dto.user.LoginDTO;
import uz.behzod.message_dispatcher.dto.user.RegisterDTO;
import uz.behzod.message_dispatcher.repository.UserRepository;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder encoder;

    public void register(RegisterDTO registerDTO) {
        if (userRepository.findByLogin(registerDTO.login()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login already taken");
        User user = new User();
        user.setLogin(registerDTO.login());
        user.setPassword(encoder.encode(registerDTO.password()));
        user.setFullName(registerDTO.fullName());
        userRepository.save(user);
    }

    public Long login(LoginDTO loginDTO) {
        User user = userRepository.findByLogin(loginDTO.login())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (!encoder.matches(loginDTO.password(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return user.getId();
    }
}