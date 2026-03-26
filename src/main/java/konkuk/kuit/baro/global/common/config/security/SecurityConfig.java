package konkuk.kuit.baro.global.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.kuit.baro.global.auth.entrypoint.CustomAuthenticationEntryPoint;
import konkuk.kuit.baro.global.auth.jwt.filter.ExceptionHandlerFilter;
import konkuk.kuit.baro.global.auth.jwt.filter.JwtAuthenticationFilter;
import konkuk.kuit.baro.global.auth.jwt.service.JwtService;
import konkuk.kuit.baro.global.auth.security.login.CustomLoginFailureHandler;
import konkuk.kuit.baro.global.auth.security.login.CustomLoginFilter;
import konkuk.kuit.baro.global.auth.security.login.CustomLoginSuccessHandler;
import konkuk.kuit.baro.global.auth.security.util.CookieUtil;
import konkuk.kuit.baro.global.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final CookieUtil cookieUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    private final String[] PERMITTED_URLS = {
            "/h2-console/**", "/actuator/**", "/swagger-ui/**",
            "/v3/api-docs/**", "/users/signup/**", "/auth/login/**",
            "/auth/mail/**", "/auth/reissue/**", "/auth/logout/**"
    };

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

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
//                        .requestMatchers(PERMITTED_URLS).permitAll()
//                        .requestMatchers("/**").authenticated())
                          .anyRequest().permitAll())
                .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(customAuthenticationEntryPoint()))
                .addFilterAt(customLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter(), CustomLoginFilter.class)
                .addFilterBefore(exceptionHandlerFilter(), JwtAuthenticationFilter.class);
        // 필터 순서: ExceptionHandlerFilter -> CustomLoginFilter -> JwtAuthenticationFilter
        return http.build();
    }

    @Bean
    public CustomLoginFilter customLoginFilter() throws Exception {
        return new CustomLoginFilter(
                authenticationManager(),
                objectMapper,
                customLoginSuccessHandler(),
                customLoginFailureHandler()
        );
    }

    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler(jwtUtil, jwtService, cookieUtil, objectMapper);
    }

    @Bean
    public CustomLoginFailureHandler customLoginFailureHandler() {
        return new CustomLoginFailureHandler(objectMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, jwtUtil);
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
