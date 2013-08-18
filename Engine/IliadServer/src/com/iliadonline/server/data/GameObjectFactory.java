package com.iliadonline.server.data;

import java.util.HashMap;

import com.iliadonline.shared.components.Collider;
import com.iliadonline.shared.components.RenderComponent;

/**
 * This Factory creates GameObjects based on Game Object Templates
 * Statically it should hold a list of all available game objects, and allow reference to the individual factory instances
 */
public class GameObjectFactory
{	
	//A list of existing GameObjectFactories that are available for use
	static protected HashMap<String, GameObjectFactory> factories;
			
	private Collider collider;
	private RenderComponent render;
	
	/**
	 * GameObjectFactory instances are created by a static factory method
	 * @param collider
	 * @param render
	 */
	protected GameObjectFactory(Collider collider, RenderComponent render)
	{
		this.collider = collider;
		this.render = render;				
	}
	
	public GameObject buildGameObject(int id)
	{
		GameObject gameObj = new GameObject(id);
		
		if(this.collider != null)
		{
			gameObj.setCollider(new Collider(this.collider));
		}
		
		if(this.render != null)
		{
			gameObj.setRenderer(new RenderComponent(this.render));
		}
		
		return gameObj;
	}

}
