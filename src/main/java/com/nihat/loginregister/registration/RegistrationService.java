package com.nihat.loginregister.registration;

import com.nihat.loginregister.appuser.AppUser;
import com.nihat.loginregister.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final PasswordEncoder passwordEncoder;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.email());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        return appUserService.signUpUser(
                AppUser.builder()
                        .firstName(request.firstName())
                        .lastName(request.lastname())
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .enabled(true)
                        .locked(false)
                        .build()
        );
    }
}
