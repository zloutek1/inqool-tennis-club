package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.ReservationDto;
import cz.inqool.tennis_club_reservation_system.dto.TerrainDto;
import cz.inqool.tennis_club_reservation_system.model.GameType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static cz.inqool.tennis_club_reservation_system.model.factory.CourtFactory.createCourtDto;
import static cz.inqool.tennis_club_reservation_system.model.factory.TerrainFactory.createTerrainDto;
import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.createUserDto;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PriceCalculationServiceTest {

    @Autowired
    private PriceCalculationService priceCalculationService;

    @Autowired
    private Clock clock;

    @ParameterizedTest
    @MethodSource("priceStream")
    void calculatePrice_givenTerrainDurationAndGameType_returnsCorrectPrice(TerrainDto terrain, Duration duration, GameType gameType, BigDecimal expectedPrice) {

        var court = createCourtDto(2L, 9, terrain);
        var now = LocalDateTime.now(clock);
        var user = createUserDto(1L, "bob");
        var reservation = new ReservationDto(1L, court, gameType, now, now.plusSeconds(duration.getSeconds()), user);

        var actualPrice = priceCalculationService.calculatePrice(reservation);

        assertThat(actualPrice).isEqualByComparingTo(expectedPrice);
    }

    private static Stream<Arguments> priceStream() {
        var dryTerrain = createTerrainDto(1L, "DRY", BigDecimal.valueOf(2.2));
        var wetTerrain = createTerrainDto(2L, "WET", BigDecimal.valueOf(6.0));
        return Stream.of(
                Arguments.of(dryTerrain, Duration.ofMinutes(1), GameType.SINGLES, BigDecimal.valueOf(2.2)),
                Arguments.of(dryTerrain, Duration.ofHours(1), GameType.SINGLES, BigDecimal.valueOf(132.0)),
                Arguments.of(dryTerrain, Duration.ofMinutes(1), GameType.DOUBLES, BigDecimal.valueOf(3.3)),

                Arguments.of(wetTerrain, Duration.ofMinutes(1), GameType.SINGLES, BigDecimal.valueOf(6.0)),
                Arguments.of(wetTerrain, Duration.ofHours(1), GameType.SINGLES, BigDecimal.valueOf(360.0)),
                Arguments.of(wetTerrain, Duration.ofMinutes(1), GameType.DOUBLES, BigDecimal.valueOf(9.0))
        );
    }

}