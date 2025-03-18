package konkuk.kuit.baro.global.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.domain.user.service.UserService;
import konkuk.kuit.baro.global.auth.dto.request.MailRequestDTO;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Duration;
import java.util.Random;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MailService {

    private static final long VERIFICATION_CODE_EXPIRY_MINUTES = 5;

    private static final long VERIFIED_TTL_SECONDS = 1800; // 30분

    private static final String EMAIL_KEY_PREFIX = "auth:email:";

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    private final UserRepository userRepository;

    private final RedisService redisService;

    public void sendMail(MailRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String authCode = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(request.getEmail()); // 메일 수신자
            mimeMessageHelper.setSubject("[BARO] 이메일 인증을 위한 인증 코드 발송"); // 메일 제목
            mimeMessageHelper.setText(setContext(authCode), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            String key = EMAIL_KEY_PREFIX + request.getEmail();

            // Redis에 인증번호와 만료시간 저장
            redisService.setValues(key, authCode, Duration.ofMinutes(VERIFICATION_CODE_EXPIRY_MINUTES));

            log.info("Success: sendMail > Key={}, Saved Value in Redis={}", key, redisService.getValues(key));

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

    public void checkVerificationNumber(CodeCheckRequestDTO request, Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        String key = EMAIL_KEY_PREFIX + currentUser.getEmail();
        String storedCode = redisService.getValues(key);

        if (storedCode == null) {
            throw new CustomException(ErrorCode.EXPIRED_EMAIL_CODE);
        }

        // 인증 번호가 이미 인증된 상태인 경우 그냥 리턴
        if ("VERIFIED".equals(storedCode)){return;};

        // 입력 코드와 Redis 코드가 다르면 에러
        if (!String.valueOf(request.getAuthCode()).equals(storedCode)) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_CODE);
        }
        // 인증 성공: 값 변경 + TTL 재설정
        redisService.setValues(key, "VERIFIED", Duration.ofSeconds(VERIFIED_TTL_SECONDS));
    }

    // thymeleaf를 통한 html 적용
    public String setContext(String authCode) {
        Context context = new Context();
        context.setVariable("code", authCode);
        return templateEngine.process("email.html", context);
    }
}

