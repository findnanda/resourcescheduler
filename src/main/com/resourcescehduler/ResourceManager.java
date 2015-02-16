package com.resourcescehduler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.resourcescehduler.vo.Resource;

/**
 * @author NYalamanchili
 * 
 */
public class ResourceManager implements IResourceManager {

	private static Log logger = LogFactory.getLog(ResourceManager.class);
	private int resourcePool;
	private List<Resource> resourceList = new ArrayList<Resource>();



	/* (non-Javadoc)
	 * @see com.resourcescehduler.IResourceManager#isResourceAvailable()
	 */
	// get the resource if free
	@Override
	public Resource isResourceAvailable() {
		return allocateResource();
	}

	// allocate resource based on availability
	private Resource allocateResource() {
		Resource resource = null;
		if (resourceList.size() < resourcePool) {
			resource = new Resource();
			resource.setStatus(Resource.PREPARE);
			resource.setResourceId(resourceList.size() + 1);
			synchronized (this) {
				if (resourceList.size() < resourcePool) {
					resourceList.add(resource);
				}
				logger.debug("allocated new Resource ");
			}
		} else {
			for (Resource resource1 : resourceList) {
				synchronized (this) {
					if (resource1.getStatus() == Resource.FREE) {
						resource1.setStatus(Resource.PREPARE);
						resource = resource1;
						break;
					}
				}
			}
		}
		return resource;
	}

	/* (non-Javadoc)
	 * @see com.resourcescehduler.IResourceManager#releaseResource(int)
	 */
	@Override
	public void releaseResource(int msgId) {
		for (Resource resource : resourceList) {
			if (resource.getMsgId() == msgId) {
				resource.resetResource();
				logger.debug("released resource with msg id = "
						+ resource.getMsgId());
				break;
			}
		}

	}

	public int getResourcePool() {
		return resourcePool;
	}

	public void setResourcePool(int resourcePool) {
		this.resourcePool = resourcePool;
	}

}
