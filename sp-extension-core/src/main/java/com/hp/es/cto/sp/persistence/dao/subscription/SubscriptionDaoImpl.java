package com.hp.es.cto.sp.persistence.dao.subscription;

import static com.hp.es.cto.sp.util.StringUtil.isNullOrEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.es.cto.sp.persistence.dao.GenericDaoImpl;
import com.hp.es.cto.sp.persistence.entity.subscription.Subscription;

/**
 * Database access implementation class for accessing SUBSCRIPTION table.
 * 
 * @see com.hp.es.cto.sp.persistence.dao.GenericDaoImpl
 * @see com.hp.es.cto.sp.persistence.dao.provider.ProviderRegionDao
 * 
 * @author Victor
 */
public class SubscriptionDaoImpl extends GenericDaoImpl<Subscription> implements SubscriptionDao {

	static final String ORDER_BY_START_TIME_DESC = " order by START_TIME DESC ";
	static final String SELECT_FROM_SUBSCRIPTION = " Select distinct SUBSCRIPTION.* from SUBSCRIPTION ";
	static final String SELECT_COUNT_FROM_SUBSCRIPTION = " Select count(*) from SUBSCRIPTION ";

	@Override
	public List<Subscription> findByOwnerIdAndStatus(final int offset, final int length, String ownerId, String status, String startTime, String endTime, String terminateFrom, String terminateTo) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_FROM_SUBSCRIPTION);
		appendCondition(sb, params, "OWNER_ID", "LIKE", "ownerId", "%" + ownerId + "%");
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime, terminateFrom, terminateTo);
		sb.append(ORDER_BY_START_TIME_DESC);
		return super.queryEntityBySql(sb.toString(), params, Subscription.class, offset, length);
	}

	@Override
	public List<Subscription> findByOrgIdAndStatus(final int offset, final int length, String orgId, String status, String startTime, String endTime, String terminateFrom, String terminateTo) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_FROM_SUBSCRIPTION);
		appendCondition(sb, params, "CSA_ORG_ID", "LIKE", "orgId", "%" + orgId + "%");
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime, terminateFrom, terminateTo);
		sb.append(ORDER_BY_START_TIME_DESC);
		return super.queryEntityBySql(sb.toString(), params, Subscription.class, offset, length);
	}

	@Override
	public List<Subscription> findByContextNodeIdAndStatus(final int offset, final int length, String nodeId, String status, String startTime, String endTime, String terminateFrom, String terminateTo) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_FROM_SUBSCRIPTION);
		appendCondition(sb, params, "CONTEXT_NODE_ID", "LIKE", "nodeId", "%" + nodeId + "%");
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime, terminateFrom, terminateTo);
		sb.append(ORDER_BY_START_TIME_DESC);
		return super.queryEntityBySql(sb.toString(), params, Subscription.class, offset, length);
	}

	@Override
	public List<Subscription> findByProjectAndStatus(final int offset, final int length, String project, String status, String startTime, String endTime, String terminateFrom, String terminateTo) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_FROM_SUBSCRIPTION);
		appendCondition(sb, params, "PROJECT", "LIKE", "project", "%" + project + "%");
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime, terminateFrom, terminateTo);
		sb.append(ORDER_BY_START_TIME_DESC);
		return super.queryEntityBySql(sb.toString(), params, Subscription.class, offset, length);
	}

	@Override
	public List<Subscription> findAllSubscriptionWithStatus(int offset, int length, String status, String startTime, String endTime, String terminateFrom, String terminateTo) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_FROM_SUBSCRIPTION);
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime, terminateFrom, terminateTo);
		sb.append(ORDER_BY_START_TIME_DESC);
		return super.queryEntityBySql(sb.toString(), params, Subscription.class, offset, length);
	}

	@Override
	public Integer countByOwnerIdWithStatus(String ownerId, String status, String startTime, String endTime, String terminateFrom, String terminateTo) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_COUNT_FROM_SUBSCRIPTION);
		appendCondition(sb, params, "OWNER_ID", "LIKE", "ownerId", "%" + ownerId + "%");
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime, terminateFrom, terminateTo);
		return super.countBySql(sb.toString(), params);
	}

	@Override
	public Integer countByOrgIdWithStatus(String orgId, String status, String startTime, String endTime, String terminateFrom, String terminateTo) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_COUNT_FROM_SUBSCRIPTION);
		appendCondition(sb, params, "CSA_ORG_ID", "LIKE", "orgId", "%" + orgId + "%");
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime, terminateFrom, terminateTo);
		return super.countBySql(sb.toString(), params);
	}

	@Override
	public Integer countByContextNodeIdWithStatus(String contextNodeId, String status, String startTime, String endTime, String terminateFrom, String terminateTo) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_COUNT_FROM_SUBSCRIPTION);
		appendCondition(sb, params, "CONTEXT_NODE_ID", "LIKE", "contextNodeId", "%" + contextNodeId + "%");
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime, terminateFrom, terminateTo);
		return super.countBySql(sb.toString(), params);
	}

	@Override
	public Integer countByProjectWithStatus(String projectName, String status, String startTime, String endTime, String terminateFrom, String terminateTo) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_COUNT_FROM_SUBSCRIPTION);
		appendCondition(sb, params, "PROJECT", "LIKE", "projectName", "%" + projectName + "%");
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime, terminateFrom, terminateTo);
		return super.countBySql(sb.toString(), params);
	}

	@Override
	public Integer countAllWithStatus(String status, String startTime, String endTime, String terminateFrom, String terminateTo) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_COUNT_FROM_SUBSCRIPTION);
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime, terminateFrom, terminateTo);
		return super.countBySql(sb.toString(), params);
	}

	private void appendTime(StringBuffer sb, Map<String, Object> params, String startTime, String endTime, String terminateFrom, String terminateTo) {
		if (!isNullOrEmpty(startTime)) {
			appendTimeCondition(sb, params, "START_TIME", ">", "startTime", startTime);
		}

		if (!isNullOrEmpty(endTime)) {
			appendTimeCondition(sb, params, "START_TIME", "<", "endTime", endTime);
		}
		
		if (!isNullOrEmpty(terminateFrom)) {
			appendTimeCondition(sb, params, "TERMINATE_TIME", ">", "terminateFrom", terminateFrom);
		}
		
		if (!isNullOrEmpty(terminateTo)) {
			appendTimeCondition(sb, params, "TERMINATE_TIME", "<", "terminateTo", terminateTo);
		}
	}
	
