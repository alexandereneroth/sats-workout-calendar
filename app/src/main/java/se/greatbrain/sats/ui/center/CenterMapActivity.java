package se.greatbrain.sats.ui.center;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;
import se.greatbrain.sats.R;
import se.greatbrain.sats.data.RealmClient;
import se.greatbrain.sats.data.model.Center;
import se.greatbrain.sats.ui.menu.MenuDrawerAdapter;
import se.greatbrain.sats.ui.menu.MenuDrawerItem;
import se.greatbrain.sats.ui.menu.MenuDrawerListener;

public class CenterMapActivity extends AppCompatActivity
{
    private GoogleMap map;
    private Map<Marker, Center> markerCenterMap;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_map);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.map)).getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        markerCenterMap = addCenterMarkers(map);

        findCenterDetailView();
        moveCameraToMyLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        ActionBar actionBar = getSupportActionBar();
        View actionBarView = getLayoutInflater().inflate(R.layout.action_bar_menu, null);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);

        // remove left actionbar padding
        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_text_view);
        actionBarTitle.setText("HITTA CENTER");
        setOnClickHomeButton();
        setupSlidingMenu();

        return super.onCreateOptionsMenu(menu);
    }

    private void setupSlidingMenu()
    {
        ListView drawerMenu = (ListView) findViewById(R.id.drawer_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerMenu.setAdapter(new MenuDrawerAdapter(this, populateDrawerList()));
        MenuDrawerListener listener = new MenuDrawerListener(this);
        drawerMenu.setOnItemClickListener(listener);
        drawerLayout.setDrawerListener(listener);
    }

    private List<MenuDrawerItem> populateDrawerList()
    {
        List<MenuDrawerItem> items = new ArrayList<>();
        items.add(new MenuDrawerItem(R.drawable.my_training, "min träning"));
        items.add(new MenuDrawerItem(R.drawable.sats_pin_drawer_menu, "hitta center"));

        return items;
    }

    private void findCenterDetailView()
    {
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker)
            {
                Intent intent = new Intent(getApplicationContext(), CenterWebViewActivity.class);
                intent.putExtra("centerUrl", markerCenterMap.get(marker).getUrl());
                startActivity(intent);
            }
        });
    }

    private void moveCameraToMyLocation()
    {
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap
                .OnMyLocationChangeListener()
        {
            boolean moveToMyLocation = true;

            @Override
            public void onMyLocationChange(Location location)
            {
                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                if (map != null && moveToMyLocation)
                {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10.0f));
                    moveToMyLocation = false;
                }
            }
        };
        map.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    private Map<Marker, Center> addCenterMarkers(GoogleMap map)
    {
        Map<Marker, Center> markerCenterMap = new HashMap<>();

        if (map != null)
        {
            RealmClient realmClient = RealmClient.getInstance(this);
            RealmResults<Center> centers = realmClient.getAllCenters();

            for (final Center center : centers)
            {
                LatLng satsLocation = new LatLng(center.getLat(), center.getLng());
                Marker satsMarker = map.addMarker(new MarkerOptions()
                        .position(satsLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.sats_pin_normal)));

                map.setInfoWindowAdapter(new SatsInfoWindowAdapter());

                markerCenterMap.put(satsMarker, center);
            }
        }
        return markerCenterMap;
    }

    private void setOnClickHomeButton()
    {
        ImageView menuIcon = (ImageView) findViewById(R.id.btn_dots_logo_sats_menu);
        menuIcon.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        if (drawer.isDrawerOpen(GravityCompat.START))
                        {
                            drawer.closeDrawer(GravityCompat.START);
                        }
                        else
                        {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed()
    {
        if (MenuDrawerListener.wasBackPressed)
        {
            super.onBackPressed();
            MenuDrawerListener.wasBackPressed = false;
        }
        else
        {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
            {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            else
            {
                drawerLayout.openDrawer(GravityCompat.START);
                MenuDrawerListener.wasBackPressed = true;
            }
        }
    }

    private class SatsInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {
        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            View view = getLayoutInflater().inflate(R.layout.marker_info_window_layout, null);
            TextView textView = (TextView) view.findViewById(R.id
                    .marker_info_text_view);
            Center center = markerCenterMap.get(marker);
            textView.setText("SATS " + center.getName());

            ImageView imageView = (ImageView) view.findViewById(R.id
                    .marker_info_image_view);
            imageView.setImageResource(R.drawable.arrow);
            return view;
        }
    }
}

