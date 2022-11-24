package cz.inqool.tennis_club_reservation_system.model;

import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "terrains")
@Getter @Setter @ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED, onConstructor_ = @PersistenceCreator)
public class Terrain extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String type;

    private BigDecimal price;

    public Terrain(String type, BigDecimal price) {
        this.type = type;
        this.price = price;
    }
}
