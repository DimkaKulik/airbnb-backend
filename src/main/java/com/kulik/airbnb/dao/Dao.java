package com.kulik.airbnb.dao;

import java.util.List;

public interface Dao<T> {
    T get(Long id);

    List<T> getAll();

    Long create(T t);

    int update(T t);

    int delete(T t);
}