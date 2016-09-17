package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.service.GenericService;

public interface SubscriptionService extends GenericService<Subscription> {
	/**
	 * Finds a list of Subscriptions of specific user id and status.
	 * 
	 * @param ownerId
	 *            user id who owned the subscriptions
	 * @param status
	 *            status of the subscription, if null or empty then will return all status
	 * @param startTime
	 *            , endTime only find the subscription with the time period
	 * @return a list of Subscriptions
	 */
	List<Subscription> findByOwnerId(final int offset, final int length, String ownerId, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * Finds a list of Subscriptions of specific org id and status.
	 * 
	 * @param orgId
	 *            organization id who owned the subscriptions
	 * @param status
	 *            status of the subscription, if null or empty then will return all status
	 * @param startTime
	 *            , endTime only find the subscription with the time period
	 * @return a list of Subscriptions
	 */
	List<Subscription> findByOrgId(final int offset, final int length, String orgId, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * Finds a list of Subscriptions of specific project and status.
	 * 
	 * @param project
	 *            project owned the subscriptions
	 * @param status
	 *            status of the subscription, if null or empty then will return all status
	 * @param startTime
	 *            , endTime only find the subscription with the time period
	 * @return a list of Subscriptions
	 */
	List<Subscription> findByProject(final int offset, final int length, String project, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * Finds a list of Subscriptions of specific context node and status.
	 * 
	 * @param nodeId
	 *            context node id owned the subscriptions
	 * @param status
	 *            status of the subscription, if null or empty then will return all status
	 * @param startTime
	 *            , endTime only find the subscription with the time period
	 * @return a list of Subscriptions
	 */
	List<Subscription> findByContextNodeId(final int offset, final int length, String nodeId, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * Count a list of Subscriptions of specific user id and status.
	 * 
	 * @param ownerId
	 * @param status
	 *            status of the subscription, if null or empty then will return all status
	 * @param startTime
	 *            , endTime only find the subscription with the time period
	 * @return
	 */
	Integer countByOwnerId(String ownerId, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * Count a list of Subscriptions of specific csa org id and status.
	 * 
	 * @param orgId
	 * @param status
	 *            status of the subscription, if null or empty then will return all status
	 * @param startTime
	 *            , endTime only find the subscription with the time period
	 * @return
	 */
	Integer countByOrgId(String orgId, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * Count a list of Subscriptions of specific context node id and status.
	 * 
	 * @param contextNodeId
	 * @param status
	 *            status of the subscription, if null or empty then will return all status
	 * @param startTime
	 *            , endTime only find the subscription with the time period
	 * @return
	 */
	Integer countByContextNodeId(String contextNodeId, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * Count a list of Subscriptions of specific project and status.
	 * 
	 * @param projectName
	 * @param status
	 *            status of the subscription, if null or empty then will return all status
	 * @param startTime
	 *            , endTime only find the subscription with the time period
	 * @return
	 */
	Integer countByProject(String projectName, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * Count a list of Subscriptions by status.
	 * 
	 * @param status
	 * @param startTime
	 *            , endTime only find the subscription with the time period
	 * @return
	 */
	Integer countAllSubscription(String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * find a list of Subscriptions of specific status.
	 * 
	 * @param offset
	 * @param limit
	 * @param status
	 *            status of the subscription, if null or empty then will return all status
	 * @param startTime
	 *            , endTime only find the subscription with the time period
	 * @return
	 */
	List<Subscription> findAllSubscription(int offset, int limit, String status, String startTime, String endTime, String terminateFrom, String terminateTo);

	/**
	 * Count a list of Subscriptions by conditions.
	 * 
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
	Integer countByCondition(String ownerId, String orgId, String contextNodeId, String projectName, String status, String startTime, String endTime, String terminateFrom, String terminateTo,String product, String version);

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
	List<Subscription> findByCondition(int offset, int limit, String ownerId, String orgId, String contextNodeId, String projectName, String status, String startTime, String endTime, String terminateFrom, String terminateTo,String product, String version);

}
