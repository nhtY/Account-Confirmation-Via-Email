package com.nihat.loginregister.bootstrap;

import com.nihat.loginregister.appuser.AppUser;
import com.nihat.loginregister.appuser.AppUserRepository;
import com.nihat.loginregister.appuser.AppUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner run() {
        return args -> {
            AppUser user = AppUser.builder()
                    .firstName("name")
                    .lastName("surname")
                    .email("email")
                    .appUserRole(AppUserRole.USER)
                    .enabled(true)
                    .locked(false)
                    .password(passwordEncoder.encode("pass"))
                    .build();

            appUserRepository.save(user);
        };
    }
}
