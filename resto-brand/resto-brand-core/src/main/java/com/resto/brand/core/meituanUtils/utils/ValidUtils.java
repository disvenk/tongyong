package com.resto.brand.core.meituanUtils.utils;

import com.resto.brand.core.meituanUtils.request.CipCaterRequest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * 用于校验
 * <p>
 * Created by cuibaosen on 16/11/17.
 */
public final class ValidUtils {
    private ValidUtils() {
    }

    public static <T extends CipCaterRequest> String valid(T t) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (violations != null && violations.size() > 0) {
            return violations.iterator().next().getMessage();
        }
        return null;
    }
}
