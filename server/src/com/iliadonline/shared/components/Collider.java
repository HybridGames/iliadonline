package com.iliadonline.shared.components;

public class Collider
{
	public enum ColliderType
	{
		Diamond,
		Cirlce,
		Rectangle,
	}

	public Collider(ColliderType type, float... params)
	{
		
	}
	
	/**
	 * Copy Constructor, designed for use in Prototype Pattern in GameObjectFactory
	 * @param copy
	 */
	public Collider(Collider copy)
	{
		
	}
}
