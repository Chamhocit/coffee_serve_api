package com.aptech.shop.demo.service;

import com.aptech.shop.demo.Response.CheckUserResponse;
import com.aptech.shop.demo.Response.LoginResponse;
import com.aptech.shop.demo.Response.RegisterResponse;
import com.aptech.shop.demo.Response.SetMessageResponse;
import com.aptech.shop.demo.entity.Role;
import com.aptech.shop.demo.entity.Token;
import com.aptech.shop.demo.entity.User;
import com.aptech.shop.demo.repository.RoleRepository;
import com.aptech.shop.demo.repository.TokenRepository;
import com.aptech.shop.demo.repository.UserRepository;
import com.aptech.shop.demo.request.LoginRequest;
import com.aptech.shop.demo.request.RegisterRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final UserDetailsService userDetailsService;
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserToken(User user){
        var validUserToken = tokenRepository.findAllValidByUser(user.getId());
        if(validUserToken.isEmpty()) return;
        validUserToken.forEach(x->{
            x.setExpired(true);
            x.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

    public RegisterResponse register(RegisterRequest request){
        List<Role> roleList = roleRepository.findByNameIn(request.getRoles());
        Set<Role> roleSet = roleList.stream().collect(Collectors.toSet());
        roleSet.stream().forEach(x->System.out.println(x));
        var user = User.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roleSet)
                .build();
        User saveUser = userRepository.save(user);
        return RegisterResponse.builder()
                .email(saveUser.getEmail())
                .phone(saveUser.getPhone())
                .name(saveUser.getName()).build();
    }

    public LoginResponse login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) user.getAuthorities();
        authorities.forEach(x->System.out.println(x));
        revokeAllUserToken(user);
        saveUserToken(user, jwtToken);
        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .name(user.getName())
                .build();
    }

    public CheckUserResponse CheckUser(HttpServletRequest request){
//        SetMessageResponse setMessageResponse = new SetMessageResponse();
        Cookie[] cookies = request.getCookies();
        Cookie jwtCookie = null;
        final String cookieName = "jwt";
        String token = null;
        if(cookies!=null){
            jwtCookie = Arrays.stream(cookies)
                    .filter(x->x.getName().equals(cookieName))
                    .findFirst().orElse(null);
        }

        if(cookies==null || jwtCookie==null ){
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            setMessageResponse.SetMessage(response, "The user is not logged in.");
            return null;

        }else {
            token = jwtCookie.getValue();
            if(jwtService.isTokenExpired(token)){
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                setMessageResponse.SetMessage(response, "The user is not logged in.");
                return null;
            }else {
                String userEmail = jwtService.extractUsername(token);
                User user = (User) userDetailsService.loadUserByUsername(userEmail);
                Set<Role> roles = user.getRoles();
                List<String> rolesNames = roles.stream().map(Role::getName).collect(Collectors.toList());
                CheckUserResponse checkUserResponse = CheckUserResponse
                        .builder().status(true).name(user.getName()).role(rolesNames).build();
                return checkUserResponse;
            }
        }
    }

}
