package com.iliadonline.client.splash;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.iliadonline.client.IliadClient;

public class SplashScreen implements Screen
{
	private static final String tag = "com.iliadonline.client.splash.SplashScreen";
	
	protected IliadClient client;
	protected AssetManager assetManager;
	protected Texture loading;
	protected SpriteBatch batch;
	protected Sprite sprite;
	
	public SplashScreen(IliadClient client)
	{
		this.client = client;
		this.assetManager = client.getAssetManager();
		this.batch = new SpriteBatch();
	}
	
	@Override
	public void render(float delta)
	{		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		//batch.draw(this.loading, 0, 0);
		sprite.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height)
	{

	}

	@Override
	public void show()
	{
		assetManager.load("gfx/splash/IliadLoading.png", Texture.class);
		
		while(!assetManager.update());
		
		FileHandle assetList = this.client.getConfig().getStaticAssetFolder().child("assets\\data\\assets.json");
		
		JSONTokener tokener;
		try
		{
			tokener = new JSONTokener(new FileInputStream(assetList.file()));
			
			JSONArray assetArray = new JSONArray(tokener);
			
			for(int i = 0; i < assetArray.length(); i++)
			{
				assetManager.load(assetArray.getString(i), BitmapFont.class);
			}
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		this.loading = assetManager.get("gfx/splash/IliadLoading.png");
		sprite = new Sprite(this.loading);
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
