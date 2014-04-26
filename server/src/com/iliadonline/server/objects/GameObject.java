package com.iliadonline.server.objects;

import com.iliadonline.shared.components.Collider;
import com.iliadonline.shared.data.Location;

public class GameObject extends com.iliadonline.shared.data.GameObject
{
	//TODO: Will all GameObjects have location?
	protected Location location = new Location();

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
	
	/**
	 * Gets the location
	 */
	public Location getLocation()
	{
		return this.location;
	}
}
