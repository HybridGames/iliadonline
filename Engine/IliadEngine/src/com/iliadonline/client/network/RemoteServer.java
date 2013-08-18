package com.iliadonline.client.network;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.iliadonline.shared.network.Message;

public class RemoteServer implements ServerInterface
{
	private int port;
	private ClientNetwork network;
	
	public RemoteServer(int port)
	{
		this.port = port;
	}

	@Override
	public void sendMessage(Message msg)
	{
		this.network.SendMessage(msg);
	}

	@Override
	public ConcurrentLinkedQueue<Message> getMessages()
	{
		return this.network.getMessages();
	}

	@Override
	public void connect()
	{
		network = new ClientNetwork();
		new Thread(network).start();
	}
	
	@Override
	public boolean isLocal()
	{
		return false;
	}

}
