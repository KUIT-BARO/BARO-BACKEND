package konkuk.kuit.baro.global.common.config.security;

import jakarta.servlet.http.HttpServletRequest;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.auth.entrypoint.CustomAuthenticationEntryPoint;
import konkuk.kuit.baro.global.auth.jwt.filter.JwtAuthenticationFilter;
import konkuk.kuit.baro.global.auth.jwt.service.JwtService;
import konkuk.kuit.baro.global.common.redis.RedisService;
import konkuk.kuit.baro.global.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import konkuk.kuit.baro.global.auth.jwt.filter.ExceptionHandlerFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(headersConfigure -> headersConfigure
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**", "/actuator/**", "/swagger-ui/**",
                                "/v3/api-docs/**", "/users/signup/**", "/auth/login/**",
                                "/auth/reissue/**").permitAll()
                        .requestMatchers("/**").authenticated())
                .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(customAuthenticationEntryPoint()))
                .addFilterAfter(jwtAuthenticationFilter(), LogoutFilter.class)
                .addFilterBefore(exceptionHandlerFilter(), JwtAuthenticationFilter.class);
        // 필터 순서: Logout filter -> jwtAuthenticationFilter
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, redisService, jwtUtil);
    }

    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter() {
        return new ExceptionHandlerFilter();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
