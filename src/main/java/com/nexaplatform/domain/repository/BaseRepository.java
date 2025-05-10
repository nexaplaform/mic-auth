package com.nexaplatform.domain.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collections;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<E, K> {

    default E create(E entity){ return null; };

    default List<E> getPaginated(Integer page, Integer size, Sort.Direction sort) { return Collections.emptyList();};

    default E getById(K id) { return null; };

    default E update(K id, E entity) { return null; };

    default void delete(K id) { };
}
