package cz.inqool.tennis_club_reservation_system.auth.refresh_token;

import cz.inqool.tennis_club_reservation_system.user.User;
import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "refresh_tokens")
@Getter @Setter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED, onConstructor_ = @PersistenceCreator)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @OneToOne(optional = false)
    private User user;

    @NonNull
    @Column(nullable = false, unique = true)
    private String token;

    @NonNull
    @Column(nullable = false)
    private Instant expiryDate;

    public RefreshToken(@NonNull User user, @NonNull String token, @NonNull Instant expiryDate) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefreshToken)) {
            return false;
        }
        RefreshToken that = (RefreshToken) o;
        return getToken().equals(that.getToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToken());
    }
}
