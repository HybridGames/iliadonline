package com.iliadonline.client.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.Gdx;
import com.iliadonline.shared.network.Message;
import com.iliadonline.shared.network.Protocol;

/**
 *	Handles the network connection for the client 
 */
public class ClientNetwork implements Runnable 
{
	private static final String tag = "com.iliadonline.client.network.ClientNetwork";
	protected boolean running = false;
	protected ConcurrentLinkedQueue<Message> in, out;
	
	private SocketChannel socket;
	private ByteBuffer buffer = ByteBuffer.allocate(Protocol.MAX_MESSAGE_SIZE);
	
	Message msg;
	
	public ClientNetwork()
	{
		in = new ConcurrentLinkedQueue<Message>();
		out = new ConcurrentLinkedQueue<Message>();
	}
	
	@Override
	public void run() {
		this.running = true;
		
		try {
			socket = SocketChannel.open();
			socket.configureBlocking(false);
			//socket.connect(new InetSocketAddress("hybridgames.gotdns.com", 6789));
			socket.connect(new InetSocketAddress("192.168.1.4", 5678));
			
			Gdx.app.debug(tag, "Waiting to Connect");
			while(!socket.finishConnect())
			{
				//Stall for connecting
				
			}
		} catch (IOException e) {
			Gdx.app.error(tag, "Connecting to Server Failed", e);
		}
		
		Gdx.app.debug(tag, "Connected");
		
		while(this.running)
		{
			try {
				socket.read(buffer);
				buffer.flip();
				in.addAll(Protocol.translate(buffer, null));
				buffer.compact();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			while(!out.isEmpty())
			{
				try {
					msg = out.poll();
					//Gdx.app.log(tag, "Message Being Written: " + msg);
					socket.write(Protocol.createBuffer(msg));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Gdx.app.error(tag, "Unable to write message: " + msg, e);
				}
			}
		}
	}
	
	public boolean hasMessages()
	{
		return !in.isEmpty();
	}
	
	/**
	 * Returns the next message, null if empty.
	 * Designed to be called by a thread other than the one running the network.
	 * @return Message|null
	 */
	public Message getNextMessage()
	{
		return in.poll();
	}
	
	/**
	 * Gets the linked list of messages
	 * @return ConcurrentLinkedQueue<Message>
	 */
	public ConcurrentLinkedQueue<Message> getMessages()
	{
		return this.in;
	}
	
	/**
	 * Adds a message to the queue to be sent
	 * Designed to be called by a thread other than the one running the network.
	 * @param msg
	 */
	public void SendMessage(Message msg)
	{
		this.out.add(msg);
	}
	
	/**
	 * Provides an internal mechanism to stop this Runnable.
	 */
	public void stop()
	{
		//Loop control variable used in run()
		this.running = false;
	}
}
