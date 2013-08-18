package com.iliadonline.server.data;

import com.iliadonline.shared.components.Collider;

public class GameObject extends com.iliadonline.shared.data.GameObject
{

	protected Collider collider = null;
	
	protected long lastUpdate;
	private long timeDiff;
	
	public GameObject(int id)
	{
		super(id);
	}

	/**
	 * Meant to allow updates to game objects
	 * Should interface with scripting, still needs to be implemented
	 * @param timeStamp
	 */
	public void update(long timeStamp)
	{
		timeDiff = timeStamp - lastUpdate;
		
		//TODO: Interface to Script
		
		lastUpdate = timeStamp;
	}
	
	public void setCollider(Collider collider)
	{
		this.collider = collider;
	}
}
