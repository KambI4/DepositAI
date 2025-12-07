package com.coursework.deposit;

import com.coursework.deposit.model.DepositEntity;
import com.coursework.deposit.model.DepositType;
import com.coursework.deposit.model.Role;
import com.coursework.deposit.model.UserEntity;
import com.coursework.deposit.repository.DepositRepository;
import com.coursework.deposit.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Инициализация тестовых данных: пользователи и несколько депозитов.
     */
    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      DepositRepository depositRepository,
                                      PasswordEncoder encoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                UserEntity admin = new UserEntity();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);
            }

            if (!userRepository.existsByUsername("user")) {
                UserEntity user = new UserEntity();
                user.setUsername("user");
                user.setPassword(encoder.encode("user123"));
                user.setRole(Role.USER);
                user.setEnabled(true);
                userRepository.save(user);
            }

            if (depositRepository.count() == 0) {
                depositRepository.save(new DepositEntity("Kaspi Bank", 12.0, 100000, 12, DepositType.SIMPLE));
                depositRepository.save(new DepositEntity("Halyk Bank", 13.5, 150000, 24, DepositType.COMPOUND));
                depositRepository.save(new DepositEntity("Jusan Bank", 11.0, 50000, 6, DepositType.PROGRESSIVE));
            }
        };
    }
}
