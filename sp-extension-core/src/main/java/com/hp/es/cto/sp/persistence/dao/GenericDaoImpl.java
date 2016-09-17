package com.hp.es.cto.sp.persistence.dao;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.core.GenericTypeResolver;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.hp.es.cto.sp.persistence.dao.subscription.SubscriptionDaoImpl;

/**
 * Generic database access implementation for defining common methods of db
 * access.
 * 
 * @param <T>
 * 
 * @author Dream
 */
public abstract class GenericDaoImpl<T> extends HibernateDaoSupport implements
		GenericDao<T> {

	private final Class<T> genericClass;
	
	static final String WHERE = " where ";
	static final String AND = " and ";
	static final String SPACE = " ";
	static final String JOIN = " join ";
	static final String ON = " on ";

	/**
	 * Constructs an instance of GenericDaoImpl and resolve the generic class.
	 */
	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		// resolve the class type of generic.
		this.genericClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(
				getClass(), GenericDaoImpl.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T create(T t) {
		getHibernateTemplate().save(t);
		return t;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T update(T t) {
		getHibernateTemplate().update(t);
		return t;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T delete(T t) {
		getHibernateTemplate().delete(t);
		return t;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		String entityName = getEntityName();
		return getHibernateTemplate().find("from " + entityName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T findById(String id) {
		return getHibernateTemplate().get(genericClass, id);
	}

	/**
	 * Gets entity name from Entity annotation. If it is not defined in Entity
	 * annotation, return simple class name as the result.
	 * 
	 * @return entity name
	 */
	private String getEntityName() {
		String entityName = genericClass.getAnnotation(Entity.class).name();

		// If name is not set in @Entity, default name is class name.
		if (entityName == null || entityName.equals("")) {
			entityName = genericClass.getSimpleName();
		}
		return entityName;
	}

	/**
	 * execute the normal sql to get the uuid list
	 * 
	 * @param sql
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> queryStringPropertyBySql(String sql, String property,
			Object... params) {

		SQLQuery query = this.getSession().createSQLQuery(sql);
		int listIndex = 0;
		int nonListIndex = 0;

		for (int i = 0; i < params.length; i++) {
			if (params[i] instanceof ArrayList<?>) {
				query.setParameterList("list" + listIndex,
						(ArrayList<String>) params[i]);
				listIndex++;
			} else {
				query.setParameter(nonListIndex, params[i]);
				nonListIndex++;
			}
		}

		List<String> list = query
				.addScalar(property, StandardBasicTypes.STRING).list();
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> queryEntityBySql(String sql, Class entity, Object... params) {
		SQLQuery query = this.getSession().createSQLQuery(sql);
		int listIndex = 0;
		int nonListIndex = 0;

		for (int i = 0; i < params.length; i++) {
			if (params[i] instanceof ArrayList<?>) {
				query.setParameterList("list" + listIndex,
						(ArrayList<String>) params[i]);
				listIndex++;
			} else {
				query.setParameter(nonListIndex, params[i]);
				nonListIndex++;
			}
		}
		query.addEntity(entity);
		List<T> list = query.list();
		return list;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<String> queryStringPropertyBySql(String sql,
			Map<String, Object> params, String property) {
		SQLQuery query = this.getSession().createSQLQuery(sql);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() instanceof ArrayList<?>) {
				query.setParameterList(entry.getKey(),
						(ArrayList<String>) entry.getValue());
			} else {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		List<String> list = query
				.addScalar(property, StandardBasicTypes.STRING).list();
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> queryEntityBySql(String sql, Map<String, Object> params,
			Class entity) {
		SQLQuery query = this.getSession().createSQLQuery(sql);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() instanceof ArrayList<?>) {
				query.setParameterList(entry.getKey(),
						(ArrayList<String>) entry.getValue());
			} else {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		query.addEntity(entity);
		List<T> list = query.list();
		return list;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Integer countBySql(String sql, Map<String, Object> params) {
		SQLQuery query = this.getSession().createSQLQuery(sql);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() instanceof ArrayList<?>) {
				query.setParameterList(entry.getKey(),
						(ArrayList<String>) entry.getValue());
			} else {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		int count = ((Number) query.uniqueResult()).intValue();
		return count;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> queryEntityBySql(String sql, Map<String, Object> params,
			Class entity, final int offset, final int length) {
		SQLQuery query = this.getSession().createSQLQuery(sql);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() instanceof ArrayList<?>) {
				query.setParameterList(entry.getKey(),
						(ArrayList<String>) entry.getValue());
			} else {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		if (offset > 0) {
			query.setFirstResult(offset);
		}
		if (length > 0) {
			query.setMaxResults(length);
		}
		query.addEntity(entity);
		List<T> list = query.list();
		return list;
	}

	/**
	 * Execute the normal SQL
	 * 
	 * @param sql
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int excuteBySql(String sql, Object... params) {
		int result;
		SQLQuery query = this.getSession().createSQLQuery(sql);

		int listIndex = 0;
		int nonListIndex = 0;

		for (int i = 0; i < params.length; i++) {
			if (params[i] instanceof ArrayList<?>) {
				query.setParameterList("list" + listIndex,
						(ArrayList<String>) params[i]);
				listIndex++;
			} else {
				query.setParameter(nonListIndex, params[i]);
				nonListIndex++;
			}
		}

		result = query.executeUpdate();
		return result;
	}
	
	protected void appendCondition(StringBuffer sb, Map<String, Object> params, String conditionColunm, String comparator, String conditionName, String conditionValue) {
		if (!isNullOrEmpty(conditionColunm) && !isNullOrEmpty(conditionValue)) {
			if (sb.toString().contains(WHERE)) {
				sb.append(AND);
			}
			else {
				sb.append(WHERE);
			}
			sb.append(conditionColunm);
			sb.append(SPACE);
			sb.append(comparator);
			sb.append(SPACE);
			sb.append(":");
			sb.append(conditionName);
			sb.append(SPACE);
			params.put(conditionName, conditionValue);
		}
	}
	
	protected void appendTimeCondition(StringBuffer sb, Map<String, Object> params, String conditionColunm, String comparator, String conditionName, String conditionValue) {
		if (!isNullOrEmpty(conditionColunm) && !isNullOrEmpty(conditionValue)) {
			
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			Date sourceDate = null;
			try {
				sourceDate = formatter.parse(conditionValue);
			}
			catch (ParseException e) {
				logger.warn("failed to parse the time: " + conditionValue);
			}
			
			
			if (sb.toString().contains(WHERE)) {
				sb.append(AND);
			}
			else {
				sb.append(WHERE);
			}
			sb.append(conditionColunm);
			sb.append(SPACE);
			sb.append(comparator);
			sb.append(SPACE);
			sb.append(":");
			sb.append(conditionName);
			sb.append(SPACE);
			params.put(conditionName, sourceDate);
		}
	}
	

	
	protected void appendTableJoin(StringBuffer sb, String tableName, String baseTable, String baseColumn, String tableColumn) {
		if (!isNullOrEmpty(tableName) && !isNullOrEmpty(tableColumn) && !isNullOrEmpty(baseColumn)) {		
			sb.append(JOIN);
			sb.append(tableName);
			sb.append(SPACE);
			sb.append(ON);
			sb.append(baseTable+"."+baseColumn);
			sb.append(SPACE);
			sb.append("=");
			sb.append(SPACE);
			sb.append(tableName+"."+tableColumn);
			sb.append(SPACE);
		}
	}
	


}
