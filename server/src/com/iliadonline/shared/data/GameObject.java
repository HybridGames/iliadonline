package com.iliadonline.shared.data;

import com.iliadonline.shared.components.RenderComponent;

/**
 * Represents a basic entity in our game.
 *
 */
public class GameObject
{
	protected int id;
	protected Location location;
	protected String name = "";
	
	//Optional Components
	protected RenderComponent renderer = null;
	
	public GameObject(int id)
	{
		this.id = id;
		location = new Location();
	}
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the renderer
	 */
	public RenderComponent getRenderer()
	{
		return renderer;
	}

	/**
	 * @param renderer the renderer to set
	 */
	public void setRenderer(RenderComponent renderer)
	{
		this.renderer = renderer;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the location
	 */
	public Location getLocation()
	{
		return location;
	}
	
}
