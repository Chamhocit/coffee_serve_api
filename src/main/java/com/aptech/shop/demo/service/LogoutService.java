package com.aptech.shop.demo.service;

import com.aptech.shop.demo.Response.SetMessageResponse;
import com.aptech.shop.demo.repository.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SetMessageResponse setMessageResponse = new SetMessageResponse();
        Cookie[] cookies = request.getCookies();
        String cookieName = "jwt";
        String token = null;
        Cookie jwtCookie = null;
        if(cookies!=null){
            jwtCookie = Arrays.stream(cookies)
                    .filter(x->x.getName().equals(cookieName))
                    .findFirst().orElse(null);
        }

        if(cookies==null || jwtCookie==null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            setMessageResponse.SetMessage(response, "Logout failed: JWT cookie not found");
        }else {
            token= jwtCookie.getValue();
            var storedToken = tokenRepository.findByToken(token).orElseThrow(null);
            if(storedToken!=null){
                storedToken.setRevoked(true);
                storedToken.setExpired(true);
                tokenRepository.save(storedToken);
                SecurityContextHolder.clearContext();
                jwtCookie = new Cookie("jwt", null);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(0);
                jwtCookie.setDomain("localhost");
                response.addCookie(jwtCookie);
                response.setStatus(HttpServletResponse.SC_OK);
                setMessageResponse.SetMessage(response, "User Logout Success.");
            }else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                setMessageResponse.SetMessage(response, "Logout failed: Token not found");
            }
        }
    }
}
