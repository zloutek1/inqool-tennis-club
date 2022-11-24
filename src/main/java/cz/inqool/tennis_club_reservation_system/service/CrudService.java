package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.EntityDto;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import cz.inqool.tennis_club_reservation_system.repository.CrudRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public abstract class CrudService<TEntity, ID, TDto, TCreateDto, TEditDto extends EntityDto<ID>> {

    private final CrudRepository<TEntity, ID> crudRepository;
    protected final BeanMappingService beanMappingService;

    private final Class<TEntity> entityClass;
    private final Class<TDto> dtoClass;


    public TDto save(TCreateDto createDto) {
        log.info("Saving new {} to the database", createDto);
        var entity = beanMappingService.mapTo(createDto, entityClass);

        var savedEntity = crudRepository.save(entity);

        return beanMappingService.mapTo(savedEntity, dtoClass);
    }

    public TDto edit(TEditDto editDto) {
        log.info("Editing {} with id {}", entityClass.getSimpleName(), editDto.getId());
        tryToFindEntity(editDto.getId());

        var entity = beanMappingService.mapTo(editDto, entityClass);
        entity = crudRepository.update(entity);
        return beanMappingService.mapTo(entity, dtoClass);
    }

    public TDto deleteById(ID id) {
        log.info("Deleting {} with id {}", entityClass.getSimpleName(), id);
        var entity = tryToFindEntity(id);

        crudRepository.softDeleteById(id);
        return beanMappingService.mapTo(entity, dtoClass);
    }


    public Page<TDto> findAll(Pageable pageable) {
        log.info("Fetching all {}", entityClass.getSimpleName());
        Page<TEntity> entities = crudRepository.findAll(pageable);
        return beanMappingService.mapTo(entities, dtoClass);
    }

    public Optional<TDto> findById(ID id) {
        log.info("Fetching {} with id {}", entityClass.getSimpleName(), id);
        var entity = crudRepository.findById(id);
        return beanMappingService.mapTo(entity, dtoClass);
    }



    protected TEntity tryToFindEntity(ID id) {
        return crudRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(entityClass.getSimpleName() + " with id " + id + " not found"));
    }
}
