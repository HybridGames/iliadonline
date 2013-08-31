package com.iliadonline.client;

import tv.ouya.console.api.CancelIgnoringOuyaResponseListener;
import tv.ouya.console.api.OuyaFacade;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.iliadonline.client.IliadClient;

public class MainActivity extends AndroidApplication
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;

		String gamerUUID = "";
		
		CancelIgnoringOuyaResponseListener<String> gamerUuidListener = new CancelIgnoringOuyaResponseListener<String>()
		{
			@Override
			public void onSuccess(String result)
			{
				Log.d("UUID", "UUID is: " + result);
			}

			@Override
			public void onFailure(int errorCode, String errorMessage,
					Bundle errorBundle)
			{
				Log.d("Error", errorMessage);
			}
		};
		
		OuyaFacade.getInstance().requestGamerUuid(gamerUuidListener);

		initialize(new IliadClient(), cfg);
	}
}