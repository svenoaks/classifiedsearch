package com.smp.findlocal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;

public class AdActivity extends Activity implements AdListener
{
	private Uri link;
	private InterstitialAd interstitialAd;
	private final String adId = "ca-app-pub-7592316401695950/9478614224";
	private final String HtcOne = "C6D90FEDCA2E50BFACEFDA3380DD4EA2";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		link = getIntent().getData();
		interstitialAd = new InterstitialAd(this, adId);
		interstitialAd.setAdListener(this);
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		adRequest.addTestDevice(HtcOne);
		interstitialAd.loadAd(adRequest);
	}
	private void gotoLink()
	{
		Intent linkIntent = new Intent(Intent.ACTION_VIEW);
		linkIntent.setData(link);
		linkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(linkIntent);
	}
	@Override
	public void onDismissScreen(Ad arg0)
	{
		gotoLink();
		finish();
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1)
	{
		gotoLink();
		finish();
	}

	@Override
	public void onLeaveApplication(Ad arg0)
	{
		finish();
	}

	@Override
	public void onPresentScreen(Ad arg0)
	{
		finish();
	}

	@Override
	public void onReceiveAd(Ad arg0)
	{
		interstitialAd.show();
	}
}
