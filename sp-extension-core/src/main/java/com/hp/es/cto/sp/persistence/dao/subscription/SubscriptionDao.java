package com.hp.es.cto.sp.persistence.dao.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.dao.GenericDao;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;

/**
 * Database access interface for accessing SUBSCRIPTION table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDao
 * 
 * @author Victor
 */
public interface SubscriptionDao extends GenericDao<Subscription> {
	
	/**
	 * Finds a list of Subscriptions of specific user id.
	 * 
	 * @param ownerId
	 *            user id who owned the subscriptions
	 * @return
	 *         a list of Subscriptions
	 */
	List<Subscription> findByOwnerIdAndStatus(final int offset, final int length, String ownerId, String Status, String startTime, String endTime, String terminateFrom, String terminateTo);
	
	/**
	 * Finds a list of Subscriptions of specific org id.
	 * 
	 * @param orgId
	 *            organization id who owned the subscriptions
	 * @return
	 *         a list of Subscriptions
	 */
	List<Subscription> findByOrgIdAndStatus(final int offset, final int length, String orgId, String Status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * Finds a list of Subscriptions of specific project.
	 * 
	 * @param project
	 *            project owned the subscriptions
	 * @return
	 *         a list of Subscriptions
	 */
	List<Subscription> findByProjectAndStatus(final int offset, final int length, String project, String Status, String startTime, String endTime, String terminateFrom, String terminateTo);
	
	/**
	 * Finds a list of Subscriptions of specific context node.
	 * 
	 * @param nodeId
	 *            context node id owned the subscriptions
	 * @return
	 *         a list of Subscriptions
	 */
	List<Subscription> findByContextNodeIdAndStatus(final int offset, final int length, String nodeId, String Status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * Finds all Subscriptions with status
	 * @param offset
	 * @param length
	 * @param status
	 * @return
	 */
	List<Subscription> findAllSubscriptionWithStatus(int offset, int length,
			String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * find a list of Subscriptions of specific conditions.
	 * 
	 * @param offset
	 * @param limit
	 * @param ownerId
	 *            user id who owned the subscriptions
	 * @param orgId
	 *            organization id who owned the subscriptions
	 * @param nodeId
	 *            context node id owned the subscriptions
	 * @param project
	 *            project owned the subscriptions
	 * @param status
	 *            status of the subscription, if null or empty then will return all status
	 * @param startTime
	 *            , endTime only find the subscription with the time period
	 * @return
	 */
	List<Subscription> findAllWithConditions(int offset, int limit, String ownerId, String orgId, String contextNodeId, String projectName, String status, String startTime, String endTime, String terminateFrom, String terminateTo,String product, String version);
	
	Integer countByOwnerIdWithStatus(String ownerId, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	Integer countByOrgIdWithStatus(String orgId, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	Integer countByContextNodeIdWithStatus(String contextNodeId, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	Integer countByProjectWithStatus(String projectName, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	Integer countAllWithStatus(String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	Integer countAllWithConditions(String ownerId, String orgId, String contextNodeId, String projectName, String status, String startTime, String endTime, String terminateFrom, String terminateTo, String product, String version);

	
}
