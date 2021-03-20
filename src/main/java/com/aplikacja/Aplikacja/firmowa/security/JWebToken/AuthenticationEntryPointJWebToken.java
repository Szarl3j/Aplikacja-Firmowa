package com.aplikacja.Aplikacja.firmowa.security.JWebToken;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;;

@Component
public class AuthenticationEntryPointJWebToken implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPointJWebToken.class);

    public void commence(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                         AuthenticationException authenticationException) throws IOException {
        logger.error("Unauthorized error {}", authenticationException.getMessage());
        servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }


}