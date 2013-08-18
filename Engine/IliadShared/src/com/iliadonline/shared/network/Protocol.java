package com.iliadonline.shared.network;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Protocol exposes a set of static methods for converting between Messages and ByteBuffers.<br />
 */

public final class Protocol {
	private static final byte MESSAGE_HEADER_COMMAND = 1;
	private static final byte MESSAGE_HEADER_LENGTH = 2;
	public static final int MAX_MESSAGE_SIZE = 65031;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger("net.hybridgames.netserver.Protocol");
	
	/**
	 *
	 */
	public Protocol()
	{
		//TODO: shifting away from Singleton
	}
	
	/**
	 * Creates a ByteBuffer from a Message object.<br />
	 * ByteBuffer is flipped at end, so that position = 0 and limit = the end of the message<br />
	 * @param Message message - The Message to turn into a ByteBuffer
	 * @return ByteBuffer
	 */
	public static ByteBuffer createBuffer(final Message message)
	{
		byte command;
		byte[] lengthBytes = new byte[MESSAGE_HEADER_LENGTH];
		byte[] data;
		int length;
		
		ByteBuffer result;
		
		command = message.command;
		data = message.data;
		length = data.length;
		
		lengthBytes = ByteConverter.UnsignedShortToByteArray(length);
		
		//Allocate a ByteBuffer that is just as large as we need.
		result = ByteBuffer.allocate(MESSAGE_HEADER_COMMAND + MESSAGE_HEADER_LENGTH + length);
		
		result.clear();
		result.put(command);
		result.put(lengthBytes);
		result.put(data);
		
		result.flip();
		
		//Return the result ByteBuffer in a ReadOnly format.
		return result.asReadOnlyBuffer();
	}
		
	/**
	 * Creates a Collection<Message> of Messages, taking into account that an incomplete message may be in the buffer.<br />
	 * Will remove all whole messages and leave the remainder compacted to the beginning(except for mark) if incomplete message in buffer.<br />
	 * Assumes the buffer is ready to be read, Position = 0, Limit = Last relevant byte.<br />
	 * @param buffer - ByteBuffer of bytes to be translated, Assumes the buffer is ready to be read
	 */
	public static Collection<Message> translate(final ByteBuffer buffer, Client client)
	{
		//logger.info("Beginning Translate");
			
		byte command;
		byte[] lengthBytes = new byte[MESSAGE_HEADER_LENGTH];
		byte[] data;
		int length;
		
		Message msg;
		
		boolean processing = true;
		
		Collection<Message> result = new LinkedList<Message>();
		
		while(processing)
		{
			//Switch processing flag to false, exit loop
			if(!buffer.hasRemaining())
			{
				processing = false;
				break;
			}
			
			//Mark where we started, in case we reset
			buffer.mark();
			
			//Get the command of the message
			command = buffer.get();
			
			//Instead of checking for a remaining of 3 or more, two if blocks are used to allow for the potential of having certain commands be known to have no data
			//Check command to see if it has a fixed length
			//Else, get the length
			
			if(buffer.remaining() >= 2)
			{
				buffer.get(lengthBytes);
				
				//Convert length byte array to length value
				length = ByteConverter.ByteArrayToUnsignedShort(lengthBytes);
				
				if(buffer.remaining() >= length)
				{
					//read off the rest of the message
					data = new byte[length];
					
					buffer.get(data);
					
					//Add message to the result collection
					msg = new Message(command, data, client);
					//logger.info("New Message: " + msg);
					result.add(msg);
					
					//Compact the buffer (remove the message) 
					buffer.compact();
					
					//Flip the buffer, prepare it for reading
					buffer.flip();
				}
				else
				{
					//buffer does not contain a complete message, reset it
					buffer.reset();
					processing = false;
				}
			}
			else
			{
				//buffer does not contain a complete message, reset it
				buffer.reset();
				processing = false;
			}
		}
		
		//logger.info("Finishing Translate: " + result.size() + " messages translated.");
				
		return result;
	}
}
