package cz.inqool.tennis_club_reservation_system.validator;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.hibernate.validator.internal.util.annotation.AnnotationDescriptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PhoneNumberValidatorTest {

    @Autowired
    private PhoneNumberUtil phoneNumberUtil;

    @ParameterizedTest
    @ValueSource(strings = {
            "+919367788755",
            "+16308520397",
            "+91 9367 788 755",
            "+16 308 520 397",
            "+421 910 100 001"
    })
    public void isValid(String phoneNumber) {
        var validator = new PhoneNumberValidator(phoneNumberUtil);
        var constraintAnnotation = new AnnotationDescriptor.Builder<>(PhoneNumber.class).build().getAnnotation();
        validator.initialize(constraintAnnotation);

        assertThat(validator.isValid(phoneNumber, null)).isTrue();
    }

}
