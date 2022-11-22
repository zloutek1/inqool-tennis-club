package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.BaseEntity;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Optional;

public abstract class CrudRepositoryImpl<T extends BaseEntity, ID> implements CrudRepository<T, ID> {
    private final Class<T> clazz;
    protected final EntityManager entityManager;

    public CrudRepositoryImpl(EntityManager entityManager, Class<T> clazz) {
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        var cb = entityManager.getCriteriaBuilder();

        var cq = cb.createQuery(clazz);
        var query = entityManager.createQuery(cq.select(cq.from(clazz)));

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList());
    }

    @Override
    public Optional<T> findById(ID id) {
        T foundEntity = entityManager.find(clazz, id);
        return Optional.ofNullable(foundEntity);
    }

    @Override
    public <S extends T> S save(S entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public <S extends T> S update(S entity) {
        entityManager.unwrap(Session.class).update(entity);
        return entity;
    }

    @Override
    public void deleteById(ID id) {
        T foundEntity = entityManager.find(clazz, id);
        entityManager.remove(foundEntity);
    }
}
