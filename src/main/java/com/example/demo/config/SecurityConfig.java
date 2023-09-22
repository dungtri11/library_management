package com.example.demo.config;

import com.example.demo.common.Authority;
import com.example.demo.exception.JwtAuthenticationEntryPoint;
import com.example.demo.filter.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private AuthenticationEntryPoint authenticationEntryPoint;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> registerJwtFilter(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);

        return registrationBean;
    }

    @Bean
    public SecurityFilterChain authenticationFilterChain(HttpSecurity http) throws Exception{
        http
                .securityMatcher(AntPathRequestMatcher.antMatcher("/auth/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(handler -> handler.authenticationEntryPoint(authenticationEntryPoint))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/login")).anonymous()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/register")).anonymous()
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public SecurityFilterChain bookFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(AntPathRequestMatcher.antMatcher("/books/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/add")).hasAuthority(Authority.LIBRARIAN.toString())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT,"/edit")).hasAuthority(Authority.LIBRARIAN.toString())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE,"/delete/**")).hasAuthority(Authority.LIBRARIAN.toString())
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class);
        return http.build();
    }

    @Bean
    public SecurityFilterChain borrowDetailFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(AntPathRequestMatcher.antMatcher("/borrow-detail/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/reader-action-details")).hasAuthority(Authority.LIBRARIAN.toString())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT,"/borrow")).hasAuthority(Authority.READER.toString())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT,"/return")).hasAuthority(Authority.READER.toString())
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class);
        return http.build();
    }

    @Bean
    public SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(AntPathRequestMatcher.antMatcher("/user/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/switch-authority")).hasAuthority(Authority.LIBRARIAN.toString())
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
