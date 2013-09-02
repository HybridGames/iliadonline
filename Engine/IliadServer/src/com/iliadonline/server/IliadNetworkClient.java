package com.iliadonline.server;

import com.iliadonline.shared.network.Client;

/**
 * Iliad's Implementation of a Networked Client
 * @author Christopher
 *
 */
public class IliadNetworkClient extends Client
{
	protected long lastUpdate;
	
	public IliadNetworkClient()
	{
	}

	/**
	 * A simple way of updating the client and tracking the last update
	 * 
	 * @param currentTime
	 */
	public void ping(long currentTime)
	{
		this.lastUpdate = currentTime;
	}
}
