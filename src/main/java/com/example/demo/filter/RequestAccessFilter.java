package com.example.demo.filter;


import com.example.demo.annotation.Authorities;
import com.example.demo.common.Authority;
import com.example.demo.jwt.Jwt;
import com.example.demo.utils.AnnotationHandler;
import com.example.demo.utils.ListHandler;
import com.example.demo.utils.ObjectHandler;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Order(1)
public class RequestAccessFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(RequestAccessFilter.class);
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
        String authority = Authority.PUBLIC.toString();
        if (token != null) {
            Jwt jwt = new Jwt();
            jwt.setSpecification(token);
            Map<String, String> payload = ObjectHandler.parseObjectString(jwt.getPayloadAsString());
            authority = payload.get("authority");
            request.getSession().setAttribute("username", payload.get("username"));
        } else {
            request.getSession().setAttribute("authorized", true);
        }
        authority = authority.toUpperCase();
        Method api = AnnotationHandler.getApiWithURI(request.getRequestURI());
        if (api.isAnnotationPresent(Authorities.class)) {
            Authorities authorities = api.getAnnotation(Authorities.class);
            if (ListHandler.contains(Arrays.asList(authorities.value()), Enum.valueOf(Authority.class, authority))) {
                filterChain.doFilter(request, response);
            } else {
                if (!authority.equals("PUBLIC")) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Unauthorized user");
                    logger.info("Response Status: {}", HttpStatus.FORBIDDEN);
                } else {
                    response.sendRedirect("http://localhost:8080/login");
                    logger.info("Response Status: {}", HttpStatus.UNAUTHORIZED);
                }

            }
        }

    }


}
