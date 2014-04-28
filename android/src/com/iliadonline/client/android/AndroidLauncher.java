package com.iliadonline.client.android;

import tv.ouya.console.api.GamerInfo;
import tv.ouya.console.api.OuyaController;
import tv.ouya.console.api.OuyaFacade;
import tv.ouya.console.api.OuyaResponseListener;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.iliadonline.client.IliadClient;

public class AndroidLauncher extends AndroidApplication implements OuyaResponseListener<GamerInfo>
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		OuyaFacade.getInstance().init(this, "00000000-0000-0000-0000-000000000000");
		
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new IliadClient(), config);
		
		OuyaFacade.getInstance().requestGamerInfo(this);
		
		OuyaController.init(this);
		OuyaController.showCursor(false);
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
