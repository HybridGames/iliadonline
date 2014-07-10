package com.iliadonline.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.client.asset.IliadFileResolver;
import com.iliadonline.client.menu.MainMenuScreen;
import com.iliadonline.client.render.RenderInterface;
import com.iliadonline.client.render.debug.DebugRenderer;
import com.iliadonline.client.state.GameState;
import com.iliadonline.client.stats.Stats;

/**
 * This is our main Application class in the LibGDX setup.
 * Here we're working to establish basic configuration data and build objects and threads that will manage the rest of the game.
 * 
 * @todo Game crashes if a controller isn't setup
 */
public class IliadClient extends Game 
{
	private static final String tag = "com.iliadonline.client.IliadClient";
	
	protected ClientConfig config;
	protected IliadController controller;
	
	protected RenderInterface render;
	protected GameState gameState;
	
	protected Stats stats;
	
	protected AssetManager assetManager;
	protected IliadFileResolver fileResolver;
	
	/**
	 * @param config
	 */
	public IliadClient(ClientConfig config, IliadController controller)
	{
		this.config = config;
		this.controller = controller;
	}
	
	@Override
	public void create() 
	{
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		this.fileResolver = new IliadFileResolver(this.config);
		this.assetManager = new AssetManager(this.fileResolver);
		
		this.stats = new Stats();
		
		//render = new Render(assetManager, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		render = new DebugRenderer(this.assetManager, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
										
		//Manages data about the game
		gameState = new GameState(this.config);
		
		Controllers.addListener(controller);
		
		gameState.connect(false);
		
		this.setScreen(new MainMenuScreen(this));
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
		super.render();
		this.stats.beginRender();
		
		//TODO: Fixed time step on processing messages
		//Is it beneficial to move ProcessMessage and Update to fixed time steps, so they aren't run every loop?
		while(gameState.processMessage());
		
		//TODO: can we combine processInput and update into one call?
		this.gameState.processInput();
		this.gameState.update();
		
		this.render.render(gameState);
		
		this.stats.endRender();
		/*Gdx.app.log("Stats", "FPS: " + this.stats.getFps());
		Gdx.app.log("Stats", "Average Frame: " + this.stats.getAverageFrame());*/
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
}