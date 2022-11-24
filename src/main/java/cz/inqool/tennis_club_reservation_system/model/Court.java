package cz.inqool.tennis_club_reservation_system.model;

import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courts")
@Getter @Setter @ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED, onConstructor_ = @PersistenceCreator)
public class Court extends BaseEntity {

    private int number;

    @ManyToOne
    private Terrain terrain;

    @ToString.Exclude
    @OneToMany
    private Set<Reservation> reservations = new HashSet<>();

    public Court(int number, Terrain terrain) {
        this.number = number;
        this.terrain = terrain;
    }
}
