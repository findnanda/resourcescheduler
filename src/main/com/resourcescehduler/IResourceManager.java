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
	 * Checks and returns a resource from the resource pool if it is is free.
	 * @return Resource
	 */
	public abstract Resource isResourceAvailable();

	/**
	 * This resource pool could be configured in config.properties and the 
	 * value will be initialised at startup time.
	 * @return returns the number of resources available in a resource pool.
	 */
	public abstract int getResourcePool();

}
