package com.iliadonline.shared.data;

/**
 * Holds a game object's location.
 * 
 * @author Christopher
 *
 */
public class Location
{
	//Maps held with location
	protected int map;
	
	/**
	 * X, Y are Float because of the need to track change over small periods of time.
	 * Float to Int conversion will drop decimal data, items may never move in a positive direction
	 */
	protected float x, y;
	
	public Location()
	{
		this(0,0,0);
	}
	
	public Location(int map, float x, float y)
	{
		this.map = map;
		this.x = x;
		this.y = y;
	}
	
	public int getMap()
	{
		return map;
	}

	public void setMap(int map)
	{
		this.map = map;
	}

	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
	}
	
	public void incrementX(float shift)
	{
		this.x += shift;
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
	}
	
	public void incrementY(float shift)
	{
		this.y += shift;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String str = "[" + this.x + ", " + this.y + "]";
		return str;
	}
}
