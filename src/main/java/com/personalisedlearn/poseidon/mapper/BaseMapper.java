package com.personalisedlearn.poseidon.mapper;

/**
 * Base mapper interface with common mapping methods
 * @param <D> DTO type parameter
 * @param <E> Entity type parameter
 */
public interface BaseMapper<D, E> {
    E toEntity(D dto);
    D toDto(E entity);
    void updateEntityFromDto(D dto, E entity);
}
