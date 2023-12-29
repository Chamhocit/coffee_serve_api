package com.aptech.shop.demo.config;

import com.aptech.shop.demo.entity.Role;
import com.aptech.shop.demo.repository.TokenRepository;
import com.aptech.shop.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().contains("/api/auth")){
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        Cookie jwtCookie = null;
        final String cookieName = "jwt";
        if(cookies!=null){
            jwtCookie = Arrays.stream(cookies)
                    .filter(x->x.getName().equals(cookieName))
                    .findFirst().orElse(null);
        }

        if(cookies==null || jwtCookie==null){
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtCookie.getValue();

        String userEmail = jwtService.extractUsername(token);

        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            var isTokenValid = tokenRepository.findByToken(token)
                    .map(x->!x.isExpired() && !x.isRevoked())
                    .orElse(false);
            if(jwtService.isTokenValidate(token, userDetails) && isTokenValid){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }


}
