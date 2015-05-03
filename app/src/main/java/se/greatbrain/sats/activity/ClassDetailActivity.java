package se.greatbrain.sats.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import se.greatbrain.sats.R;
import se.greatbrain.sats.fragment.ClassDetailFragment;

public class ClassDetailActivity extends ActionBarActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        setContentView(R.layout.activity_main);

        FragmentManager manager = getFragmentManager();

        Fragment classDetailFragment = manager.findFragmentById(R.layout
                .fragment_class_detail);

        Log.d("Test", "Am I here?");

        if (classDetailFragment == null)
        {
            manager.beginTransaction().add(R.id.bottom_fragment_container,
                    new ClassDetailFragment()).commit();
        }

    }

}
