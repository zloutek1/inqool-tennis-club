package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.BaseEntity;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@NoRepositoryBean
public abstract class CrudRepositoryImpl<T extends BaseEntity, ID> implements CrudRepository<T, ID> {
    private final Class<T> clazz;

    @PersistenceContext
    protected EntityManager entityManager;
    private final Clock clock;

    public CrudRepositoryImpl(Clock clock, Class<T> clazz) {
        this.clazz = clazz;
        this.clock = clock;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        var cb = entityManager.getCriteriaBuilder();
        var cq = cb.createQuery(clazz);

        var entity = cq.from(clazz);
        cq.where(cb.isNull(entity.get("deletedAt")));

        var query = entityManager.createQuery(cq);

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList());
    }

    @Override
    public Optional<T> findById(ID id) {
        var cb = entityManager.getCriteriaBuilder();
        var cq = cb.createQuery(clazz);

        var entity = cq.from(clazz);
        cq.where(cb.equal(entity.get("id"), id), cb.isNull(entity.get("deletedAt")));

        var query = entityManager.createQuery(cq);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
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
    public void softDeleteById(ID id) {
        if (findById(id).isEmpty()) {
            throw new NoResultException("Entity with id " + id + " not found");
        }

        var cb = entityManager.getCriteriaBuilder();

        var update = cb.createCriteriaUpdate(clazz);
        var entity = update.from(clazz);

        update.set("deletedAt", LocalDateTime.now(clock));
        update.where(cb.equal(entity.get("id"), id));

        entityManager.createQuery(update).executeUpdate();
    }
}
