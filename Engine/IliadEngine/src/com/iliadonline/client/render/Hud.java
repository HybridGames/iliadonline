package com.iliadonline.client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.iliadonline.client.GameState;
import com.iliadonline.client.GameState.GameStateEnum;
import com.iliadonline.shared.data.Location;

public class Hud
{
	private static final String tag = "com.iliadonline.client.render.Hud";
	
	SpriteBatch batch = new SpriteBatch();
	AssetManager assetManager;
	BitmapFont font;
	
	protected CharSequence versionStr = "Iliad Online Beta 0.0.1";
	protected CharSequence stateStr;
	
	public Hud(AssetManager assetManager)
	{
		this.assetManager = assetManager;
	}
	
	public void render(GameState state)
	{
		if(!this.assetManager.update())
		{
			Gdx.app.log(tag, "Asset Manager loading: " + this.assetManager.getProgress());
			return;
		}
		
		font = this.assetManager.get("gfx/fonts/arial32.fnt", BitmapFont.class);
		
		//Get Renderable Elements from GameState
		stateStr = state.getState().name();
		
		batch.begin();
		
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.draw(batch, versionStr + " - " + stateStr, 100, 100);
				
		if(state.getState() == GameStateEnum.Authenticated)
		{
			Location location = state.getPlayer().getLocation();
			font.draw(batch, "[" + location.x + ", " + location.y + "]", 100, 1000);
		}
		
		batch.end();
	}
}
