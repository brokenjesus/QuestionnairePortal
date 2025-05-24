package by.lupach.questionnaireportal.services;

import by.lupach.questionnaireportal.exceptions.InvalidVerificationCodeException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationCodeService {
    private final Map<String, String> emailToCodeMap = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> codeExpiryMap = new ConcurrentHashMap<>();

    public String generateVerificationCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        emailToCodeMap.put(email, code);
        codeExpiryMap.put(email, LocalDateTime.now().plusMinutes(15)); // 15 minutes expiry
        return code;
    }

    public boolean validateCode(String email, String code) {
        String storedCode = emailToCodeMap.get(email);
        LocalDateTime expiryTime = codeExpiryMap.get(email);

        if (storedCode == null || expiryTime == null) {
            throw new InvalidVerificationCodeException("Invalid verification code");
        }

        if (LocalDateTime.now().isAfter(expiryTime)) {
            emailToCodeMap.remove(email);
            codeExpiryMap.remove(email);
            throw new InvalidVerificationCodeException("Verification code expired");
        }

        return storedCode.equals(code);
    }
}