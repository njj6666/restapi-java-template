/* $Id: TimeLimitedCache.java,v 1.1 2005/01/07 14:19:27 qmay Exp $ */

package com.hp.es.cto.sp.cache;

/**
 *
 *
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public interface TimeLimitedCache extends Cache
{
	public void setTimeLimit(long limit);
	public long getTimeLimit();
}
