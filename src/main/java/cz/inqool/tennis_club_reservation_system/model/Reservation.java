package cz.inqool.tennis_club_reservation_system.model;

import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter @Setter @ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED, onConstructor_ = @PersistenceCreator)
public class Reservation extends BaseEntity {

    @ManyToOne
    private Court court;

    @Enumerated(value = EnumType.STRING)
    private GameType gameType;

    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    @ManyToOne
    private User user;

}
