package com.iliadonline.server.managers;

import java.util.ArrayList;

import com.iliadonline.shared.network.Client;
import com.iliadonline.shared.network.Message;

/**
 * A support class for the ServerGameState
 * Holds the clients, and provides some basic functionality.
 */
public class ClientManager
{
	protected ArrayList<Client> clients;

	/**
	 * Defaulted Constructor
	 */
	public ClientManager()
	{
		this(200);
	}
	
	/**
	 * Constructor
	 */
	public ClientManager(int maxClients)
	{
		this.clients = new ArrayList<Client>(maxClients);
	}

	/**
	 * Simple Add
	 * 
	 * @param client
	 */
	public void addClient(Client client)
	{
		clients.add(client);
	}

	/**
	 * Simple Remove
	 * 
	 * @param client
	 */
	public void removeClient(Client client)
	{
		clients.remove(client);
	}

	/**
	 * Sends a message to all clients
	 * 
	 * @param msg
	 */
	public void sendAll(Message msg)
	{
		for(Client client : clients)
		{
			client.sendMessage(msg);
		}
	}
}
