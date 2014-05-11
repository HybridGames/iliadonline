package com.iliadonline.client.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.client.ClientConfig;

public class DesktopClientConfig implements ClientConfig
{

	@Override
	public FileHandle getStaticAssetFolder()
	{
		FileHandle dataDir = Gdx.files.internal("./bin/data/");
		return dataDir;
	}

	@Override
	public FileHandle getWritableAssetFolder()
	{
		FileHandle dataDir = Gdx.files.internal("./bin/data/");
		return dataDir;
	}

}
