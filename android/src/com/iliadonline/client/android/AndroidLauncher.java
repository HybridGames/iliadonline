package com.iliadonline.client.android;

import tv.ouya.console.api.GamerInfo;
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
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new IliadClient(), config);
		
		OuyaFacade.getInstance().requestGamerInfo(this);
	}

	@Override
	public void onCancel()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailure(int arg0, String arg1, Bundle arg2)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(GamerInfo gamerInfo)
	{
		Gdx.app.log("User Name", gamerInfo.getUsername());
	}
}
