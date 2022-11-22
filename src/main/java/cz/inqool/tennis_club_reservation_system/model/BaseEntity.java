package cz.inqool.tennis_club_reservation_system.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
@ToString
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "edited_at")
    protected LocalDateTime editedAt = null;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt = null;

    @PreRemove
    public void onDelete() {
        deletedAt = LocalDateTime.now();
    }

}
