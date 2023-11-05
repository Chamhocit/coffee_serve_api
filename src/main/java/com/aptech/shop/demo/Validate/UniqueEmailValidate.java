package com.aptech.shop.demo.Validate;

import com.aptech.shop.demo.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
@RequiredArgsConstructor
public class UniqueEmailValidate implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && !userRepository.existsUserByEmail(email);
    }
}
