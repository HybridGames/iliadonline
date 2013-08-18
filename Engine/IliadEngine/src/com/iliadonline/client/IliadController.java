package com.iliadonline.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;

public class IliadController extends ControllerAdapter
{
	public static final String tag = "com.iliadonline.client.IliadController";
	
	
	public IliadController()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerAdapter#buttonDown(com.badlogic.gdx.controllers.Controller, int)
	 */
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex)
	{
		Gdx.app.log(tag, controller.getName() + " : "  + buttonIndex);
		return super.buttonDown(controller, buttonIndex);
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerAdapter#connected(com.badlogic.gdx.controllers.Controller)
	 */
	@Override
	public void connected(Controller controller)
	{
		// TODO Auto-generated method stub
		super.connected(controller);
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerAdapter#disconnected(com.badlogic.gdx.controllers.Controller)
	 */
	@Override
	public void disconnected(Controller controller)
	{
		// TODO Auto-generated method stub
		super.disconnected(controller);
	}
}
