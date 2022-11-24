package cz.inqool.tennis_club_reservation_system.validator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Autowired
    private final PhoneNumberUtil phoneNumberUtil;

    public PhoneNumberValidator(PhoneNumberUtil phoneNumberUtil) {
        this.phoneNumberUtil = phoneNumberUtil;
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        try {
            var countryCode = Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name();
            var phone = phoneNumberUtil.parse(phoneNumber, countryCode);
            return phoneNumberUtil.isValidNumber(phone);
        } catch (NumberParseException e) {
            return false;
        }
    }

}
