package com.iliadonline.client.network;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.iliadonline.shared.network.Message;

public class RemoteServer implements ServerInterface
{
	protected String address;
	protected int port;
	protected ClientNetwork network;
	
	public RemoteServer(String address, int port)
	{
		this.port = port;
		this.address = address;
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
		network = new ClientNetwork(this.address, this.port);
		new Thread(network).start();
	}
	
	@Override
	public boolean isLocal()
	{
		return false;
	}

}
