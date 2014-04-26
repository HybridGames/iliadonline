package com.iliadonline.client.network;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.iliadonline.shared.network.Message;

public interface ServerInterface
{
	public void sendMessage(Message msg);
	public ConcurrentLinkedQueue<Message> getMessages();
	public void connect();
	
	public boolean isLocal();
}
