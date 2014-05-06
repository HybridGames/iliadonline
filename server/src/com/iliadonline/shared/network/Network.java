package com.iliadonline.shared.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Network implements Runnable {
	//Define client buffers to be just large enough to hold the largest possible message
	public static final int BUFFER_SIZE = Protocol.MAX_MESSAGE_SIZE;
	
	private int port;
	
	//Set up a Selector and the Default server channel
	private Selector serverSelector = Selector.open();
	private ServerSocketChannel server = ServerSocketChannel.open();
	
	//In and Out queue
	private ConcurrentLinkedQueue<Message> incomingQueue;
	
	//Listener for Client connections
	private ClientListener netListener;
	
	//Is the network still running, is it paused
	private boolean running = false;
	private boolean pause = false;
	
	//The logger for this class
	private final Logger logger = Logger.getLogger("com.iliadonline.network");
	
	/**
	 * Creates a Network object that will wait for connections
	 * @param port
	 */
	public Network(ClientListener netListener, int port) throws IOException
	{
		this.port = port;
		
		//Assign the in queue
		this.incomingQueue = new ConcurrentLinkedQueue<Message>();
		
		//Assign netListener
		this.netListener = netListener;
		
		logger.setLevel(Level.ALL);
	}

	/**
	 * Implementation of runnable
	 * Moved majority of code to helper methods so we can apply a try block and have a finally for cleaning up
	 */
	@Override
	public void run() {
		
		try
		{
			initializeSocket();
			mainLoop();
		}
		catch (IOException e)
		{
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			deinitializeSocket();
		}
	}
	
	/**
	 * Helper for the run() method that initializes the server socket
	 * @throws IOException
	 */
	private void initializeSocket() throws IOException
	{
		server.configureBlocking(false);
		server.socket().bind(new InetSocketAddress(this.port));
		server.register(serverSelector, SelectionKey.OP_ACCEPT);

		logger.info("Network bound to port " + port);
	}
	
	/**
	 * Helper for the run() method to deinitialize the server socket.
	 * Theoretically this should let the server be run and stopped and restarted in a new thread
	 * TODO: Needs testing
	 */
	private void deinitializeSocket()
	{
		try
		{
			server.close();
		}
		catch (IOException e)
		{
			logger.log(Level.WARNING, "Closing Server Socket threw an IOException");
			e.printStackTrace();
		}
	}
	
	private void mainLoop()
	{
		running = true;
		pause = false;
	
		Set<SelectionKey> readyKeys;
		Iterator<SelectionKey> it;
		SelectionKey key;
		SocketChannel clientSocket;
		
		ByteBuffer tempBuffer;
		
		Client gameClient;
		int bytesRead;
		
		//Variables for writing messages out to the network for clients
		ConcurrentLinkedQueue<Message> msgQueue;
		Message msg;
		
		while(running)
		{
			//A way of holding up network traffic, but without stopping the loop
			if(pause)
			{
				continue;
			}
			
			//Get an iterator over the ready keys
			try {
				serverSelector.select();
			} catch (IOException e1) {
				// TODO Log Error
				e1.printStackTrace();
				
				//Restart our loop, trying to select again
				continue;	//continue chosen as opposed to placing rest of loop inside of Try Block
			}
			
			//Get the keys ready to be worked with
			readyKeys = serverSelector.selectedKeys();
			it = readyKeys.iterator();
			
			while(it.hasNext())
			{
				key = it.next();
				
				//logger.info("Key [" + key.toString() + "] - " + key.readyOps());
								
				//Accept the channel
				if(key.isAcceptable())
				{
					//Server channel should be the only channel accepting connections
					//This check also allows us to skip casting the key.channel to ServerSocketChanel
					if(server == key.channel())
					{
						try 
						{
							clientSocket = server.accept();
							if(clientSocket != null)
							{
								//Log the new client connection
								logger.info("Accepting new client");
								
								clientSocket.configureBlocking(false);
							
								//Getting the client from netListener is also the hook that allows us to store the clients
								gameClient = netListener.newClient(clientSocket);
							
								//Register the client socket with the selector, and attach the networkClient object to it
								clientSocket.register(serverSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, gameClient);
								
								//Log after the client has been registered
								logger.info("Net client registered");
							}
						} 
						catch (IOException e) 
						{
							logger.warning("Error accepting a new channel: " + System.getProperty("line.separator") +  e);
							e.printStackTrace();
						}
					}
				}
				
				//Read from the channel
				if(key.isReadable())
				{
					if(key.attachment() instanceof Client)
					{
						//Get the NetworkClient and the Socket
						gameClient = (Client)key.attachment();
						clientSocket = (SocketChannel)key.channel();

						//Read from the socket to the clients buffer
						try {
							//Read network data
							bytesRead = clientSocket.read(gameClient.getReadBuffer());
							
							//logger.info("bytesRead = " + bytesRead);
							
							//Translate into messages, and push onto message queue
							//TODO check if addAll maintains proper order of messages
							if(bytesRead > 0)
							{
								gameClient.getReadBuffer().flip();
								//FIXME: Need to link Client with the Message
								
								incomingQueue.addAll(Protocol.translate(gameClient.getReadBuffer(), gameClient));
								gameClient.getReadBuffer().compact();	//TODO move compact into Protocol.translate??
							}
							else if(bytesRead < 0)//User no longer connected
							{
								//Logged as info because this may simply mean a user terminated the connection abrubtly
								logger.info("Client connection closed unexpectedly");
								
								closeClient(gameClient, clientSocket, false);
								it.remove();
								continue;
							}
							
						} catch (IOException e) {
							logger.info("Client connection closed unexpectedly");
							
							closeClient(gameClient, clientSocket, false);
							/*
							netListener.closeClient(gameClient, false);
							try
							{
								clientSocket.close();
							} catch (IOException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}*/
							it.remove();
							continue;	//Continue moves on to the next key (it)
						}
					}//if(key.attachment() instanceof Client)
					else
					{
						logger.warning("Attachment is not of type Client");
					}
				}
				
				//Write to the channel
				if(key.isWritable())
				{
					gameClient = (Client)key.attachment();
					clientSocket = (SocketChannel)key.channel();
										
					//Get the outgoing messages and put them in the ByteBuffer
					msgQueue = gameClient.getMessages();
					while(!msgQueue.isEmpty())
					{
						msg = msgQueue.poll();
						tempBuffer = Protocol.createBuffer(msg);		//tempBuffer is here in case any additional work with the buffer must be done
						gameClient.getWriteBuffer().put(tempBuffer);		//removed inlining of Protocol.createBuffer call
					}
					
					try {
						gameClient.getWriteBuffer().flip();
						clientSocket.write(gameClient.getWriteBuffer());
						gameClient.getWriteBuffer().compact();
					} catch (IOException e) {
						// TODO SocketChannel write failure
						e.printStackTrace();
					}
				}
				
				
			}
		}
	}
	
	private void closeClient(Client client, Channel socketChannel, boolean graceful)
	{
		netListener.closeClient(client, graceful);
		try {
			socketChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Simple get for incomingQueue
	 * @return ConcurrentLinkedQueue<Message>
	 */
	public ConcurrentLinkedQueue<Message> getIncomingQueue()
	{
		return this.incomingQueue;
	}
}
