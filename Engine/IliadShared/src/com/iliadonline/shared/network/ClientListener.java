package com.iliadonline.shared.network;

import java.nio.channels.SocketChannel;

/**
 * A listener interface that listens to the network for new clients and closed connections.
 *
 */

public interface ClientListener 
{
	/**
	 * A new socket is ready to be Accepted, request a new Client object to attach to the socket.<br />
	 * V1.2 Passes the SocketChannel object into the newClient call. This allows hooking into the basic sockets if desired.<br />
	 * @param SocketChannel - The socket that is connecting. Provides access to basic socket information.
	 * @return
	 */
	public Client newClient(SocketChannel socket);
	
	/**
	 * A socket has been disconnected.
	 * @param client Which client was attached to the key that disconnected
	 * @param graceful Was the disconnect expected or unexpected
	 */
	public void closeClient(Client client, boolean graceful);
}
