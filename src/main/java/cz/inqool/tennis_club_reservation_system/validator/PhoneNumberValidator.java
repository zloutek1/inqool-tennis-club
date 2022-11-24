package cz.inqool.tennis_club_reservation_system.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private static final String PATTERNS
            = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3} ?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3} ?)(\\d{2} ?){2}\\d{2}$";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return phoneNumber.matches(PATTERNS);
    }

}
