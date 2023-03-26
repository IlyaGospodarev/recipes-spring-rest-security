package recipes.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.entity.User;
import recipes.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> register(User user) {

        if (userRepository.findByEmailIgnoreCase(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        if (isValidRegistrationRequest(user)) {
            User createUser = new User(user.getEmail(), user.getPassword());
            createUser.setPassword(passwordEncoder.encode(createUser.getPassword()));
            userRepository.save(createUser);
        }
        return ResponseEntity.ok().build();
    }

    private boolean isValidRegistrationRequest(User user) {
        return user.getEmail() != null && user.getPassword() != null;
    }
}
