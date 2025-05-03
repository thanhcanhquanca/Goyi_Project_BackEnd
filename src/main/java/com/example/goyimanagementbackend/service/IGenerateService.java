package com.example.goyimanagementbackend.service;

import java.util.List;

public interface IGenerateService<T> {
    T create(T entity);
    T update(T entity);
    void delete(Integer id);
    T findById(Integer id);
    List<T> findAll();
}