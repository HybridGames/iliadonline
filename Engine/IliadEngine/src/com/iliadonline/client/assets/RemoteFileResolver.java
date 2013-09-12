package com.iliadonline.client.assets;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

/**
 * A FileResolver that works like a normal resolver,
 * but will fail back to a remote file source if a local file isn't found.
 */
public class RemoteFileResolver implements FileHandleResolver
{
	private static final String tag = "com.iliadonline.client.assets.RemoteFileResolver";
	
	protected FileHandle dataDir;
	
	public RemoteFileResolver(FileHandle dataDir)
	{
		if(!dataDir.file().canWrite())
		{
			throw new IllegalArgumentException("dataDir must be writable.");
		}
		
		if(!dataDir.isDirectory())
		{
			throw new IllegalArgumentException("dataDir must be a directory.");
		}
		
		this.dataDir = dataDir;
	}

	/**
	 * The resolve method that does most of the work for this class.
	 * Will return a FileHandle, after determining if the file is available locally or needs to be retrieved
	 */
	public FileHandle resolve(String fileName)
	{
		//TODO: Need some way to clean the added file information, it's giving a fully qualified path
		Gdx.app.debug(tag, "FileName: " + fileName);
		
		String pathMask = dataDir.file().getAbsolutePath();
		String newChild = fileName.replace(pathMask, "");
		
		Gdx.app.log(tag, "pathMask: " + pathMask);
		Gdx.app.log(tag, "FileName relative path: " + newChild);
		
		FileHandle child = dataDir.child(newChild);
		if(!child.exists())
		{
			RemoteFileHandle remoteFile;
			try
			{
				//newChild = newChild.replaceFirst("/", "");
				Gdx.app.log(tag, "http://hybridgames.net/iliad/assets/" + newChild);
				remoteFile = new RemoteFileHandle("http://hybridgames.net/iliad/assets/" + newChild);
				return remoteFile;
			}
			catch (MalformedURLException e)
			{
				return null;
			}
			catch (IOException e)
			{
				return null;
			}
		}
		
		//TODO: Following Logic
		//Check if the file exists locally (DataDir)
		//Check if local copy is same as remote copy
		//If it is, return that handle
		//If it isn't return the remote file handle
		
		try
		{
			return child;
		}
		catch(Exception e)
		{
			
		}
		
		return null;
	}
}
