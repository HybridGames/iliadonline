package com.iliadonline.client.game;

import com.badlogic.gdx.Screen;
import com.iliadonline.client.IliadClient;
import com.iliadonline.client.render.RenderInterface;
import com.iliadonline.client.state.GameState;
import com.iliadonline.client.stats.Stats;

/**
 * The main screen for the game
 */
public class GameScreen implements Screen
{
	protected IliadClient client;
	
	protected Stats stats;
	protected GameState gameState;
	protected RenderInterface render;
	
	/**
	 * Constructor requires a reference to the client
	 * @param client
	 */
	public GameScreen(IliadClient client)
	{
		this.client = client;
		this.stats = client.getStats();
		this.gameState = client.getGameState();
		this.render = client.getRender();
	}

	@Override
	public void render(float delta)
	{
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
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void show()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void hide()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

}
