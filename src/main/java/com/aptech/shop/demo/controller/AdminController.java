package com.aptech.shop.demo.controller;

import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/getInfo")
    public ResponseEntity<?> getInfo(){
        return ResponseEntity.ok("fdsjvbnjidhfvb");
    }

}
