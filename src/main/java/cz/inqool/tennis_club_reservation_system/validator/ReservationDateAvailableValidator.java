package cz.inqool.tennis_club_reservation_system.validator;

import cz.inqool.tennis_club_reservation_system.dto.CourtDto;
import cz.inqool.tennis_club_reservation_system.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

@Slf4j
public class ReservationDateAvailableValidator implements ConstraintValidator<ReservationDateAvailable, Object> {

    private String courtField;
    private String fromDateField;
    private String toDateField;

    private final ReservationRepository reservationRepository;

    public ReservationDateAvailableValidator(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void initialize(final ReservationDateAvailable constraintAnnotation) {
        courtField = constraintAnnotation.courtField();
        fromDateField = constraintAnnotation.fromDateField();
        toDateField = constraintAnnotation.toDateField();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        CourtDto court;
        LocalDateTime fromDate;
        LocalDateTime toDate;

        try {
            court = (CourtDto) PropertyUtils.getProperty(value, courtField);
            fromDate = (LocalDateTime) PropertyUtils.getProperty(value, fromDateField);
            toDate = (LocalDateTime) PropertyUtils.getProperty(value, toDateField);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException ex) {
            log.error("Error while validating ReservationDateAvailableValidator('" + courtField + "', '" + fromDateField + "', '" + toDateField + "')", ex);
            return false;
        }

        if (court == null || fromDate == null || toDate == null) {
            return false;
        }

        log.info("validating registration on court {} between {}-{}", court.getNumber(), fromDate, toDate);
        return reservationRepository.isDateRangeAvailable(court.getNumber(), fromDate, toDate);
    }
}
