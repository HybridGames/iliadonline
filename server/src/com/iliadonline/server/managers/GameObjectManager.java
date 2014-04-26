package com.iliadonline.server.managers;

import com.badlogic.gdx.physics.box2d.World;
import com.iliadonline.shared.data.GameObject;

/**
 * Manages the lifespan of GameObjects
 * TODO: Need to determine best way to keep a list of GameObjects
 * @author Christopher
 *
 */
public class GameObjectManager
{
	private UUID uuid;
	private GameObject gameObject;
	
	private World world;
	
	public GameObjectManager()
	{
		this.uuid = new UUID(Integer.MIN_VALUE);
	}
	
	public GameObject createGameObject()
	{
		gameObject = new GameObject(this.uuid.getUUID());
		
		return gameObject;
	}
}
