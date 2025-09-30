package com.nexaplatform.domain.repository;

import com.nexaplaform.core.api.dto.SortEnumDTO;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<E, K> {

    default E create(E model) {
        return null;
    }

    default List<E> getPaginated(Integer page, Integer size, SortEnumDTO sort) {
        return Collections.emptyList();
    }

    default E getById(K id) {
        return null;
    }

    default E update(K id, E model) {
        return null;
    }

    default void delete(K id) {
    }

    default ResponseEntity<List<E>> findAllPaginated(int page, int size, String sort, SortEnumDTO direction) {
        return null;
    }

    default ResponseEntity<List<E>> filters(E filterProperties, int page, int size, SortEnumDTO direction, String... sortProperties) {
        return null;
    }
}
