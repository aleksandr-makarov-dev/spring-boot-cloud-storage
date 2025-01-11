package com.aleksandrmakarovdev.springbootcloudstorage.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.data.annotation.Reference;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface ValidUsername {
    String message() default "Invalid username";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
