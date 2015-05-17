package se.greatbrain.sats.ui.menu;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import se.greatbrain.sats.R;
import se.greatbrain.sats.ui.center.CenterMapActivity;
import se.greatbrain.sats.ui.MainActivity;

public class MenuDrawerListener extends DrawerLayout.SimpleDrawerListener
                                implements ListView.OnItemClickListener
{
    public static boolean wasBackPressed = false;

    private final Activity activity;
    private int position;
    private boolean itemClicked = false;
    private DrawerLayout drawerLayout;

    public MenuDrawerListener(Activity activity)
    {
        this.activity = activity;
        this.drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        this.position = position;
        itemClicked = true;
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onDrawerClosed(View drawerView)
    {
        if(wasBackPressed)
        {
            wasBackPressed = false;
        }

        if (itemClicked)
        {
            startIntent(drawerView);
            itemClicked = false;
            wasBackPressed = false;
        }
    }

    private void startIntent(View drawerView)
    {
        if(position == 0)
        {
            Intent mainIntent = new Intent(drawerView.getContext(), MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            drawerView.getContext().startActivity(mainIntent);

            if(activity.getClass() != MainActivity.class)
            {
                activity.finish();
            }
        }
        else
        {
            Intent mapIntent = new Intent(drawerView.getContext(), CenterMapActivity.class);
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            drawerView.getContext().startActivity(mapIntent);

            if(activity.getClass() != CenterMapActivity.class)
            {
                activity.finish();
            }
        }
    }
}
