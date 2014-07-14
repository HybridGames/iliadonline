package com.iliadonline.client.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.iliadonline.client.IliadClient;

public class MainMenuScreen implements Screen
{
	private static final String tag = "com.iliadonline.client.menu.MainMenuScreen";
	
	protected IliadClient client;
	
	protected Stage stage;
	
	public MainMenuScreen(IliadClient client)
	{
		this.client = client;
	}

	@Override
	public void render(float delta)
	{
		this.stage.act(Gdx.graphics.getDeltaTime());
		this.stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		AssetManager manager = this.client.getAssetManager();
		manager.load("gfx/fonts/courier12.fnt", BitmapFont.class);
		while(!manager.update())
		{
			//Gdx.app.log(tag, "Asset Manager loading: " + manager.getProgress());
		}
		
		Gdx.app.log(tag, "Loading Complete");
		
		this.initStage();
	}

	@Override
	public void hide()
	{
		this.stage.dispose();
	}

	@Override
	public void pause()
	{
		
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void dispose()
	{
		
	}

	/**
	 * Initializes our stage for us
	 */
	protected void initStage()
	{
		this.stage = new Stage(new ScreenViewport());
		
		LabelStyle style = new LabelStyle();
		BitmapFont font = client.getAssetManager().get("gfx/fonts/courier12.fnt", BitmapFont.class);
		style.font = font;
		Label label = new Label("Iliad Online", style);
		label.setBounds(500, 500, 100, 50);
		stage.addActor(label);
	}
}
