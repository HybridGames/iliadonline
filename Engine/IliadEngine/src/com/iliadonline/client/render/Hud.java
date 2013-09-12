package com.iliadonline.client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.iliadonline.client.GameState;
import com.iliadonline.client.GameState.GameStateEnum;
import com.iliadonline.shared.data.Location;

public class Hud
{
	private static final String tag = "com.iliadonline.client.render.Hud";
	
	Stage stage;
	Label gameVersion;
	
	SpriteBatch batch = new SpriteBatch();
	AssetManager assetManager;
	BitmapFont font;
	
	protected CharSequence versionStr = "Iliad Online Beta 0.0.1";
	protected CharSequence stateStr;
	
	public Hud(AssetManager assetManager)
	{
		this.assetManager = assetManager;
		
		stage = new Stage();
	}
	
	public void render(GameState state)
	{
		if(!this.assetManager.update())
		{
			Gdx.app.log(tag, "Asset Manager loading: " + this.assetManager.getProgress());
			return;
		}
		
		/*font = this.assetManager.get("gfx/fonts/arial32.fnt", BitmapFont.class);
		LabelStyle style = new LabelStyle(font, Color.WHITE);
		gameVersion = new Label(versionStr, style);
		
		stage.addActor(gameVersion);
		stage.draw();*/
		
		//Get Renderable Elements from GameState
		stateStr = state.getState().name();
		
		batch.begin();
		
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.draw(batch, versionStr + " - " + stateStr, 100, 100);
				
		if(state.getState() == GameStateEnum.Authenticated)
		{
			Location location = state.getPlayer().getLocation();
			font.draw(batch, "[" + location.getX() + ", " + location.getY() + "]", 100, 1000);
		}
		
		batch.end();
	}
}
