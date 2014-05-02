package com.iliadonline.client;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.client.network.LocalServer;
import com.iliadonline.client.network.RemoteServer;
import com.iliadonline.client.network.ServerInterface;
import com.iliadonline.shared.components.RenderComponent;
import com.iliadonline.shared.data.GameObject;
import com.iliadonline.shared.data.IliadMap;
import com.iliadonline.shared.network.ByteConverter;
import com.iliadonline.shared.network.Message;

/**
 * Responsible for holding the the client's view of the game data
 * TODO Need some way to return a set of render-able objects, but have proper layering 
 */
public class GameState
{
	private static final String tag = "com.iliadonline.client.GameState";
	
	private ServerInterface server;
	private FileHandle dataDir;
	
	private GameObject player;
	private GameStateEnum state;
	
	private IliadMap currentMap;
	
	private Message msg;
	
	protected float epsilon = 0.2f;
	
	public enum GameStateEnum
	{
		Started,
		MainMenu,
		Connected,
		Authenticated,
		Playing,
		Paused
	}

	/**
	 * Basic constructor
	 * @param dataDir
	 */
	public GameState(FileHandle dataDir)
	{
		if(!dataDir.file().canWrite())
		{
			//throw new IllegalArgumentException("dataDir must be writable.");
		}
		
		state = GameStateEnum.Started;
		this.dataDir = dataDir;
	}

	/**
	 * Getter for the game state
	 * We don't want outside code changing the state directly.
	 * @return GameStateEnum
	 */
	public GameStateEnum getState()
	{
		return this.state;
	}
	
	/**
	 * Method for isolating our message processing code
	 * Processes a single message. Idea is that IliadClient can control the looping.
	 * Returns false if there are no messages, otherwise true;
	 */
	public boolean processMessage() 
	{
		if(server == null)
		{
			return false;
		}
		
		msg = server.getMessages().poll();
		
		if(msg == null)
		{
			return false;
		}
					
		//Gdx.app.log(tag, "Message Received: " + msg);
		
		//We're starting with the switch to get done for CREATE
		//We can keep the switch as the default and build a better system upstream from it
		switch(msg.command)
		{
			case -1:
				this.player = new GameObject(ByteConverter.ByteArrayToInt(msg.data));
				this.player.setRenderer(new RenderComponent());
				
				Gdx.app.log(tag, "Client ID: " + this.player.getId());
				this.state = GameStateEnum.Authenticated;
				break;
			case 1:
				IntBuffer ints = ByteBuffer.wrap(msg.data).asIntBuffer();
				int id = ints.get(0);
				int x = ints.get(1);
				int y = ints.get(2);
									
				this.updateGameObject(id, x, y);
				break;
		}
		
		return true;
	}
	
	/**
	 * Called to tell GameState to update it's data
	 */
	public void update()
	{
		if(state == GameStateEnum.Authenticated)
		{
			ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE + Float.SIZE + Float.SIZE);
			
			buff.putInt(this.player.getId());
			buff.putFloat(this.player.getLocation().getX());
			buff.putFloat(this.player.getLocation().getY());
			
			Message msg = new Message((byte)1, buff.array(), null);
			
			//Gdx.app.log(tag, msg.toString());
			
			server.sendMessage(msg);
		}
	}
	
	/**
	 * Tells the gamestate to connect to the server
	 * @param localServer If TRUE will create an inprocess instance of the game server to act like a single player game
	 */
	public void connect(boolean localServer)
	{
		//TODO: should do state checks to enusre connect isn't called in an invalid state
		
		Gdx.app.log(tag, "Connect(" + String.valueOf(localServer) + ")");
		if(localServer)
		{
			this.server = new LocalServer(this.dataDir);
			this.server.connect();
		}
		else
		{
			this.server = new RemoteServer(5679);
			this.server.connect();
		}
		
		this.state = GameStateEnum.Connected;
	}
	
	/**
	 * General call for input processing
	 * Will determine which processing code is needed by where the app is running
	 */
	public void processInput()
	{
		/*
		 * @todo need better way to process input
		if(OuyaFacade.getInstance().isRunningOnOUYAHardware())
		{
			processInputOuya();
		}
		else
		{
			processInputPC();
		}*/
	}
	
	/**
	 * Specific handling for the Ouya
	 */
	protected void processInputOuya()
	{	
		if(Controllers.getControllers().size <= 0)
		{
			//End processing, no controller found
			return;
		}
		
		
		Controller controller = Controllers.getControllers().get(0);
		if(Controllers.getControllers().get(0) == null)
		{
			Gdx.app.log(tag, "Controller Not Found");
			return;
		}
		
		switch(this.state)
		{
			case Started:
				if(controller.getButton(Ouya.BUTTON_O))
				{
					this.connect(true);
				}
				break;				
			case MainMenu:
				break;
			case Authenticated:
			case Connected:
				if(player != null)
				{
					float shift = 55;
					float x, y;
					
					x = controller.getAxis(Ouya.AXIS_LEFT_X);
					y = -controller.getAxis(Ouya.AXIS_LEFT_Y);
					
					if(controller.getButton(Ouya.BUTTON_L1));
					{
						shift *= 3;
					}
										
					shift = Gdx.graphics.getDeltaTime() * shift;
					
					if(Math.abs(x) > epsilon)
					{
						player.getLocation().incrementX(shift * x);
					}
					
					if(Math.abs(y) > epsilon)
					{
						player.getLocation().incrementY(shift * y);
					}
					
					Gdx.app.log(tag, "X: " + String.valueOf(x));
					Gdx.app.log(tag, "Y: " + String.valueOf(y));
				}
				break;
			case Paused:
				break;
			case Playing:
				break;
			default:
				break;
		}
	}
	
	private void processInputPC()
	{
		switch(this.state)
		{
			case Started:
			case MainMenu:
				if(Gdx.app.getInput().isKeyPressed(Input.Keys.SPACE))
				{
					this.connect(true);
				}
				break;
			case Authenticated:
			case Connected:
				if(player != null)
				{
					float shift = 55;
					
					if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
					{
						shift *= 3;
					}
					
					shift = Gdx.graphics.getDeltaTime() * shift;
					
					if(Gdx.input.isKeyPressed(Keys.UP))
					{
						player.getLocation().incrementY(shift);
					}
					if(Gdx.input.isKeyPressed(Keys.DOWN))
					{
						player.getLocation().incrementY(-shift);
					}
					
					if(Gdx.input.isKeyPressed(Keys.LEFT))
					{
						player.getLocation().incrementX(-shift);
					}
					
					if(Gdx.input.isKeyPressed(Keys.RIGHT))
					{	
						player.getLocation().incrementX(shift);
					}
				}
				break;
			case Paused:
				break;
			case Playing:
				break;
			default:
				break;
		}
	}
	
	public void updateGameObject(int id, float x, float y)
	{
		if(id == this.player.getId())
		{
			//TODO: Can do a move check tolerance with the server
		}
		else
		{
			if(!this.currentMap.getGameObjects().containsKey(id))
			{
				GameObject gameObj = new GameObject(id);
				gameObj.getLocation().setMap(1);
				
				this.currentMap.getGameObjects().put(id, gameObj);
			}
			
			this.currentMap.getGameObjects().get(id).getLocation().setX(x);
			this.currentMap.getGameObjects().get(id).getLocation().setY(y);
		}
	}
	
	public GameObject getPlayer()
	{
		return this.player;
	}
	
	public IliadMap getMap()
	{
		return this.currentMap;
	}
	
	/**
	 * Releases any resources, stops any worker threads
	 */
	public void dispose()
	{
		
	}
}
