package com.iliadonline.shared.data;

import java.util.HashMap;

import com.iliadonline.shared.components.RenderComponent;

/**
 * Sections of the game are divided into individual maps.
 * A town, inside a house, caves, etc.
 */
public class IliadMap
{
	protected int id;
	private HashMap<Integer, GameObject> gameObjects;
	protected RenderComponent render;
		
	public IliadMap(int id)
	{
		this.id = id;
		gameObjects = new HashMap<Integer, GameObject>();
	}
	
	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Adds a GameObject to this map
	 * @param gameObject
	 */
	public void addGameObject(GameObject gameObject)
	{
		if(!this.gameObjects.containsKey(gameObject.getId()))
		{
			this.gameObjects.put(gameObject.getId(), gameObject);
			gameObject.getLocation().map = this.getId();
		}
	}
	
	/**
	 * Removes a GameObject from this Map
	 * @param gameObject
	 */
	public void removeGameObject(GameObject gameObject)
	{
		this.gameObjects.remove(gameObject);
	}
	
	public HashMap<Integer, GameObject> getGameObjects()
	{
		return this.gameObjects;
	}
}