//	private void appendTimeWithSQL(StringBuffer sb, String startTime, String endTime, String terminateFrom, String terminateTo) {
//		if (!isNullOrEmpty(startTime)) {
//			appendConditionWithSQL(sb, "START_TIME", ">", startTime);
//		}
//
//		if (!isNullOrEmpty(endTime)) {
//			appendConditionWithSQL(sb, "START_TIME", "<", endTime);
//		}
//		
//		if (!isNullOrEmpty(terminateFrom)) {
//			appendConditionWithSQL(sb, "TERMINATE_TIME", ">", terminateFrom);
//		}
//		
//		if (!isNullOrEmpty(terminateTo)) {
//			appendConditionWithSQL(sb, "TERMINATE_TIME", "<", terminateTo);
//		}
//	}

	@Override
	public List<Subscription> findAllWithConditions(int offset, int limit, String ownerId, String orgId, String contextNodeId, String projectName, String status, String startTime, String endTime,
			String terminateFrom, String terminateTo,String product, String version) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_FROM_SUBSCRIPTION);
		if (!isNullOrEmpty(product) || !isNullOrEmpty(version)) {
//			return findAllByProduct(offset, limit, ownerId, orgId, contextNodeId, projectName, status, startTime, endTime,
//					terminateFrom, terminateTo,product, version);
			appendTableJoin(sb,"SUBSCRIPTION_SERVER","SUBSCRIPTION","UUID","SUBSCRIPTION_ID");
			appendTableJoin(sb,"SUBSCRIPTION_SERVER_PRODUCT","SUBSCRIPTION_SERVER","UUID","SERVER_ID");
			appendCondition(sb, params, "SUBSCRIPTION_SERVER_PRODUCT.PRODCUT_NAME", "=", "product", product);
			appendCondition(sb, params, "SUBSCRIPTION_SERVER_PRODUCT.PRODUCT_VERSION", "=", "version", version);
		}
		if (!isNullOrEmpty(ownerId)) {
			appendCondition(sb, params, "OWNER_ID", "LIKE", "ownerId", "%" + ownerId + "%");
		}
		if (!isNullOrEmpty(orgId)) {
			appendCondition(sb, params, "CSA_ORG_ID", "LIKE", "orgId", "%" + orgId + "%");
		}
		if (!isNullOrEmpty(contextNodeId)) {
			appendCondition(sb, params, "CONTEXT_NODE_ID", "LIKE", "contextNodeId", "%" + contextNodeId + "%");
		}
		if (!isNullOrEmpty(projectName)) {
			appendCondition(sb, params, "PROJECT", "LIKE", "projectName", "%" + projectName + "%");
		}
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime,terminateFrom, terminateTo);
		sb.append(ORDER_BY_START_TIME_DESC);
		return super.queryEntityBySql(sb.toString(), params, Subscription.class, offset, limit);
	}

	@Override
	public Integer countAllWithConditions(String ownerId, String orgId, String contextNodeId, String projectName, String status, String startTime, String endTime, String terminateFrom,
			String terminateTo,String product, String version) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append(SELECT_COUNT_FROM_SUBSCRIPTION);
		if (!isNullOrEmpty(product) || !isNullOrEmpty(version)) {
			appendTableJoin(sb,"SUBSCRIPTION_SERVER","SUBSCRIPTION","UUID","SUBSCRIPTION_ID");
			appendTableJoin(sb,"SUBSCRIPTION_SERVER_PRODUCT","SUBSCRIPTION_SERVER","UUID","SERVER_ID");
			appendCondition(sb, params, "SUBSCRIPTION_SERVER_PRODUCT.PRODCUT_NAME", "=", "product", product);
			appendCondition(sb, params, "SUBSCRIPTION_SERVER_PRODUCT.PRODUCT_VERSION", "=", "version", version);
		}
		if (!isNullOrEmpty(ownerId)) {
			appendCondition(sb, params, "OWNER_ID", "LIKE", "ownerId", "%" + ownerId + "%");
		}
		if (!isNullOrEmpty(orgId)) {
			appendCondition(sb, params, "CSA_ORG_ID", "LIKE", "orgId", "%" + orgId + "%");
		}
		if (!isNullOrEmpty(contextNodeId)) {
			appendCondition(sb, params, "CONTEXT_NODE_ID", "LIKE", "contextNodeId", "%" + contextNodeId + "%");
		}
		if (!isNullOrEmpty(projectName)) {
			appendCondition(sb, params, "PROJECT", "LIKE", "projectName", "%" + projectName + "%");
		}
		appendCondition(sb, params, "STATUS", "=", "status", status);
		appendTime(sb, params, startTime, endTime, terminateFrom, terminateTo);
		return super.countBySql(sb.toString(), params);
	}
	
	
