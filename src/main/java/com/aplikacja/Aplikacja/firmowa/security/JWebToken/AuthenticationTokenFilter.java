package com.aplikacja.Aplikacja.firmowa.security.JWebToken;

import com.aplikacja.Aplikacja.firmowa.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Configuration
// adnotacja configuration tworzy Ci beana a jednoczesnie masz beana tworzacego authenticationTokenFilter
// w klasie SecurityConfig, wiec albo tu dajesz @Configuration albo tam tworzysz @Bean
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JWebTokenUtils jWebTokenUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse
            servletResponse, FilterChain fChain) throws ServletException, IOException {
        try {
            String jWebToken = parasejWebToken(servletRequest);
            if (jWebToken != null && jWebTokenUtils.validateJWebToken(jWebToken)) {
                String userLogin = jWebTokenUtils.getUserLoginFromJWebToken(jWebToken);
                UserDetails uDetails = userDetailsServiceImpl.loadUserByUsername(userLogin);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        uDetails, null, uDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(servletRequest));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot reach user authentication {}", e);
        }
        fChain.doFilter(servletRequest,servletResponse);
    }
    private String parasejWebToken(HttpServletRequest servletRequest){
        String mainAuth= servletRequest.getHeader("Authorization");
        if (StringUtils.hasText(mainAuth) && mainAuth.startsWith("Bearer")){
            return mainAuth.substring(7,mainAuth.length());
        }
        return null;
    }


}
