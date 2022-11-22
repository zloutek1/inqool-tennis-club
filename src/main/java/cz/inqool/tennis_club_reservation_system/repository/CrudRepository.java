package cz.inqool.tennis_club_reservation_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface CrudRepository<T, ID> extends Repository<T, ID> {

    Page<T> findAll(Pageable pageable);

    Optional<T> findById(ID id);

    <S extends T> S save(S entity);

    <S extends T> S update(S entity);

    void softDeleteById(ID id);

}
