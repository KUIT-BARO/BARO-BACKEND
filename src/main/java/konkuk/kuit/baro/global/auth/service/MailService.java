package konkuk.kuit.baro.global.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import konkuk.kuit.baro.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final JavaMailSender javaMailSender;

    public int createNumber() {
        return (int) (Math.random() * 90000) + 100000;   // 인증번호가 무조건 1로 시작함;
    }

    public MimeMessage createMail(String mail, int number) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("이메일 인증");

        String body = """
            <h3>요청하신 인증 번호입니다.</h3>
            <h1>%d</h1>
            <h3>감사합니다.</h3>
            """.formatted(number);

        message.setText(body, "UTF-8", "html");
        return message;
    }

    public int sendMail(String mail) {
        int number = createNumber();
        try {
            MimeMessage message = createMail(mail, number);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException(ErrorCode.MAIL_SEND_FAILED);
        }
        return number;
    }
}

