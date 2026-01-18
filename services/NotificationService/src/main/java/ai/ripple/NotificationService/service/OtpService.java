package ai.ripple.NotificationService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;

    private static final long OTP_TTL_MINUTES = 5; 

    public Mono<Void> generateOtp(String email, String purpose) {
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        String key = "OTP:" + purpose + ":" + email;

        return redisTemplate.opsForValue()
                .set(key, otp, Duration.ofMinutes(OTP_TTL_MINUTES))
                .then(Mono.fromRunnable(() -> emailService.sendOtp(email, otp, purpose)));
    }


    public Mono<Boolean> verifyOtp(String email, String purpose, String otp) {
        String key = "OTP:" + purpose + ":" + email;

        return redisTemplate.opsForValue()
                .get(key)
                .flatMap(storedOtp -> {
                    if (!storedOtp.equals(otp)) {
                        return Mono.error(new RuntimeException("Invalid OTP"));
                    }
                    // OTP is valid → delete from Redis (one-time use)
                    return redisTemplate.delete(key).thenReturn(true);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("OTP expired or not found")));
    }
}
