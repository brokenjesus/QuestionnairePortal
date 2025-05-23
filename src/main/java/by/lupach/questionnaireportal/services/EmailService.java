package by.lupach.questionnaireportal.services;

import by.lupach.questionnaireportal.repositories.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailRepository {
    private final JavaMailSender mailSender; // Let Spring inject the configured instance

    public void sendRegistrationMessage(String email, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Welcome to Questionnaire Portal");
        message.setText(String.format("Dear %s,\n\nThank you for registering in our Questionnaire Portal!\n\nBest regards,\nQuestionnaire Portal Team", firstName));

        mailSender.send(message);
    }

    public void sendVerificationEmail(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Verification");
        message.setText(String.format("Your verification code is: %s\n\nThis code will expire in 15 minutes.", verificationCode));
        mailSender.send(message);
    }

    public void sendPasswordSuccessfullyChangedMessage(String email, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password changed Questionnaire Portal");
        message.setText(String.format("Dear %s,\n\nYou have successfully changed your password in our Questionnaire Portal!\n\nBest regards,\nQuestionnaire Portal Team", firstName));

        mailSender.send(message);
    }

    @Override
    public void sendEmailSuccessfullyChangedMessage(String email, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password changed Questionnaire Portal");
        message.setText(String.format("Dear %s,\n\nYou have successfully changed your email in our Questionnaire Portal!\n\nBest regards,\nQuestionnaire Portal Team", firstName));

        mailSender.send(message);
    }
}