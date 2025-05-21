package edu.yonsei.Studymate.login;

import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LoginDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public LoginDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        saveUserIfNotExists("admin", "1234");
        saveUserIfNotExists("testuser", "pass123");
    }

    private void saveUserIfNotExists(String loginId, String password) {
        userRepository.findByLoginId(loginId)
                .ifPresentOrElse(
                        user -> System.out.println("User '" + loginId + "' already exists. Skipping..."),
                        () -> userRepository.save(new User(loginId, password))
                );
    }
}
