package com.iliadonline.client.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.client.ClientConfig;

/**
 * A file resolver that will work to keep files updated.
 * First we check if the local file exists, if it's up to date with the current,
 * and updates the file if needed
 */
public class IliadFileResolver implements FileHandleResolver
{
	private static String tag = "com.iliadonline.client.asset.IliadFileResolver";
	
	protected ClientConfig config;
	protected FileHandle assetDir;
	protected FileHandle staticDir;
	
	public IliadFileResolver(ClientConfig config)
	{
		this.config = config;
		
		this.staticDir = config.getStaticAssetFolder();
		this.assetDir = config.getWritableFolder().child("assets");
		
		if(!assetDir.isDirectory())
		{
			assetDir.mkdirs();
		}
		
		Gdx.app.log(tag, assetDir.file().getAbsolutePath());
	}
	
	@Override
	public FileHandle resolve(String fileName)
	{
		Gdx.app.log(tag, "File Resolve: " + fileName);
				
		if(this.staticDir.child(fileName).exists())
		{
			return this.staticDir.child(fileName);
		}
		
		if(this.assetDir.child(fileName).exists())
		{
			return assetDir.child(fileName);
		}
		
		return Gdx.files.internal(fileName);
	}
}
