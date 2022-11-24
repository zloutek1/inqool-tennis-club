package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.CourtCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.CourtDto;
import cz.inqool.tennis_club_reservation_system.dto.ReservationDto;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import cz.inqool.tennis_club_reservation_system.model.Court;
import cz.inqool.tennis_club_reservation_system.repository.CourtRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CourtService extends CrudService<Court, Long, CourtDto, CourtCreateDto, CourtDto> {

    private final CourtRepository courtRepository;

    public CourtService(CourtRepository courtRepository, BeanMappingService beanMappingService) {
        super(courtRepository, beanMappingService, Court.class, CourtDto.class);
        this.courtRepository = courtRepository;
    }

    public List<ReservationDto> findReservations(int courtNumber) {
        var court = tryToFindByCourtNumber(courtNumber);
        var reservations = court.getReservations();
        return beanMappingService.mapTo(reservations, ReservationDto.class);
    }

    private Court tryToFindByCourtNumber(int courtNumber) {
        return courtRepository.findByCourtNumber(courtNumber)
                .orElseThrow(() -> new NotFoundException("Court with number " + courtNumber + " not found"));
    }
}
