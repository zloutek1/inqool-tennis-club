package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.ReservationCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.ReservationDto;
import cz.inqool.tennis_club_reservation_system.dto.UserCreateDto;
import cz.inqool.tennis_club_reservation_system.model.Reservation;
import cz.inqool.tennis_club_reservation_system.model.User;
import cz.inqool.tennis_club_reservation_system.repository.ReservationRepository;
import cz.inqool.tennis_club_reservation_system.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class ReservationService extends CrudService<Reservation, Long, ReservationDto, ReservationCreateDto, ReservationDto> {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, BeanMappingService beanMappingService, UserRepository userRepository) {
        super(reservationRepository, beanMappingService, Reservation.class, ReservationDto.class);
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReservationDto save(ReservationCreateDto reservationCreateDto) {
        log.info("Saving new {} to the database", reservationCreateDto);

        var user = saveOrGetUser(reservationCreateDto.getUser());
        var reservation = beanMappingService.mapTo(reservationCreateDto, Reservation.class);
        reservation.setUser(user);

        reservation = reservationRepository.save(reservation);
        user.addReservation(reservation);

        return beanMappingService.mapTo(reservation, ReservationDto.class);
    }

    private User saveOrGetUser(UserCreateDto userCreateDto) {
        return userRepository.findByUsername(userCreateDto.getUsername())
                .orElseGet(() -> userRepository.save(beanMappingService.mapTo(userCreateDto, User.class)));
    }
}
