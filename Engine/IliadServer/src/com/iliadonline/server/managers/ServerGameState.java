package com.iliadonline.server.managers;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;
import com.iliadonline.server.IliadNetworkClient;
import com.iliadonline.server.objects.IliadMap;
import com.iliadonline.shared.network.ByteConverter;
import com.iliadonline.shared.network.Client;
import com.iliadonline.shared.network.ClientListener;
import com.iliadonline.shared.network.Message;

public class ServerGameState implements ClientListener, Runnable
{
	private static final String tag = "com.iliadonline.server.managers.ServerGameState";
	
	protected FileHandle dataDir;
	
	protected ArrayList<IliadMap> maps;

	protected ClientManager clientManager = new ClientManager();
	
	private UUID uuid;
	private long currentTime;
	
	private ConcurrentLinkedQueue<Message> incoming;
	private Message message;
	private boolean running;
	private boolean paused;
	
	private Logger logger;
	
	public ServerGameState(FileHandle dataDir)
	{		
		//TODO: With persistence, we need determine the next UUID once items are loaded
		uuid = new UUID(Integer.MIN_VALUE);
		
		logger = new Logger(ServerGameState.tag);
		logger.setLevel(Logger.INFO);
	}
	
	public void setIncoming(ConcurrentLinkedQueue<Message> incoming)
	{
		this.incoming = incoming;
	}
	
	public void update()
	{
		currentTime = System.nanoTime();
		
		for(IliadMap map : maps)
		{
			//Run an update on each map, all game objects should be on maps, all game objects should be updated
			map.update(currentTime);
		}
	}
	
	protected void processMessage()
	{
		if(this.incoming == null)
		{
			return;
		}
		
		if(this.incoming.isEmpty())
		{
			return;
		}
			
		message = incoming.poll();
		IliadNetworkClient client = (IliadNetworkClient)message.client;
		
		//logger.info("Server Message Received: " + message.toString());
				
		//TODO: Look into a strategy pattern to clean this up long term
		switch(message.command)
		{
			case 0:	//HeartBeat
				//message.getClient()
				break;
			case 1: //Current location
				IntBuffer locBuffer = ByteBuffer.wrap(message.data).asIntBuffer();
				
				try
				{
					client.x = locBuffer.get(0);
					client.y = locBuffer.get(1);
				}
				catch (NullPointerException e)
				{
					System.err.println(locBuffer.get(0));
					System.err.println(locBuffer.get(1));
					System.err.println(client);
					e.printStackTrace();
				}
				
				//logger.info("Client Location: " + client.getId() + "(" + client.x + "," + client.y + ")");
				
				ByteBuffer buffer = ByteBuffer.allocate(256);
				buffer.putInt(client.getId());
				buffer.putInt(client.x);
				buffer.putInt(client.y);
				
				buffer.flip();
				
				clientManager.sendAll(new Message((byte)1, buffer.array(), null));
				break;
		}
	}

	@Override
	public Client newClient(SocketChannel socket) 
	{
		IliadNetworkClient client = clientManager.newClient(uuid.getUUID());
		
		Message msg = new Message((byte)-1, ByteConverter.IntToByteArray(client.getId()), client);
		client.sendMessage(msg);
		
		return client;
	}

	@Override
	public void closeClient(Client client, boolean graceful) 
	{
		clientManager.closeClient(client, graceful);
	}

	@Override
	public void run()
	{
		this.running = true;
		this.paused = false;
		
		while(this.running)
		{
			if(this.paused)
			{
				continue;
			}
			
			this.processMessage();
			//this.update();
		}
	}
}
