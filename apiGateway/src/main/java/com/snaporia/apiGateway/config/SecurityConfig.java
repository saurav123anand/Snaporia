package com.snaporia.apiGateway.config;

import com.snaporia.common_security.exception.SecurityExceptionHandler;
import com.snaporia.common_security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http,
                                            SecurityExceptionHandler securityExceptionHandler) throws Exception {
         http.csrf(AbstractHttpConfigurer::disable)
                 .formLogin(AbstractHttpConfigurer::disable)
                 .httpBasic(AbstractHttpConfigurer::disable)
                 .sessionManagement(session->
                         session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authorizeHttpRequests( auth->auth
                         .requestMatchers("/api/v1/auth/public/**").permitAll()
                         .requestMatchers("/actuator/health").permitAll()
                         .anyRequest().authenticated()
                 )
                 .exceptionHandling(exception->exception
                         .authenticationEntryPoint(securityExceptionHandler)
                         .accessDeniedHandler(securityExceptionHandler)

                 )
                 .addFilterBefore(
                         jwtFilter,
                         UsernamePasswordAuthenticationFilter.class
                 );
         return http.build();
    }
    @Bean
    SecurityExceptionHandler securityExceptionHandler() {
        return new SecurityExceptionHandler();
    }
}
