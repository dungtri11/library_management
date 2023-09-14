package com.example.demo.filter;

import com.example.demo.entity.User;
import com.example.demo.jwt.Header;
import com.example.demo.jwt.Jwt;
import com.example.demo.jwt.Payload;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.ObjectHandler;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Order(1)
public class ValidJwtFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(ValidJwtFilter.class);
    @Autowired
    private UserRepository userRepository;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            List<Cookie> cookieList = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("credential")).collect(Collectors.toList());
            token = (cookieList.size() > 0 ? cookieList.get(0).getValue() : null);
        }
        logger.info(token);
        if (token == null) {
            filterChain.doFilter(request, response);
        } else {
            Jwt jwt = new Jwt();
            jwt.setSpecification(token);
            Map<String, String> payloadMap = ObjectHandler.parseObjectString(jwt.getPayloadAsString());
            String username = payloadMap.get("username");
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                Payload payload = new Payload(username, user.getAuthority());
                jwt = new Jwt(new Header(), payload, "MySecret");
                if (jwt.getSpecification().equals(token)) {
                    filterChain.doFilter(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("Token is not match");
                    logger.info("Response Status: {}", HttpStatus.CONFLICT);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Couldn't find user");
                logger.info("Response Status: {}", HttpStatus.BAD_REQUEST);
            }
        }
    }
}
