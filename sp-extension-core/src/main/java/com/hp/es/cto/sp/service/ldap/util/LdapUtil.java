/*************************************************************************     
 *******************                                                           
 * HP CONFIDENTIAL *                                                           
 *******************                                                           
 *                                                                             
 * [2007] - [2013] Hewlett Packard Corporation                                 
 *                                                                             
 * All Rights Reserved.                                                        
 *                                                                             
 * NOTICE:                                                                     
 *     All information contained herein is, and remains the property of        
 * Hewlett Packard Corporation and its suppliers, if any.  The intellectual    
 * and technical concepts contained herein are proprietary and confidential to 
 * Hewlett Packard Corporation and its suppliers and may be covered by U.S. and
 * Foreign Patents, patents in process, and are protected by trade secret or   
 * copyright law. Dissemination of this information or reproduction of this    
 * material is strictly forbidden unless prior written permission is obtained  
 * from Hewlett Packard Corporation.                                           
 *                                                                             
 */
package com.hp.es.cto.sp.service.ldap.util;

import java.util.ArrayList;
import java.util.List;

import com.hp.es.cto.sp.util.StringUtil;

/**
 * Utility class for string processing.
 * 
 * @author Victor
 * 
 */
public class LdapUtil {
	
	private static final String EMPTY_STRING = ""; 

	
	/**
	 * get the parent group of the current ldap dn
	 * (Do trim before checking).
	 * 
	 * @param ldapDn
	 *            ldapDn to get parent
	 * @return
	 *         the parent of ldapDn
	 */
	public static String getParentDn(String ldapDn) {
		if (StringUtil.isNullOrEmpty(ldapDn)) {
			return EMPTY_STRING;
		}
		
		if(!ldapDn.contains(",")){
			return EMPTY_STRING;
		}
		
		int firstIndex = ldapDn.indexOf(",");
		return ldapDn.substring(firstIndex+1);		
	}
	
	/**
	 * get all the parent group of the current ldap dn
	 * (Do trim before checking).
	 * 
	 * @param ldapDn
	 *            ldapDn  to get parent
	 * @return
	 *         the parent list of ldapDn
	 */
	public static List<String> getParentsDn(String ldapDn) {
		List<String> parentsList = new ArrayList<String>();
		
		if (StringUtil.isNullOrEmpty(ldapDn)) {
			return parentsList;
		}
		
		if(!ldapDn.contains(",")){
			return parentsList;
		}
		
		do{
			int firstIndex = ldapDn.indexOf(",");
			String parentDn = ldapDn.substring(firstIndex+1);
			if(!parentsList.contains(parentDn)){
				parentsList.add(parentDn);
			}
			ldapDn = parentDn;
		}while(ldapDn.contains(","));
		
		return parentsList;
	}
	
	/**
	 * Checks whether the candidate dn is parent of ldapdn
	 * (Do trim before checking).
	 * 
	 * @param ldapDn
	 *            ldapDn to get parent
	 * @param parentCandidateDn
	 *            ldapDn for check
	 * @return
	 *         
	 */
	public static boolean isParentDn(String ldapDn, String parentCandidateDn) {
		if (StringUtil.isNullOrEmpty(parentCandidateDn)) {
			return false;
		}
		
		if (StringUtil.isNullOrEmpty(ldapDn)) {
			return false;
		}
		List<String> parentsList = getParentsDn(ldapDn);
		return parentsList.contains(parentCandidateDn.trim());
	}
	
	/**
	 * Add the Base Dn to Ldap Dn to make full dn
	 * 
	 * @param ldapDn
	 *            ldapDn to get parent
	 * @param baseDn
	 *           baseDN to add
	 * @return
	 *        full dn
	 */
	public static String addBaseDn(String ldapDn, String baseDn) {
		if (StringUtil.isNullOrEmpty(ldapDn)) {
			return ldapDn;
		}
		
		if (StringUtil.isNullOrEmpty(baseDn)) {
			return ldapDn;
		}
		
		StringBuffer fulldn = new StringBuffer();
		fulldn.append(ldapDn);
		fulldn.append(",");
		fulldn.append(baseDn);
		return fulldn.toString();
	}


}
