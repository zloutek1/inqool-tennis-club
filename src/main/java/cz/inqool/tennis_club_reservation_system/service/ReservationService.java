package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.ReservationCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.ReservationDto;
import cz.inqool.tennis_club_reservation_system.model.Reservation;
import cz.inqool.tennis_club_reservation_system.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReservationService extends CrudService<Reservation, Long, ReservationDto, ReservationCreateDto, ReservationDto> {


    public ReservationService(ReservationRepository reservationRepository, BeanMappingService beanMappingService) {
        super(reservationRepository, beanMappingService, Reservation.class, ReservationDto.class);
    }
}
