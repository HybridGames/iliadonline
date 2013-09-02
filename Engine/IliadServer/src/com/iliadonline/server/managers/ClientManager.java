package com.iliadonline.server.managers;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import com.iliadonline.server.IliadNetworkClient;
import com.iliadonline.server.ServerHelper;
import com.iliadonline.shared.network.Client;
import com.iliadonline.shared.network.ClientListener;
import com.iliadonline.shared.network.Message;

public class ClientManager implements ClientListener
{
	protected ArrayList<IliadNetworkClient> clients;

	/**
	 * Default Constructor
	 */
	public ClientManager()
	{
		this.clients = new ArrayList<IliadNetworkClient>();
	}

	/**
	 * newClient from ClientListener
	 */
	@Override
	public Client newClient(SocketChannel socket)
	{
		return new IliadNetworkClient();
	}

	/**
	 * Removes a client from the currently active list
	 * 
	 * @param client
	 * @param graceful
	 */
	public void closeClient(Client client, boolean graceful)
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
		for (IliadNetworkClient client : clients)
		{
			client.sendMessage(msg);
		}
	}
}
