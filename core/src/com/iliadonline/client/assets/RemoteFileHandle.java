package com.iliadonline.client.assets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import com.badlogic.gdx.files.FileHandleStream;

public class RemoteFileHandle extends FileHandleStream
{
	protected ReadableByteChannel byteChannel;
	protected InputStream readStream;
	
	public RemoteFileHandle(String path) throws MalformedURLException, IOException
	{
		super(path);
		
		URL url = new URL(path);
		this.byteChannel = Channels.newChannel(url.openStream());
		this.readStream = Channels.newInputStream(this.byteChannel);
	}

	/**
	 * @see com.badlogic.gdx.files.FileHandleStream#read()
	 */
	@Override
	public InputStream read()
	{
		return this.readStream;
	}

	/**
	 * @see com.badlogic.gdx.files.FileHandleStream#write(boolean)
	 */
	@Override
	public OutputStream write(boolean overwrite)
	{
		// TODO Auto-generated method stub
		return super.write(overwrite);
	}
}
