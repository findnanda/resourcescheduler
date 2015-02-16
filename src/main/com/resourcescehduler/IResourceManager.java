package com.resourcescehduler;

import com.resourcescehduler.vo.Resource;

/**
 * @author NYalamanchili
 *
 */
public interface IResourceManager {

	/**
	 * @param msgId
	 */
	public abstract void releaseResource(int msgId);

	/**
	 * @return Resource
	 */
	public abstract Resource isResourceAvailable();

}
