package com.crud.ecommerce.business.util;

import com.crud.ecommerce.dto.response.Response;
import com.crud.ecommerce.exception.ConflictException;
import org.springframework.data.jpa.repository.JpaRepository;

final public class EntityOperationUtils {
    private EntityOperationUtils() {}

    public static <T> Response create(JpaRepository<T, Long> repository, T entity, String successMessage) {
        repository.save(entity);
        return new Response(successMessage, true);
    }

    public static <T> Response update(JpaRepository<T, Long> repository, T entity, String successMessage) {
        repository.save(entity);
        return new Response(successMessage, true);
    }

    public static <T> Response delete(JpaRepository<T, Long> repository, T entity, String successMessage) {
        repository.delete(entity);
        return new Response(successMessage, true);
    }

    public static void validateNoLinks(boolean hasLinks, String conflictMessage) {
        if (hasLinks) throw new ConflictException(conflictMessage);
    }
}
