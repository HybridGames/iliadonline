package com.iliadonline.shared.components;

import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Renderer Component.
 * Holds all the data that a game object would need to render.
 * @author Christopher
 *
 */
public class RenderComponent
{
	protected Color tint;
	public Queue<Sprite> sprites;

	public RenderComponent()
	{
		
	}
	
	public Queue<Sprite> getSprites()
	{
		return this.sprites;
	}

	/**
	 * Copy Constructor, designed for use in Prototype Pattern in GameObjectFactory
	 * @param copy
	 */
	public RenderComponent(RenderComponent copy)
	{
		this.sprites.addAll(copy.sprites);
	}
	
}
