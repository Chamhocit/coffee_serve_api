package com.aptech.shop.demo.controller;

import com.aptech.shop.demo.Response.LoginResponse;
import com.aptech.shop.demo.request.LoginRequest;
import com.aptech.shop.demo.request.RegisterRequest;
import com.aptech.shop.demo.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody @Valid RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest request, HttpServletResponse response){
        LoginResponse loginResponse = authenticationService.login(request);
        //add cookie jwt http-only
        String jwtToken = loginResponse.getAccessToken();
        Cookie jwtCookie = new Cookie("jwt", jwtToken);
        jwtCookie.setDomain("localhost");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(7*24*60*60);

        //thêm cookie userMail
        Cookie userEmailCookie = new Cookie("userName", request.getEmail());
        userEmailCookie.setMaxAge(7*24*60*60);
        userEmailCookie.setPath("/");
        userEmailCookie.setSecure(true);
        userEmailCookie.setDomain("localhost");
        // add 2 cookie vào response
        response.addCookie(userEmailCookie);
        response.addCookie(jwtCookie);
        return ResponseEntity.ok(loginResponse);
    }
}
