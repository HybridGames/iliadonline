package com.iliadonline.shared.data;

/**
 * Holds a game object's location.
 * 
 * @author Christopher
 *
 */
public class Location
{
	//Maps held with location, Maps containing game objects is for performance not data consistency - CL
	public int map;
	
	/**
	 * X, Y are Float because of the need to track change over small periods of time.
	 * Float to Int conversion will drop decimal data, items may never move in a positive direction
	 */
	public float x, y;
	
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
	
	/**
	 * Creates a new Location as a Translation of the Subject from the Origin
	 * Map will be set to that of the subject
	 * @param origin
	 * @param subject
	 */
	public Location(Location origin, Location subject)
	{
		//We may not care about different maps, left the code here in case we do
		/*if(this.map != other.map)
		{
			throw new IllegalArgumentException("Locations must be on the same Map");
		}*/
		
		this.map = subject.map;
		this.x = subject.x - origin.x;
		this.y = subject.y - origin.y;
	}
	
	/**
	 * TODO: a hold over of not having Matrix translations on our rendering
	 * Sets the X,Y data for this location based on the subject's relative position to the center
	 * @param center
	 * @param subject
	 */
	public void setRelativeLocation(Location center, Location subject)
	{
		this.x = (subject.x - center.x);		
		this.y = (subject.y - center.y);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		//TODO: Use String Builder? 
		String str = "[" + this.x + ", " + this.y + "]";
		return str;
	}
}
