package com.iliadonline.client;

import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.client.controller.ControllerActionEnum;

/**
 * Configuration Object to establish settings and data needed for the IliadClient to be independent of the Launchers
 */
public class ClientConfig
{
	protected FileHandle staticAssetFolder;
	protected FileHandle writableAssetFolder;
	
	/**
	 * Constructor determines required fields for Config
	 * @param staticAssetFolder
	 * @param writableAssetFolder
	 */
	public ClientConfig(FileHandle staticAssetFolder, FileHandle writableAssetFolder)
	{
		this.staticAssetFolder = staticAssetFolder;
		this.writableAssetFolder = writableAssetFolder;
	}

	/**
	 * Static folder, assets that are not meant to be overwritten
	 * @return
	 */
	public FileHandle getStaticAssetFolder()
	{
		return staticAssetFolder;
	}

	/**
	 * Writable folder, for updatable and dynamic assets
	 * @return
	 */
	public FileHandle getWritableAssetFolder()
	{
		return writableAssetFolder;
	}
}
