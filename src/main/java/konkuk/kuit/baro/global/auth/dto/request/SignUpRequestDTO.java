package konkuk.kuit.baro.global.auth.dto.request;


import lombok.Getter;

@Getter
public class SignUpRequestDTO{
    String email;
    String password;
    String name;
}
