package com.iliadonline.server.managers;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectMap;
import com.iliadonline.server.IliadNetworkClient;
import com.iliadonline.server.data.DataInterface;
import com.iliadonline.server.data.HsqlDataProvider;
import com.iliadonline.server.objects.GameObject;
import com.iliadonline.server.objects.IliadMap;
import com.iliadonline.shared.network.ByteConverter;
import com.iliadonline.shared.network.Client;
import com.iliadonline.shared.network.ClientListener;
import com.iliadonline.shared.network.Message;

/**
 * ServerGameState acts as the main running loop for the server.
 * 
 * Communication is done through a set of incoming and outgoing ConcurrentLinkedQueues of Messages
 * Each Client to the system will have it's own outgoing queue, and there is a single incoming queue
 * 
 * This allows us to have a networked, multiplayer server, and a local, in instance server
 */
public class ServerGameState implements Runnable
{
	private static final String tag = "com.iliadonline.server.managers.ServerGameState";
	
	protected FileHandle dataDir;
	protected ClientManager clientManager;
	private ConcurrentLinkedQueue<Message> incoming;
	
	protected DataInterface data;
	
	protected ArrayList<IliadMap> maps;
	protected ObjectMap<Client, GameObject> clientObjectMap;	//Maps clients to the GameObject that is their character

	private UUID uuid;
	private long currentTime;
	
	private Message message;
	private boolean running;
	private boolean paused;
	
	private GameObject gameObject;
	
	public ServerGameState(FileHandle dataDir, ConcurrentLinkedQueue<Message> incomingMessages, ClientManager clientManager)
	{
		this.dataDir = dataDir;
		this.incoming = incomingMessages;
		this.clientManager = clientManager;
		
		//Connect to Data
		//Initialize Database
		//Load Data
		//Initialize GameState
		FileHandle dbDir = dataDir.child("db/db");
		this.connectDatabase(dbDir);
		
		uuid = new UUID(Integer.MIN_VALUE);
	}
	
	/**
	 * Helper function that establishes our connection to the database.
	 * TODO: Should initialize the database with proper schema
	 * @param dbDir
	 */
	protected void connectDatabase(FileHandle dbDir)
	{
		if(!dbDir.exists())
		{
			dbDir.mkdirs();
		}
		this.data = new HsqlDataProvider(dbDir.file());
	}
	
	/**
	 * Periodic update of game data
	 */
	public void update()
	{
		currentTime = System.nanoTime();
		
		for(IliadMap map : maps)
		{
			//Run an update on each map, all game objects should be on maps, all game objects should be updated
			map.update(currentTime);
		}
	}
	
	/**
	 * One of the primary worker methods for this class.
	 * Processes the next, single, message in the queue.
	 */
	protected void processMessage()
	{
		if(this.incoming == null || this.incoming.isEmpty())
		{
			return;
		}
			
		message = incoming.poll();
		IliadNetworkClient client = (IliadNetworkClient)message.client;
		
		Gdx.app.log(tag, "Message Received: " + message.toString());
				
		//TODO: Look into a strategy pattern or perhaps event system to clean this up long term
		switch(message.command)
		{
			case 0:	//HeartBeat
				//message.getClient()
				break;
			case 1: //Current location
				IntBuffer locBuffer = ByteBuffer.wrap(message.data).asIntBuffer();
				
				gameObject = clientObjectMap.get(message.client);
				
				try
				{
					gameObject.getLocation().x = locBuffer.get(0);
					gameObject.getLocation().y = locBuffer.get(1);
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
				buffer.putInt(gameObject.getId());
				buffer.putInt((int)gameObject.getLocation().x);
				buffer.putInt((int)gameObject.getLocation().y);
				
				buffer.flip();
				
				clientManager.sendAll(new Message((byte)1, buffer.array(), null));
				break;
		}
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
