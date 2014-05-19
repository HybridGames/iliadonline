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
	
	protected long now;
	
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
		now = System.nanoTime();
		
		//Average time for each rendering frame
		averageFrame = ((averageFrame * frameCount) + (now - startTime)) / ++frameCount;
		
		//Actual time between the end of each frame
		fps = 1000000000f / (now - lastFrame);
		lastFrame = now;
	}
	
	/**
	 * The current FPS
	 * @return float
	 */
	public float getFps()
	{
		return this.fps;
	}
	
	/**
	 * The average length of a rendering frame
	 * @return float
	 */
	public float getAverageFrame()
	{
		return (averageFrame / 1000000000f);
	}
}
