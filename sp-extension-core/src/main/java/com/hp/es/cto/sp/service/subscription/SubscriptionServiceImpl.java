package com.hp.es.cto.sp.service.subscription;

import java.util.List;

import javax.inject.Named;

import com.hp.es.cto.sp.persistence.dao.subscription.SubscriptionDao;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;
import com.hp.es.cto.sp.service.GenericServiceImpl;

@Named
public class SubscriptionServiceImpl extends GenericServiceImpl<Subscription>
		implements SubscriptionService {
	private SubscriptionDao subscriptionDao;

	public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
		this.subscriptionDao = subscriptionDao;
	}

	@Override
	public List<Subscription> findByOwnerId(final int offset, final int length,
			String ownerId, String status, String startTime, String endTime , String terminateFrom, String terminateTo) {
		return subscriptionDao.findByOwnerIdAndStatus(offset, length,
				ownerId, status, startTime, endTime, terminateFrom, terminateTo);
	}

	@Override
	public List<Subscription> findByOrgId(final int offset, final int length,
			String orgId, String status, String startTime, String endTime , String terminateFrom, String terminateTo) {
		return subscriptionDao.findByOrgIdAndStatus(offset, length, orgId,
				status, startTime, endTime, terminateFrom, terminateTo);
	}

	@Override
	public List<Subscription> findByProject(final int offset, final int length,
			String project, String status, String startTime, String endTime , String terminateFrom, String terminateTo) {
		return subscriptionDao.findByProjectAndStatus(offset, length,
				project, status, startTime, endTime, terminateFrom, terminateTo);
	}

	@Override
	public List<Subscription> findByContextNodeId(final int offset,
			final int length, String nodeId, String status, String startTime, String endTime , String terminateFrom, String terminateTo) {
		return subscriptionDao.findByContextNodeIdAndStatus(offset, length,
				nodeId, status, startTime, endTime, terminateFrom, terminateTo);
	}
	
	@Override
	public List<Subscription> findAllSubscription(int offset, int length,
			String status, String startTime, String endTime , String terminateFrom, String terminateTo) {
		return subscriptionDao.findAllSubscriptionWithStatus(offset, length, status, startTime, endTime, terminateFrom, terminateTo);
	}


	@Override
	public Integer countByOwnerId(String ownerId, String status, String startTime, String endTime , String terminateFrom, String terminateTo) {
		return subscriptionDao.countByOwnerIdWithStatus(ownerId, status, startTime, endTime, terminateFrom, terminateTo);
	}

	@Override
	public Integer countByOrgId(String orgId, String status, String startTime, String endTime , String terminateFrom, String terminateTo) {
		return subscriptionDao.countByOrgIdWithStatus(orgId, status, startTime, endTime, terminateFrom, terminateTo);
	}

	@Override
	public Integer countByContextNodeId(String contextNodeId, String status, String startTime, String endTime , String terminateFrom, String terminateTo) {
		return subscriptionDao.countByContextNodeIdWithStatus(contextNodeId, status, startTime, endTime, terminateFrom, terminateTo);
	}

	@Override
	public Integer countByProject(String projectName, String status, String startTime, String endTime , String terminateFrom, String terminateTo) {
		return subscriptionDao.countByProjectWithStatus(projectName, status, startTime, endTime, terminateFrom, terminateTo);
	}

	@Override
	public Integer countAllSubscription(String status, String startTime, String endTime , String terminateFrom, String terminateTo) {
		return subscriptionDao.countAllWithStatus(status, startTime, endTime, terminateFrom, terminateTo);
	}

	@Override
	public Integer countByCondition(String ownerId, String orgId, String contextNodeId, String projectName, String status, String startTime, String endTime , String terminateFrom, String terminateTo,String product, String version) {
		return subscriptionDao.countAllWithConditions(ownerId, orgId, contextNodeId, projectName, status, startTime, endTime, terminateFrom, terminateTo, product, version);
	}

	@Override
	public List<Subscription> findByCondition(int offset, int limit, String ownerId, String orgId, String contextNodeId, String projectName, String status, String startTime, String endTime , String terminateFrom, String terminateTo,String product, String version) {
		return subscriptionDao.findAllWithConditions(offset, limit, ownerId, orgId, contextNodeId, projectName, status, startTime, endTime, terminateFrom, terminateTo, product, version);
	}

}
