package com.iliadonline.server.managers;

import java.util.ArrayList;

import com.iliadonline.server.IliadNetworkClient;
import com.iliadonline.server.ServerHelper;
import com.iliadonline.shared.network.Client;
import com.iliadonline.shared.network.Message;

public class ClientManager {
	protected ArrayList<IliadNetworkClient> clients;
	
	public ClientManager()
	{
		this.clients = new ArrayList<IliadNetworkClient>();
	}
	
	/**
	 * Creates a new client
	 * @return
	 */
	public IliadNetworkClient newClient(int uuid)
	{
		IliadNetworkClient client = new IliadNetworkClient(uuid);
		clients.add(client);
		return client;
	}
	
	/**
	 * Removes a client from the currently active list
	 * @param client
	 * @param graceful
	 */
	public void closeClient(Client client, boolean graceful)
	{
		clients.remove(client);
	}
	
	/**
	 * Sends a message to all clients
	 * @param msg
	 */
	public void sendAll(Message msg)
	{
		for(IliadNetworkClient client : clients)
		{
			client.sendMessage(msg);
		}
	}
}
