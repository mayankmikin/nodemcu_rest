package com.dominion.nodemcu.jwtsecurity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/*this whole class has been created as a result of this tutorial
https://coder-coacher.github.io/Tech-Primers/Spring-Security-using-JWT-in-Spring-Boot-App-Tech-Primers--HYrUs1ZCLI.html
https://github.com/TechPrimers/jwt-security-example
*/

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
    }
}