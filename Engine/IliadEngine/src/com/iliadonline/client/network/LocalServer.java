package com.iliadonline.client.network;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.server.managers.ServerGameState;
import com.iliadonline.shared.network.Client;
import com.iliadonline.shared.network.Message;

public class LocalServer implements ServerInterface
{
	private static final String tag = "com.iliadonline.client.network.LocalServer";
	
	private ServerGameState serverState;
	private ConcurrentLinkedQueue<Message> serverMessages;
	
	private Client client;
	
	public LocalServer(FileHandle dataDir)
	{
		serverMessages = new ConcurrentLinkedQueue<Message>();
		serverState = new ServerGameState(dataDir);
		serverState.setIncoming(serverMessages);
		
		Gdx.app.log(tag, "Server Starting");
		
		new Thread(serverState).start();
	}

	@Override
	public void sendMessage(Message msg)
	{
		msg.client = this.client;
		this.serverMessages.add(msg);
	}

	@Override
	public ConcurrentLinkedQueue<Message> getMessages()
	{
		return client.getMessages();
	}
	
	@Override
	public void connect()
	{
		this.client = this.serverState.newClient(null);
	}

	@Override
	public boolean isLocal()
	{
		return true;
	}
}
