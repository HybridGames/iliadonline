package com.iliadonline.server;

/**
 * A set of Static methods that help in access Server methods and data
 *
 */
public final class ServerHelper
{
	private ServerHelper(){}
	
	public static int getNextUUID()
	{
		return ServerMain.instance.uuid.getUUID();
	}
}
