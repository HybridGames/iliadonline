package com.iliadonline.client.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.iliadonline.client.render.Render;

public class MainMenu extends Stage
{
	public static final String tag = "com.iliadonline.client.menu.MainMenu";
	
	public MainMenu(Render render)
	{
		Table table = new Table();
		table.setFillParent(true);
		this.addActor(table);
		
		TextButtonStyle style = new TextButtonStyle();
		style.font = render.getFont("default");
		
		TextButton button1 = new TextButton("Single Player", style);
		TextButton button2 = new TextButton("Multi Player", style);
		
		table.add(button1);
		table.row();
		table.add(button2);
		table.row();
		
		TextFieldStyle style2 = new TextFieldStyle();
		style2.font = render.getFont("default");
		style2.messageFont = render.getFont("default");
		style2.fontColor = new Color(1, 1, 1, 1f);
		style2.messageFontColor = new Color(1,1,1,.2f);
		TextField text1 = new TextField("", style2);
		text1.setMessageText("UserName");
		
		table.add(text1);
		
		button1.addListener(new EventListener()
		{
			
			@Override
			public boolean handle(Event event)
			{
				Gdx.app.log(tag, event.toString());
				return false;
			}
		});
		
		text1.addListener(new InputListener(){
			
			@Override
			public boolean keyDown(InputEvent event, int keycode)
			{
				
				return super.keyDown(event, keycode);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button)
			{
				Gdx.input.getTextInput(new TextInputListener()
				{
					@Override
					public void input(String arg0)
					{
						Gdx.app.log(tag, arg0);
						
					}
					
					@Override
					public void canceled()
					{
						// TODO Auto-generated method stub
						
					}
				}, "Title", "InitialValue");
				return super.touchDown(event, x, y, pointer, button);
			}
			
		});
	}
}
