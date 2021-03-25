package com.aplikacja.Aplikacja.firmowa.security.JWebToken;

import com.aplikacja.Aplikacja.firmowa.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.*;

import java.util.Date;


@Component
public class JWebTokenUtils {

    private static final Logger logger = LoggerFactory.getLogger(JWebTokenUtils.class);

 //   @Value("${Aplikacja-firmowa.app.JwtSecret}")
    @Value("${aplikacja_dla_firm.app.jwtSecret}")
    private String jwtSecret;

//   @Value("${Aplikacja-firmowa.app.jwtExpirationMs")
    @Value("{aplikacja_dla_firm.app.jwtExpirationMs}")
    private String jwtExpirationMs;

    public String generateJWebToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserLoginFromJWebToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJWebToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Incorrect JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Incorrect JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token time is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is not supported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
