package cz.inqool.tennis_club_reservation_system.model;

import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courts")
@Getter @Setter @ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED, onConstructor_ = @PersistenceCreator)
public class Court extends BaseEntity {

    @Column(nullable = false, unique = true)
    private int number;

    @ManyToOne
    private Terrain terrain;

    @OneToMany
    @ToString.Exclude
    @OrderBy("createdAt")
    private List<Reservation> reservations = new ArrayList<>();

    public Court(int number, Terrain terrain) {
        this.number = number;
        this.terrain = terrain;
    }
}
