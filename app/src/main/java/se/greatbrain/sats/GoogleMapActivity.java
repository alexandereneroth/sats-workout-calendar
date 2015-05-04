package se.greatbrain.sats;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmResults;
import se.greatbrain.sats.model.realm.Center;
import se.greatbrain.sats.realm.RealmClient;

/**
 * Created by aymenarbi on 30/04/15.
 */

public class GoogleMapActivity extends ActionBarActivity
{
    private GoogleMap map ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.map)).getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        final Map<Marker, Center> markerCenterMap = addCenterMarkers(map);

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker)
            {
                Intent intent = new Intent(getApplicationContext(), FindCenterDetailActivity.class);
                intent.putExtra("centerUrl", markerCenterMap.get(marker).getUrl());
                startActivity(intent);
            }
        });

        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap
                .OnMyLocationChangeListener()
        {
            @Override
            public void onMyLocationChange(Location location)
            {
                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                Marker locationMarker = map.addMarker(new MarkerOptions().position(myLocation)
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_location)));
                if (map != null)
                {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10.0f));
                }
            }
        };
        map.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        return super.onCreateOptionsMenu(menu);
    }

    private Map<Marker, Center> addCenterMarkers(GoogleMap map)
    {
        Map<Marker, Center> markerCenterMap = new HashMap<>();

        if (map != null)
        {
            RealmClient realmClient = RealmClient.getInstance(this);
            RealmResults<Center> centers = realmClient.getAllCenters();

            for (Center center : centers)
            {
                LatLng satsLocation = new LatLng(center.getLat(), center.getLng());
                Marker satsMarker = map.addMarker(new MarkerOptions()
                        .position(satsLocation)
                        .title(center.getName())
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.sats_pin_normal)));

                markerCenterMap.put(satsMarker, center);
            }
        }
        return markerCenterMap;
    }
}

