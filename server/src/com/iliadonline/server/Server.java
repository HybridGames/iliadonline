package com.iliadonline.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.server.managers.ClientManager;
import com.iliadonline.server.managers.ServerGameState;
import com.iliadonline.server.managers.UUID;
import com.iliadonline.shared.network.Network;

/**
 * Primary entry point for the Stand-alone Multiplayer Server
 */
public class Server
{
	private static final String tag = "com.iliadonline.server.ServerMain";
	
	protected ServerGameState serverState;
	protected ClientManager clientManager;
	protected File serverDir;
	protected File assetDir;

	protected UUID uuid;

	protected boolean running = false;
	protected boolean paused = false;
	protected Network network;

	public static final long MAX_NETWORK_TIME = 10000L;

	public static Logger logger = Logger.getLogger("com.iliadonline.ServerMain");

	public int port;
	public int assetPort;

	protected Server(File serverDir)
	{
		this.serverDir = serverDir;
		this.loadConfiguration();
		
		serverState = new ServerGameState(new FileHandle(serverDir));

		try
		{
			network = new Network(serverState, this.port);			
		}
		catch (IOException e)
		{
			Gdx.app.error(tag, "Unable to start Network");
			e.printStackTrace();
		}

		//serverState.setIncomingQueue(network.getIncomingQueue());
		
		new Thread(serverState).start();
		new Thread(network).start();
	}

	/**
	 * Loads the server configurations from files in the directory passed at run
	 * This configuration data should be limited to the configuration needed for
	 * networking. Game State configuration should be handled by the
	 * ServerGameState object.
	 */
	protected void loadConfiguration()
	{
		File serverProps = new File(serverDir.getAbsolutePath()	+ "/serverProps.json");

		JSONTokener tokener;
		JSONObject configObj;
		JSONObject serverObj;
		JSONObject assetsObj;

		try
		{
			tokener = new JSONTokener(new FileInputStream(serverProps));
			configObj = new JSONObject(tokener);
			serverObj = configObj.getJSONObject("server");
			assetsObj = serverObj.getJSONObject("assets");

			this.port = serverObj.getInt("port");

			this.assetDir = new File(serverDir.getAbsolutePath() + "/" + assetsObj.getString("dir"));
			this.assetPort = assetsObj.getInt("port");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Server entry point
	 * 
	 * @param args
	 */
	public static void main(String args[])
	{
		if (args.length <= 0)
		{
			throw new IllegalArgumentException("arg[0] not provided, must be a direcotry.");
		}

		File serverDir = new File(args[0]);

		if (!serverDir.isDirectory())
		{
			throw new IllegalArgumentException("arg[0] must be a direcotry.");
		}

		if (!serverDir.canRead())
		{
			throw new IllegalArgumentException("arg[0] is not readable.");
		}

		if (!serverDir.canWrite())
		{
			throw new IllegalArgumentException("arg[0] is not writable.");
		}

		// Logger.getLogger("").setLevel(Level.OFF);
		new Server(serverDir);
	}
}
