package com.iliadonline.server.managers;

/**
 * Class designed to produce Unique IDs.
 * Current implementation uses a simple autoincrement int
 * 2Billion+ possible numbers
 * @author Christopher
 *
 */
public class UUID
{
	protected int nextId;
	
	/**
	 * creates a new UUID
	 * startId is the next ID that should be given out, not that last one used.
	 * @param startId
	 */
	public UUID(int startId)
	{
		this.nextId = startId;
	}
	
	/**
	 * Return the next usable ID and increment that counter
	 * @return
	 */
	public int getUUID()
	{
		return this.nextId++;
	}
}
