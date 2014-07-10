package com.iliadonline.client.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.client.ClientConfig;

/**
 * An extension of the ClientConfig that allows us to pass specific data into our application
 */
public class AndroidClientConfig implements ClientConfig
{

	@Override
	public FileHandle getStaticAssetFolder()
	{
		FileHandle dataDir = Gdx.files.internal("data");
		return dataDir;
	}

	@Override
	public FileHandle getWritableFolder()
	{
		FileHandle dataDir = Gdx.files.local("data");
		
		FileHandle gfxDir = dataDir.child("gfx");
		if(!gfxDir.isDirectory())
		{
			//mkdirs will make all directories, so this includes the "data" dir
			gfxDir.mkdirs();
		}
		
		FileHandle spritesDir = gfxDir.child("sprites");
		if(!spritesDir.isDirectory())
		{
			spritesDir.mkdirs();
		}
		
		FileHandle fontsDir = gfxDir.child("fonts");
		if(!fontsDir.isDirectory())
		{
			fontsDir.mkdirs();
		}
		
		return dataDir;
	}

	@Override
	public String getRemoteAddress()
	{
		//return "127.0.0.1";
		return "192.168.1.2";
	}

	@Override
	public int getRemotePort()
	{
		return 5678;
	}

}
