package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.configs.ApiUris;
import cz.inqool.tennis_club_reservation_system.dto.CourtCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.CourtDto;
import cz.inqool.tennis_club_reservation_system.dto.ReservationDto;
import cz.inqool.tennis_club_reservation_system.service.CourtService;
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
import java.util.List;

@RestController
@Tag(name = "court")
@RequestMapping(ApiUris.ROOT_URI)
@RequiredArgsConstructor
public class CourtController {

    private final CourtService courtService;

    @PutMapping(ApiUris.COURT_NEW)
    public ResponseEntity<CourtDto> newCourt(@Valid @RequestBody CourtCreateDto createDto) {
        CourtDto savedCourt = courtService.save(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourt);
    }

    @PutMapping(ApiUris.COURT_EDIT)
    public ResponseEntity<CourtDto> editCourt(@Valid @RequestBody CourtDto editDto) {
        CourtDto editedCourt = courtService.edit(editDto);
        return ResponseEntity.ok(editedCourt);
    }

    @DeleteMapping(ApiUris.COURT_DELETE)
    public ResponseEntity<CourtDto> deleteCourt(@PathVariable Long id) {
        CourtDto deletedCourt = courtService.deleteById(id);
        return ResponseEntity.ok(deletedCourt);
    }

    @GetMapping(ApiUris.COURTS)
    @PageableAsQueryParam
    public ResponseEntity<Page<CourtDto>> findAllCourts(@ParameterObject Pageable pageable) {
        Page<CourtDto> courts = courtService.findAll(pageable);
        return ResponseEntity.ok(courts);
    }

    @GetMapping(ApiUris.COURT_RESERVATIONS)
    public ResponseEntity<List<ReservationDto>> findCourtReservations(@PathVariable int number) {
        List<ReservationDto> reservations = courtService.findReservations(number);
        return ResponseEntity.ok(reservations);
    }

}