//	private List<Subscription> findAllByProduct(int offset, int limit, String ownerId, String orgId, String contextNodeId, String projectName, String status, String startTime, String endTime,
//			String terminateFrom, String terminateTo,String product, String version) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("select distinct sub.* from SUBSCRIPTION sub ");
//		sb.append(",SUBSCRIPTION_SERVER ser, SUBSCRIPTION_SERVER_PRODUCT prod  ");
//		sb.append("where sub.UUID=ser.SUBSCRIPTION_ID and ser.UUID=prod.SERVER_ID ");
//		if (!isNullOrEmpty(product)) {
//			appendConditionWithSQL(sb,"prod.PRODCUT_NAME", "LIKE", "%" + product + "%");
//		}
//		if (!isNullOrEmpty(version)) {
//			appendConditionWithSQL(sb,"prod.PRODUCT_VERSION", "LIKE", "%" + version + "%");
//		}
//		if (!isNullOrEmpty(ownerId)) {
//			appendConditionWithSQL(sb, "sub.OWNER_ID", "LIKE","%" + ownerId + "%");
//		}
//		if (!isNullOrEmpty(orgId)) {
//			appendConditionWithSQL(sb, "CSA_ORG_ID", "LIKE", "%" + orgId + "%");
//		}
//		if (!isNullOrEmpty(contextNodeId)) {
//			appendConditionWithSQL(sb, "CONTEXT_NODE_ID", "LIKE", "%" + contextNodeId + "%");
//		}
//		if (!isNullOrEmpty(projectName)) {
//			appendConditionWithSQL(sb, "PROJECT", "LIKE", "%" + projectName + "%");
//		}
//		appendConditionWithSQL(sb, "STATUS", "=", status);
//		appendTimeWithSQL(sb, startTime, endTime,terminateFrom, terminateTo);
//		sb.append("order by sub.START_TIME DESC");
//		return super.queryBySql(sb.toString(), offset, limit);
//	}
}
