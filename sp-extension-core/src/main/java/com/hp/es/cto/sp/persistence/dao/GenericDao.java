package com.hp.es.cto.sp.persistence.dao;

import java.util.List;
import java.util.Map;

/**
 * Generic database access interface for defining common methods of db access.
 * 
 * @param <T>
 *            generic type for ORM entity
 * 
 * @author Dream
 */
public interface GenericDao<T> {

	T create(T t);

	T update(T t);

	T delete(T t);

	List<T> findAll();

	T findById(String id);

	List<String> queryStringPropertyBySql(String sql, String property,
			Object... params);

	List<T> queryEntityBySql(String sql,
			@SuppressWarnings("rawtypes") Class entity, Object... params);

	List<String> queryStringPropertyBySql(String sql,
			Map<String, Object> params, String property);

	List<T> queryEntityBySql(String sql, Map<String, Object> params,
			@SuppressWarnings("rawtypes") Class entity);

	List<T> queryEntityBySql(String sql, Map<String, Object> params,
			@SuppressWarnings("rawtypes")  Class entity, int offset, int length);
	
	int excuteBySql(String sql, Object... params);

	Integer countBySql(String sql, Map<String, Object> params);
}
