package edu.yonsei.Studymate.common;

import edu.yonsei.Studymate.login.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/study-mate/auth/**",
                                "/api/study-mate/auth/**",
                                "/api/study-mate/studygroups/**",
                                "/api/study-mate/mate/**",  // 메이트 관련 API 추가
                                "/api/study-mate/subjects/**",  // 과목 관련 API 추가
                                "/",
                                "/study-mate/main",
                                "/find-id",
                                "/find-password",
                                "/error"

                        ).permitAll()
                        .requestMatchers("/study-mate/search-page").authenticated()  // 이 부분 수정
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/study-mate/auth/login")
                        .loginProcessingUrl("/study-mate/auth/login")
                        .defaultSuccessUrl("/study-mate/main", true)
                        .failureUrl("/study-mate/auth/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/study-mate/auth/logout")
                        .logoutSuccessUrl("/study-mate/auth/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return auth.build();
    }
}