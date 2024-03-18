package com.nihat.loginregister.registration.token;

import com.nihat.loginregister.appuser.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final static Integer VALID_TOKEN_DURATION_IN_MINUTE = 15;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public ConfirmationToken generateToken(AppUser user) {
        String token = UUID.randomUUID().toString();
        return ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(VALID_TOKEN_DURATION_IN_MINUTE))
                .confirmedAt(null)
                .appUser(user)
                .build();
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return Optional.of(confirmationTokenRepository.findByToken(token))
                .orElse(null);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now()
        );
    }
}
