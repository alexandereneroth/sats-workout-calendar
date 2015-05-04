package se.greatbrain.sats.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import se.greatbrain.sats.R;
import se.greatbrain.sats.fragment.ClassDetailFragment;

public class ClassDetailActivity extends ActionBarActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d("Test", "Am I here?");

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().add(R.id.bottom_fragment_container,
                new ClassDetailFragment()).commit();
    }

}
