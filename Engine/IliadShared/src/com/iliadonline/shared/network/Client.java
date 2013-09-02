package com.iliadonline.shared.network;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A Basic Client
 * Holds a list of Messages to be sent and messages received, as well read and write buffers to be used for holding partial messages.
 * Helpful for networking, where a message may be sent in several packets.
 */
public class Client
{
	// read is data coming in from the network, write is going out
	private ByteBuffer read = ByteBuffer.allocate(Network.BUFFER_SIZE);
	private ByteBuffer write = ByteBuffer.allocate(Network.BUFFER_SIZE);

	private ConcurrentLinkedQueue<Message> msgQueue = new ConcurrentLinkedQueue<Message>();

	/**
	 * Get the ByteBuffer for incoming data
	 * 
	 * @return ByteBuffer
	 */
	public ByteBuffer getReadBuffer()
	{
		return read;
	}

	/**
	 * Get the ByteBuffer for writing out to the network
	 * 
	 * @return ByteBuffer
	 */
	public ByteBuffer getWriteBuffer()
	{
		return write;
	}

	/**
	 * Places a message onto the Queue to be sent for this client
	 * 
	 * @param msg
	 */
	public void sendMessage(Message msg)
	{
		// Using a ConcurrentLinkedQueue for messages allows us to provide
		// thread safety
		// over having our sending thread writing into a ByteBuffer designed to
		// be read by the network thread
		msgQueue.add(msg);
	}

	/**
	 * Gets the message queue for outgoing messages
	 * 
	 * @return ConcurrentLinkedQueue<Message>
	 */
	public ConcurrentLinkedQueue<Message> getMessages()
	{
		return msgQueue;
	}
}
