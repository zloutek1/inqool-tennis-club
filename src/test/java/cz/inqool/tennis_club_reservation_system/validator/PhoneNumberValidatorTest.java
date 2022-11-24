package cz.inqool.tennis_club_reservation_system.validator;

import org.hibernate.validator.internal.util.annotation.AnnotationDescriptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PhoneNumberValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "2055550125",
            "202 555 0125",
            "(202) 555-0125",
            "+111 (202) 555-0125",
            "636 856 789",
            "+111 636 856 789",
            "636 85 67 89",
            "+111 636 85 67 89"
    })
    public void isValid(String phoneNumber) {
        var validator = new PhoneNumberValidator();
        var constraintAnnotation = new AnnotationDescriptor.Builder<>(PhoneNumber.class).build().getAnnotation();
        validator.initialize(constraintAnnotation);

        assertThat(validator.isValid(phoneNumber, null)).isTrue();
    }

}
