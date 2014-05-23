package com.iliadonline.client.render.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.iliadonline.client.render.RenderInterface;
import com.iliadonline.client.state.GameState;

public class DebugRenderer implements RenderInterface
{
	private static String tag = "com.iliadonline.client.render.debug.DebugRenderer";
	
	protected AssetManager assetManager;
	protected int width, height;
	
	protected OrthographicCamera camera;
	protected ShapeRenderer shapes;
	protected SpriteBatch batch;
	
	protected BitmapFont font;

	protected TextBounds bounds;
	
	/**
	 * @param assetManager
	 * @param width
	 * @param height
	 */
	public DebugRenderer(AssetManager assetManager, int width, int height)
	{
		this.assetManager = assetManager;
		this.width = width;
		this.height = height;
		
		camera = new OrthographicCamera(width, height);
		batch = new SpriteBatch();
		shapes = new ShapeRenderer();
		
		this.assetManager.load("gfx/fonts/courier32.fnt", BitmapFont.class);
	}
	
	@Override
	public void render(GameState state)
	{
		if(!this.assetManager.update())
		{
			Gdx.app.log(tag, "Asset Manager loading: " + this.assetManager.getProgress());
			return;
		}
		
		if(font == null)
		{
			font = this.assetManager.get("gfx/fonts/courier32.fnt");
		}
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL30.GL_COLOR_CLEAR_VALUE);
				
		switch(state.getState())
		{
			default:
				batch.begin();		
				//camera.update();
				
				font.setColor(Color.WHITE);
				CharSequence chars = "Test";
				bounds = font.draw(batch, chars, 100, 100);
				
				batch.end();
				
				batch.begin();
				shapes.begin(ShapeType.Line);
				shapes.setProjectionMatrix(camera.combined);
				shapes.setColor(1,0,1,1);
				shapes.line(10, 0, 0, 0 + 10);
				shapes.line(0, 0+10, 0-10, 0);
				shapes.line(0-10, 0, 0, 0-10);
				shapes.line(0, 0-10, 0+10, 0);
				shapes.end();
				
				batch.end();
				break;
		}
	}

	@Override
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
}
