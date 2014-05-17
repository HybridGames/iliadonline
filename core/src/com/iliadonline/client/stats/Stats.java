package com.iliadonline.client.stats;

import com.badlogic.gdx.Gdx;

/**
 * Stats is designed to encapsulate basic FPS and other stats code from clogging up the main render loops
 */
public class Stats
{	
	protected long startTime = 0, frameCount = 0, averageFrame = 0;
	protected long lastFrame = 0, currentFrame = 0;
	protected float fps = 0.0f;
	
	
	/**
	 * Called at the beginning of a Rendering pass to establish beginning times
	 */
	public void beginRender()
	{
		startTime = System.nanoTime();
	}
	
	/**
	 * Called at the end of a Rendering pass
	 */
	public void endRender()
	{
		averageFrame = ((averageFrame * frameCount) + (System.nanoTime() - startTime)) / ++frameCount;
		
		fps = 1000000000f / averageFrame;
		
		Gdx.app.log("Stats", "Average Frame: " + averageFrame);
		Gdx.app.log("Stats", "FPS: " + fps);
	}	
}
