package se.greatbrain.sats.adapter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SlidingDrawer;

import se.greatbrain.sats.activity.GoogleMapActivity;
import se.greatbrain.sats.activity.MainActivity;

public class DrawerItemClickListener implements ListView.OnItemClickListener
{
    private final DrawerLayout drawerLayout;

    public DrawerItemClickListener(DrawerLayout drawerLayout)
    {
        this.drawerLayout = drawerLayout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        switch (position)
        {
            case 0:
                Intent mainIntent = new Intent(view.getContext(), MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                view.getContext().startActivity(mainIntent);
                new Handler().postDelayed(new DrawerCloser(drawerLayout), 70);
                break;
            case 1:
                Intent mapIntent = new Intent(view.getContext(), GoogleMapActivity.class);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                view.getContext().startActivity(mapIntent);
                new Handler().postDelayed(new DrawerCloser(drawerLayout), 70);
                break;
        }
    }

    private class DrawerCloser implements Runnable
    {
        private final DrawerLayout drawerLayout;

        public DrawerCloser(DrawerLayout drawerLayout)
        {
            this.drawerLayout = drawerLayout;
        }

        @Override
        public void run()
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}
