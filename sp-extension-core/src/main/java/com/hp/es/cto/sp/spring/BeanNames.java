package com.hp.es.cto.sp.spring;

/**
 * Constant class includes all bean names defined in spring context.
 * 
 * @author Dream
 * 
 */
@Deprecated
public interface BeanNames {
    // Key management DAO bean names
    public static final String USER_KEY_DAO = "userKeyDao";
    public static final String PROVIDER_KEY_DAO = "providerKeyDao";
    public static final String PROVIDER_REGION_DAO = "providerRegionDao";

    // Key management service bean names
    public static final String USER_KEY_SERVICE = "userKeyService";
    public static final String PROVIDER_KEY_SERVICE = "providerKeyService";
    public static final String PROVIDER_REGION_SERVICE = "providerRegionService";

    // Group management DAO bean names
    public static final String ORGANIZATION_DAO = "organizationDao";
    public static final String GROUP_DAO = "groupDao";
    public static final String GROUP_MAPPING_DAO = "groupMappingDao";
    public static final String PROJECT_DAO = "projectDao";
    public static final String PROJECT_LDAP_MAPPING_DAO = "projectLdapMappingDao";

    // Group management service bean names
    public static final String ORGANIZATION_SERVICE = "organizationService";
    public static final String GROUP_SERVICE = "groupService";
    public static final String GROUP_MAPPING_SERVICE = "groupMappingService";
    public static final String LDAP_QUERY_SERVICE = "ldapQueryService";
    public static final String PROJECT_SERVICE = "projectService";
    public static final String PROJECT_LDAP_MAPPING_SERVICE = "projectLdapMappingService";

    // Context management DAO bean names
    public static final String PROVIDER_SERVER_DAO = "providerServerDao";
    public static final String CONTEXT_METE_DATA_DAO = "contextMetaDataDao";
//    public static final String CONTEXT_NODE_DAO = "contextNodeDao";
    public static final String CONTEXT_NODE_DAO_MSSQL = "contextNodeDaoMssql";
    public static final String CONTEXT_NODE_DAO_POSTGRES = "contextNodeDaoPostgre";
    public static final String CONTEXT_NODE_LDAP_MAPPING_DAO = "contextNodeLdapMappingDao";
    public static final String CONTEXT_NODE_PROVIDER_MAPPING_DAO = "contextNodeProviderMappingDao";

    // Context management service bean names
    public static final String PROVIDER_SERVER_SERVICE = "providerServerService";
    public static final String PROVIDER_SHARED_RESOURCE_SERVICE = "providerSharedResourceService";
    public static final String CONTEXT_METE_DATA_SERVICE = "contextMetaDataService";
    public static final String CONTEXT_NODE_SERVICE = "contextNodeService";
    public static final String CONTEXT_NODE_LDAP_MAPPING_SERVICE = "contextNodeLdapMappingService";
    public static final String CONTEXT_NODE_PROVIDER_MAPPING_SERVICE = "contextNodeProviderMappingService";

    // Subscription management service bean names
    public static final String SUBSCRIPTION_SERVER_SERVICE = "subscriptionServerService";
    public static final String SUBSCRIPTION_SERVICE = "subscriptionService";
    public static final String SUBSCRIPTION_LOG_SERVICE = "subscriptionLogService";
    public static final String SUBSCRIPTION_PROPERTY_SERVICE = "subscriptionPropertyService";
    public static final String SUBSCRIPTION_SERVER_PRODUCT_SERVICE = "subscriptionServerProductService";
    public static final String PRODUCT_VERSION_SERVICE = "productVersionService";
    
    //Provider management service bean names
    public static final String PROVIDER_TYPE_SERVICE = "providerTypeService";
    public static final String PROVIDER_SIZE_SERVICE = "providerSizeService";
    public static final String PROVIDER_REGION_AZ_SERVICE = "providerRegionAzService";
    public static final String DESIGN_PROVIDER_SIZE_SERVICE = "designProviderSizeMappingService";
    public static final String ORG_PROVIDER_SIZE_SERVICE = "orgProviderSizeMappingService";
    public static final String ORG_PROVIDER_REGION_SERVICE = "orgProviderRegionMappingService";
    public static final String ORG_PROVIDER_TYPE_SERVICE = "orgProviderTypeMappingService";
}
