package cz.inqool.tennis_club_reservation_system.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String field;
    private String matchField;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        field = constraintAnnotation.field();
        matchField = constraintAnnotation.matchField();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        Object fieldValue;
        Object fieldMatchValue;

        try {
            fieldValue = BeanUtils.getProperty(value, field);
            fieldMatchValue = BeanUtils.getProperty(value, matchField);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            log.error("Error while validating FieldMatch('" + field + "', '" + matchField + "')", ex);
            return false;
        }

        if (fieldValue != null) {
            return fieldValue.equals(fieldMatchValue);
        } else {
            return fieldMatchValue == null;
        }
    }
}
