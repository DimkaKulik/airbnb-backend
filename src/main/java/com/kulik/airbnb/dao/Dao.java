package com.kulik.airbnb.dao;

import java.util.List;

public interface Dao<T> {
    List<T> getPage(int limit, int offset);

    T getById(Long id);

    int create(T t);

    int update(T t);

    int delete(T t);
}