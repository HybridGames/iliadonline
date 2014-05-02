package com.iliadonline.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controllers;
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
		
	protected Render render;
	protected GameState gameState;
	
	private long lastFrame = 0, currentFrame = 0;
	private float fps = 0.0f;
	
	protected boolean isOuya = false;
	
	protected AssetManager assetManager;
	
	/**
	 * Prefer a constructor that determines if we're playing on Ouya or not
	 * @param isOuya boolean
	 */
	public IliadClient(boolean isOuya)
	{
		this.isOuya = isOuya;
	}
	
	/**
	 * Default constructor to help with any libGdx automated code
	 */
	public IliadClient()
	{
		this(false);
	}
	
	@Override
	public void create() 
	{		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		this.lastFrame = System.nanoTime();		
		//This accounts for a problem when linking through eclipse
		FileHandle dataDir;
		if(Gdx.app.getType() == ApplicationType.Android)
		{
			//Initialize out local directories
			dataDir = initializeOuyaDirectories();
			
			//dataDir = Gdx.files.internal("data");
		}
		else
		{
			//TODO: Check this assessment
			//Internal on a desktop is roughly the same as Local
			dataDir = Gdx.files.internal("./bin/data/");
		}
		
		assetManager = new AssetManager(new RemoteFileResolver(dataDir));
		
		FileHandle gfxDir;
		gfxDir = dataDir.child("gfx");
		//render = new Render(gfxDir, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		render = new Render(assetManager, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
										
		//Manages data about the game
		gameState = new GameState(dataDir);
		
		Controllers.addListener(new IliadController());
		
		gameState.connect(false);
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
		
		this.render.render(gameState);
		
		//Simple FPS calculations
		currentFrame = System.nanoTime();
		fps = 1000000000f / (currentFrame - lastFrame);
		lastFrame = currentFrame;
		//Gdx.app.log(tag, "FPS: " + fps);
	}

	@Override
	public void resize(int width, int height) {
		render.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	/**
	 * Creates all the local directories that we require if they are missing.
	 * We may need something like this for desktop, but focus is on Ouya right now.
	 * @return
	 */
	protected FileHandle initializeOuyaDirectories()
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
		
		Gdx.app.log(tag, "Data Dir: " + dataDir.path());
		
		return dataDir;
	}
}