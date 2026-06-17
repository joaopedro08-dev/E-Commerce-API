package com.crud.ecommerce.business.util;

import com.crud.ecommerce.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

final public class EntityFinderUtils {

    private EntityFinderUtils() {}

    public static <T> T findById(JpaRepository<T, Long> repository, Long id, String notFoundMessage) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(notFoundMessage));
    }

    public void create(){}
    public void update(){}
    public void delete(){}
}