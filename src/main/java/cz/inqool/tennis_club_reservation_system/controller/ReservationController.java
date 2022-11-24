package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.configs.ApiUris;
import cz.inqool.tennis_club_reservation_system.dto.ReservationCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.ReservationDto;
import cz.inqool.tennis_club_reservation_system.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Tag(name = "reservation")
@RequestMapping(ApiUris.ROOT_URI)
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PutMapping(ApiUris.RESERVATION_NEW)
    public ResponseEntity<ReservationDto> newReservation(@Valid @RequestBody ReservationCreateDto createDto) {
        ReservationDto savedReservation = reservationService.save(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
    }

    @PutMapping(ApiUris.RESERVATION_EDIT)
    public ResponseEntity<ReservationDto> editReservation(@Valid @RequestBody ReservationDto editDto) {
        ReservationDto editedReservation = reservationService.edit(editDto);
        return ResponseEntity.ok(editedReservation);
    }

    @DeleteMapping(ApiUris.RESERVATION_DELETE)
    public ResponseEntity<ReservationDto> deleteReservation(@PathVariable Long id) {
        ReservationDto deletedReservation = reservationService.deleteById(id);
        return ResponseEntity.ok(deletedReservation);
    }

    @GetMapping(ApiUris.RESERVATIONS)
    @PageableAsQueryParam
    public ResponseEntity<Page<ReservationDto>> findAllReservations(@ParameterObject Pageable pageable) {
        Page<ReservationDto> reservations = reservationService.findAll(pageable);
        return ResponseEntity.ok(reservations);
    }

}
