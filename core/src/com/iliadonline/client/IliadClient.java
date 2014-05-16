package com.iliadonline.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.client.assets.RemoteFileResolver;
import com.iliadonline.client.render.Render;

/**
 * This is our main Application class in the LibGDX setup.
 * Here we're working to establish basic configuration data and build objects and threads that will manage the rest of the game.
 * 
 * @todo Game crashes if a controller isn't setup
 */
public class IliadClient implements ApplicationListener 
{
	private static final String tag = "com.iliadonline.client.IliadClient";
	
	protected ClientConfig config;
	protected IliadController controller;
	
	protected Render render;
	protected GameState gameState;
	
	private long lastFrame = 0, currentFrame = 0;
	private float fps = 0.0f;
	
	protected AssetManager assetManager;
	
	/**
	 * @param config
	 */
	public IliadClient(ClientConfig config, IliadController controller)
	{
		this.config = config;
	}
	
	@Override
	public void create() 
	{		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		this.lastFrame = System.nanoTime();		
		//This accounts for a problem when linking through eclipse
		FileHandle dataDir = this.config.getWritableAssetFolder();
		
		//assetManager = new AssetManager(new RemoteFileResolver(dataDir));
		
		//render = new Render(assetManager, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
										
		//Manages data about the game
		gameState = new GameState(dataDir);
		
		Controllers.addListener(controller);
		
		for(Controller c : Controllers.getControllers())
		{
			c.addListener(controller);
		}
		
		gameState.connect(true);
	}
	
	/**
	 * Dispose is the closing method called by LibGDX
	 */
	@Override
	public void dispose() 
	{
		try
		{
			
		}
		finally
		{
			gameState.dispose();
		}
	}

	/**
	 * Render is the libGDX method that we run our game loop inside of
	 * We want to keep this method slim and push actual functionality into their corresponding managers
	 */
	@Override
	public void render() 
	{
		
		//TODO: Fixed time step on processing messages
		//Is it beneficial to move ProcessMessage and Update to fixed time steps, so they aren't run every loop?
		while(gameState.processMessage());
		
		//TODO: can we combine processInput and update into one call?
		this.gameState.processInput();
		this.gameState.update();
		
		//this.render.render(gameState);
		
		//Simple FPS calculations
		currentFrame = System.nanoTime();
		fps = 1000000000f / (currentFrame - lastFrame);
		lastFrame = currentFrame;
		//Gdx.app.log(tag, "FPS: " + fps);
	}

	@Override
	public void resize(int width, int height) {
		//render.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}	
}