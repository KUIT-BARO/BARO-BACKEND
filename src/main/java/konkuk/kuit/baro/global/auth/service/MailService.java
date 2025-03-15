package konkuk.kuit.baro.global.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import konkuk.kuit.baro.domain.user.service.UserService;
import konkuk.kuit.baro.global.auth.dto.request.MailRequestDTO;
import konkuk.kuit.baro.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    private final UserService userService;

    public void sendMail(MailRequestDTO request) {
        String authCode = createCode();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(senderEmail); // 메일 수신자
            mimeMessageHelper.setSubject("[BARO] 이메일 인증을 위한 인증 코드 발송"); // 메일 제목
            mimeMessageHelper.setText(setContext(authCode), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("Success");

        } catch (MessagingException e) {
            log.info("fail");
            throw new CustomException(ErrorCode.MAIL_SEND_FAILED);
        }
    }

    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    // thymeleaf를 통한 html 적용
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("email.html", context);
    }
}

