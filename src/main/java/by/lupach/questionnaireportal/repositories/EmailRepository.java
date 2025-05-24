package by.lupach.questionnaireportal.repositories;

public interface EmailRepository {
    void sendRegistrationMessage(String email, String firstName);

    void sendPasswordSuccessfullyChangedMessage(String email, String firstName);

    void sendEmailSuccessfullyChangedMessage(String email, String firstName);
}
