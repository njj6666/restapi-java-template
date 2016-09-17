package com.hp.es.cto.sp.service;

import java.util.List;

public interface GenericService<T> {
	T create(T t);

	T update(T t);

	T delete(T t);

	List<T> findAll();

	T findById(String id);
}
