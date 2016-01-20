package se.greatbrain.sats.ui;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import se.greatbrain.sats.R;

public class HockeyAppActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        checkForUpdates();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        UpdateManager.unregister();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        checkForCrashes();
    }

    private void checkForCrashes()
    {
        CrashManager.register(this, getString(R.string.hockey_app_id));
    }

    private void checkForUpdates()
    {
        // Remove this for store / production builds!
        UpdateManager.register(this, getString(R.string.hockey_app_id));
    }
}