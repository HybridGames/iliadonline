package com.iliadonline.shared.network;

import java.util.logging.Logger;

/**
 * A network Message Consists of a Command, Data and Client The Client is not
 * transmitted by the network, only used on server side to associate a message
 * with the client
 * 
 */
public class Message
{

	// private static logger
	@SuppressWarnings("unused")
	private static Logger logger;

	static
	{
		Message.logger = Logger.getLogger("net.hybridgames.netserver.Message");
	}

	// possible command values -128 to 127
	public byte command;
	public byte[] data;
	public Client client;

	/**
	 * Returns the Minimum value for command.
	 * 
	 * @return int
	 */
	public static int getCommandMin()
	{
		return Byte.MIN_VALUE;
	}

	/**
	 * Returns the Maximum value for command.
	 * 
	 * @return int
	 */
	public static int getCommandMax()
	{
		return Byte.MAX_VALUE;
	}

	/**
	 * @param command
	 *            - The type of this message, or Command for this message
	 * @param data
	 *            - The data associated with this message
	 * @param client
	 *            - The client associated with this
	 */
	public Message(final byte command, final byte[] data, Client client)
	{
		// logger.info("New Message Being Created - Command: " + command +
		// " Data: " + data);

		this.command = command;
		this.data = data;
		this.client = client;
	}

	/**
	 * Creates a message with an empty data and client
	 * 
	 * @param command
	 */
	public Message(final byte command)
	{
		this(command, new byte[0], null);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param msg
	 */
	public Message(Message msg)
	{
		this(msg.command, msg.data, msg.client);
	}

	/**
	 * An override for Object.toString() Provides the Message command and
	 * address for the byte array of data
	 */
	@Override
	public String toString()
	{
		return "Message(" + this.command + ") - " + this.data;
	}
}
