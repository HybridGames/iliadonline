package com.iliadonline.client.desktop;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.client.ClientConfig;
import com.iliadonline.client.IliadClient;
import com.iliadonline.client.IliadController;
import com.iliadonline.client.controller.ControllerActionEnum;

/**
 * Launcher for the Desktop version.
 * Needs to establish Config to send to the IliadClient
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 600;
		
		ClientConfig clientConfig = new DesktopClientConfig();
		
		Map<Integer, ControllerActionEnum> buttonMapping = new HashMap<Integer, ControllerActionEnum>();
		buttonMapping.put(Keys.UP, ControllerActionEnum.MOVE_UP);
		buttonMapping.put(Keys.DOWN, ControllerActionEnum.MOVE_DOWN);
		buttonMapping.put(Keys.LEFT, ControllerActionEnum.MOVE_LEFT);
		buttonMapping.put(Keys.RIGHT, ControllerActionEnum.MOVE_RIGHT);
		buttonMapping.put(Keys.ESCAPE, ControllerActionEnum.MENU);
		
		Map<Integer, ControllerActionEnum> axisMapping = new HashMap<Integer, ControllerActionEnum>();
		
		IliadController controller = new IliadController(buttonMapping, axisMapping);
		
		IliadClient client = new IliadClient(clientConfig, controller);
		
		new LwjglApplication(client, config);
	}
}
