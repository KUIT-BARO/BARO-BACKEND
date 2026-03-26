package konkuk.kuit.baro.global.auth.security.login;

import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다: " + email));

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
