package com.aleksandrmakarov.springbootcloudstorage.users.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.annotation.*;
import java.util.stream.Stream;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface FieldMatch {
    String[] value() default {};

    String message() default "{passwordsMatch.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class PasswordsMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String[] fields;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        fields = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        if (fields == null || fields.length == 0) {
            return true;
        }

        return Stream.of(fields)
                .map(f -> PARSER.parseExpression(f).getValue(o, String.class))
                .distinct()
                .count() == 1;
    }
}