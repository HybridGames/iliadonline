package com.iliadonline.client;

import com.badlogic.gdx.files.FileHandle;

/**
 * Configuration Object to establish settings and data needed for the IliadClient to be independent of the Launchers
 */
public interface ClientConfig
{
	public FileHandle getStaticAssetFolder();
	public FileHandle getWritableAssetFolder();	
}
