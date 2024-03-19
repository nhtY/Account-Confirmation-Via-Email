package com.nihat.loginregister.registration;

import com.nihat.loginregister.appuser.AppUser;
import com.nihat.loginregister.appuser.AppUserRole;
import com.nihat.loginregister.appuser.AppUserService;
import com.nihat.loginregister.email.EmailSender;
import com.nihat.loginregister.registration.token.ConfirmationToken;
import com.nihat.loginregister.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.email());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        String token = appUserService.signUpUser(
                AppUser.builder()
                        .firstName(request.firstName())
                        .lastName(request.lastname())
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .appUserRole(AppUserRole.USER)
                        .build()
        );

        String confirmationLink = "http://localhost:8080/api/v1/confirm?token=" + token;
        emailSender.send(
                request.email(),
                buildEmail(request.firstName(), confirmationLink));
        return token;

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

    private String buildEmail(String name, String link) {
        try {
            // Read the email template from the resources directory
            ClassPathResource resource = new ClassPathResource("email-template.html");
            Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            String template = FileCopyUtils.copyToString(reader);

            // Replace placeholders with actual values
            template = template.replace("{name}", name);
            template = template.replace("{link}", link);

            return template;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read email template", e);
        }
    }
}
