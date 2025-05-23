package by.lupach.questionnaireportal.services;

import by.lupach.questionnaireportal.models.User;
import by.lupach.questionnaireportal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public User save(User user) {
        return userRepository.save(user);
    }


    public User create(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }


    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }
}