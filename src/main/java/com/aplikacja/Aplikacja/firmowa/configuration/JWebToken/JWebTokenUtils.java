package com.aplikacja.Aplikacja.firmowa.configuration.JWebToken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JWebTokenUtils {

private static final Logger logger= LoggerFactory.getLogger(JWebTokenUtils.class);

@Value("${aplikacja-firmowa.app.JwtSecret}")
private String jwtSecret;

@Value("${aplikacja-firmowa.app.jwtExpirationMs")
    private String jwtExpirationMs;

public String generateJWebToken(Authentication authentication){
return generateJWebToken(authentication);
}

}
