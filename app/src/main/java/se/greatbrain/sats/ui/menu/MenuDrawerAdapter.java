package se.greatbrain.sats.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.greatbrain.sats.R;

public class MenuDrawerAdapter extends ArrayAdapter<MenuDrawerItem>
{
    public MenuDrawerAdapter(Context context, List<MenuDrawerItem> items)
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

        MenuDrawerItem item = getItem(position);
        title.setText(item.title);
        icon.setImageResource(item.drawable);

        return convertView;
    }
}
