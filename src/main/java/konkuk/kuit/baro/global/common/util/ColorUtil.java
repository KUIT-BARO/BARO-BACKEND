package konkuk.kuit.baro.global.common.util;


import konkuk.kuit.baro.domain.promise.repository.PromiseMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ColorUtil {

    private static final Random RANDOM = new Random();
    private final PromiseMemberRepository promiseMemberRepository;

    public String generateRandomHexColor(Long promiseId) {
        List<String> existingColors = promiseMemberRepository.findColorsByPromiseId(promiseId);
        Set<String> existingColorSet = new HashSet<>(existingColors);

        String randomColor;

        do {
            randomColor = generateHex();
        } while (existingColorSet.contains(randomColor));

        return randomColor;
    }

    private String generateHex() {
        int r = 50 + RANDOM.nextInt(156); // 50 ~ 205 사이의 값
        int g = 50 + RANDOM.nextInt(156);
        int b = 50 + RANDOM.nextInt(156);

        return String.format("#%02X%02X%02X", r, g, b);
    }
}
