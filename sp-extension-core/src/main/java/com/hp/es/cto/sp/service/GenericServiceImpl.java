package com.hp.es.cto.sp.service;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;

public abstract class GenericServiceImpl<T> implements GenericService<T> {
	private GenericDao<T> genericDao;
	
	public void setGenericDao(GenericDao<T> genericDao) {
		this.genericDao = genericDao;
	}

	@Override
	public T create(T t) {
		return genericDao.create(t);
	}

	@Override
	public T update(T t) {
		return genericDao.update(t);
	}

	@Override
	public T delete(T t) {
		return genericDao.delete(t);
	}

	@Override
	public List<T> findAll() {
		return genericDao.findAll();
	}

	@Override
	public T findById(String id) {
		return genericDao.findById(id);
	}

}
