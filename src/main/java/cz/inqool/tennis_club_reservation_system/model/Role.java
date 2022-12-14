package cz.inqool.tennis_club_reservation_system.model;

import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles")
@Getter @Setter @ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED, onConstructor_ = @PersistenceCreator)
public class Role extends BaseEntity implements GrantedAuthority {

    @NonNull
    @Column(nullable = false, unique = true)
    private String name;

    public Role(@NonNull String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        Role role = (Role) o;
        return getName().equals(role.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String getAuthority() {
        return name;
    }
}