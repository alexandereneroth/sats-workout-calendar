package se.greatbrain.sats.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.greatbrain.sats.R;
import se.greatbrain.sats.activity.GoogleMapActivity;
import se.greatbrain.sats.activity.MainActivity;
import se.greatbrain.sats.model.DrawerMenuItem;

public class DrawerMenuAdapter extends ArrayAdapter<DrawerMenuItem>
{
    public DrawerMenuAdapter(Context context, List<DrawerMenuItem> items)
    {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listrow_drawer_menu,
                    null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.drawer_menu_textview);
        ImageView icon = (ImageView) convertView.findViewById(R.id.drawer_menu_icon);

        DrawerMenuItem item = getItem(position);
        title.setText(item.title);
        icon.setImageResource(item.drawable);

        return convertView;
    }
}
