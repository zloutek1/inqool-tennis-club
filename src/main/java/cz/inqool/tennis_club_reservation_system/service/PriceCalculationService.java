package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.ReservationDto;
import cz.inqool.tennis_club_reservation_system.model.GameType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@Service
public class PriceCalculationService {

    public BigDecimal calculatePrice(ReservationDto reservation) {
        var terrainPrice = reservation.getCourt().getTerrain().getPrice();

        var duration = Duration.between(reservation.getFromDate(), reservation.getToDate());
        var durationMinutes = BigDecimal.valueOf(duration.getSeconds() / 60);

        var multiplier = BigDecimal.valueOf(getMultiplier(reservation.getGameType()));

        return terrainPrice
                .multiply(durationMinutes)
                .multiply(multiplier);
    }

    private double getMultiplier(GameType gameType) {
        switch (gameType) {
            case SINGLES: return 1.0;
            case DOUBLES: return 1.5;
            default: throw new IllegalArgumentException("unknown game type " + gameType);
        }
    }

}
