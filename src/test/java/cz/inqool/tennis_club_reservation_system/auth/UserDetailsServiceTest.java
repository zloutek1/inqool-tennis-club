package cz.inqool.tennis_club_reservation_system.auth;

import cz.inqool.tennis_club_reservation_system.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static cz.inqool.tennis_club_reservation_system.user.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDetailsServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void loadUserByUsername_givenValidUser_returnsCorrectDetails() {
        var user = createUser(1L, "john123", List.of("USER", "ADMIN"));

        when(userRepository.findByUsername("john123"))
                .thenReturn(Optional.of(user));

        var actual = userDetailsService.loadUserByUsername(user.getUsername());

        assertThat(actual).isEqualTo(user);
        assertThat(actual.getAuthorities()).isEqualTo(user.getAuthorities());
    }

    @Test
    public void loadUserByUsername_givenInvalidUser_returnsCorrectDetails() {
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userDetailsService.loadUserByUsername("non_existent"))
                .withMessage("User with username non_existent not found");
    }

}
