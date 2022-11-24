package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.CourtCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.CourtDto;
import cz.inqool.tennis_club_reservation_system.model.Court;
import cz.inqool.tennis_club_reservation_system.repository.CourtRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CourtService extends CrudService<Court, Long, CourtDto, CourtCreateDto, CourtDto> {

    public CourtService(CourtRepository courtRepository, BeanMappingService beanMappingService) {
        super(courtRepository, beanMappingService, Court.class, CourtDto.class);
    }
}
