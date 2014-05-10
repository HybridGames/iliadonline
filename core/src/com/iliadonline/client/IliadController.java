package com.iliadonline.client;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.iliadonline.client.controller.ControllerActionEnum;

/**
 * An interface for handling controller input. To be initialized by the Launcher and read by the Core.
 */
public class IliadController extends ControllerAdapter
{
	public static final String tag = "com.iliadonline.client.IliadController";
	
	protected Map<Integer, ControllerActionEnum> buttonMapping;
	protected Map<Integer, ControllerActionEnum> axisMapping; 
	
	/**
	 * Sets the Button and Axis mapping to abstract the controller setups from different launchers
	 * @param buttonMapping
	 * @param axisMapping
	 */
	public IliadController(Map<Integer, ControllerActionEnum> buttonMapping, Map<Integer, ControllerActionEnum> axisMapping)
	{
		super();
		this.buttonMapping = buttonMapping;
		this.axisMapping = axisMapping;
	}

	/**
	 * @see com.badlogic.gdx.controllers.ControllerAdapter#buttonDown(com.badlogic.gdx.controllers.Controller, int)
	 */
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex)
	{
		Gdx.app.log(tag, controller.getName() + " : "  + buttonIndex);
		
		//GameState.inputAction(buttonMapping.get(buttonIndex));
		
		return super.buttonDown(controller, buttonIndex);
	}
	
	/**
	 * Handles analog sticks, etc.
	 */
	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value)
	{
		// TODO Auto-generated method stub
		return super.axisMoved(controller, axisIndex, value);
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
