package com.iliadonline.client.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.client.ClientConfig;

public class AndroidClientConfig implements ClientConfig
{

	@Override
	public FileHandle getStaticAssetFolder()
	{
		FileHandle dataDir = Gdx.files.internal("data");
		return dataDir;
	}

	@Override
	public FileHandle getWritableAssetFolder()
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

}
