package edu.yonsei.Studymate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/**") // 전체 경로에 적용
                .excludePathPatterns(   // 로그인 없이 허용할 경로
                        "/login", "/logout",
                        "/css/**", "/js/**", "/images/**", "/error"
                );
    }
}
