package com.iliadonline.client.android;

import java.util.HashMap;
import java.util.Map;

import tv.ouya.console.api.GamerInfo;
import tv.ouya.console.api.OuyaController;
import tv.ouya.console.api.OuyaFacade;
import tv.ouya.console.api.OuyaResponseListener;
import android.os.Bundle;
import android.util.SparseArray;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.iliadonline.client.ClientConfig;
import com.iliadonline.client.IliadClient;
import com.iliadonline.client.IliadController;
import com.iliadonline.client.controller.ControllerActionEnum;

public class AndroidLauncher extends AndroidApplication implements OuyaResponseListener<GamerInfo>
{
	private String tag = "AndroidLauncher";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		
		//Configure Android Application
		OuyaFacade.getInstance().init(this, "a82d33ec-3e86-4afd-ade4-c23c56c023b5");
		OuyaFacade.getInstance().requestGamerInfo(this);
		
		OuyaController.init(this);
		OuyaController.showCursor(false);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
				
		//Setup Controller Mappings
		Map<Integer, ControllerActionEnum> buttonMapping = new HashMap<Integer, ControllerActionEnum>();
		buttonMapping.put(OuyaController.BUTTON_MENU, ControllerActionEnum.MENU);
		
		Map<Integer, ControllerActionEnum> axisMapping = new HashMap<Integer, ControllerActionEnum>();
		buttonMapping.put(OuyaController.AXIS_LS_X, ControllerActionEnum.MOVE_X);
		buttonMapping.put(OuyaController.AXIS_LS_Y, ControllerActionEnum.MOVE_Y);
		
		IliadController controller = new IliadController(buttonMapping, axisMapping);
				
		ClientConfig clientConfig = new AndroidClientConfig();
		
		IliadClient client = new IliadClient(clientConfig, controller);
		initialize(client, config);
	}

	@Override
	protected void onDestroy()
	{
		OuyaFacade.getInstance().shutdown();
		super.onDestroy();
	}

	@Override
	public void onCancel()
	{
		Gdx.app.log("User Name", "Cancel");
	}

	@Override
	public void onFailure(int arg0, String str, Bundle arg2)
	{
		Gdx.app.log("User Name", "Failure - " + str);		
	}

	@Override
	public void onSuccess(GamerInfo gamerInfo)
	{
		Gdx.app.log("User Name", gamerInfo.getUsername());
	}
}
