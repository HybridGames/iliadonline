package com.iliadonline.server;

import com.iliadonline.shared.network.Client;

public class IliadNetworkClient extends Client {
	
	protected int id;
	protected long lastUpdate;
	
	public int x;
	public int y;
	
	public IliadNetworkClient(int id)
	{
		this.id = id;
	}
	
	/**
	 * A simple way of updating the client and tracking the last update
	 * @param currentTime
	 */
	public void ping(long currentTime)
	{
		this.lastUpdate = currentTime;
	}
	
	/**
	 * Gets the id for this client
	 * @return int
	 */
	public int getId()
	{
		return this.id;
	}
}
