package com.practice.project.dto;

/**
 * @param <E> Entity
 * @param <D> Dto
 */
public interface DtoImpl<E, D> {
    D of(E e);
    E toEntity(D d);
}
