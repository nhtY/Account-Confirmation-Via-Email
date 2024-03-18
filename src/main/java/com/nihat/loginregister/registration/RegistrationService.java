package com.nihat.loginregister.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.email());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        return "works";
    }
}
