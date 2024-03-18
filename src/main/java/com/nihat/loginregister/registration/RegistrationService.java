package com.nihat.loginregister.registration;

import com.nihat.loginregister.appuser.AppUser;
import com.nihat.loginregister.appuser.AppUserRole;
import com.nihat.loginregister.appuser.AppUserService;
import com.nihat.loginregister.registration.token.ConfirmationToken;
import com.nihat.loginregister.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final PasswordEncoder passwordEncoder;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.email());
        if (!isValidEmail) {
            // throw new IllegalStateException("email not valid");
        }
        return appUserService.signUpUser(
                AppUser.builder()
                        .firstName(request.firstName())
                        .lastName(request.lastname())
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .appUserRole(AppUserRole.USER)
                        .build()
        );
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow( () ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);

        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail()
        );

        return "confirmed";

    }
}
