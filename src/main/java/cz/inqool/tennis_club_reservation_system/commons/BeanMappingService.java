package cz.inqool.tennis_club_reservation_system.commons;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BeanMappingService {

    private final ModelMapper mapper;

    public <T, U> List<U> mapTo(@NonNull Collection<T> objects, Class<U> mapToClass) {
        return objects.stream()
                .map(o -> mapTo(o, mapToClass))
                .collect(Collectors.toList());
    }

    public <T, U> Optional<U> mapTo(@NonNull Optional<T> obj, Class<U> mapToClass) {
        if (obj.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapTo(obj.get(), mapToClass));
    }

    public <T, U> Page<U> mapTo(@NonNull Page<T> obj, Class<U> mapToClass) {
        return obj.map(o -> mapTo(o, mapToClass));
    }

    public <T, U> U mapTo(@NonNull T obj, Class<U> mapToClass) {
        log.debug("mapping {} to class {}", obj, mapToClass);
        return mapper.map(obj, mapToClass);
    }
}
