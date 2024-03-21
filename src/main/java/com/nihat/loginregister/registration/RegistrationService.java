package com.nihat.loginregister.registration;

import com.nihat.loginregister.appuser.AppUser;
import com.nihat.loginregister.appuser.AppUserRole;
import com.nihat.loginregister.appuser.AppUserService;
import com.nihat.loginregister.email.EmailSender;
import com.nihat.loginregister.registration.token.ConfirmationToken;
import com.nihat.loginregister.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;

    @Transactional
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

        String confirmationLink = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
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

    public String buildEmail(String name, String link) {
        // Create a Thymeleaf context
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("link", link);

        // Process the template
        return templateEngine.process("email_template", context);
    }
}
