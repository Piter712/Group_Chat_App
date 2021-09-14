package com.example.chatspace;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    public ClipData clipData;
    public ClipData clipDataLong;
    public ClipboardManager clipboardManager;
    private Button btncpy;

    public void searchLocation(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        List<Address> list = null;

        if (location != null && !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
                //addressList = geocoder.getFromLocation(latPoint,lng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(getApplicationContext(), "Please enter coordinates or location name", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setOnMapClickListener(this);


        mMap.setOnMarkerDragListener(this);


        LatLng PP = new LatLng(52.40198, 16.95159);
        Marker StartMarker = mMap.addMarker(new MarkerOptions().
                position(PP)
                .title("PP")
                .snippet("Here we go!")
                .draggable(true)
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PP));

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("TAG", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("TAG", "Can't find style. Error: ", e);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.addMarker(
                new MarkerOptions().position(latLng)
                        .title("Nowy marker")
                        .draggable(true)
        );

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMarkerDragEnd(Marker marker) {

        LatLng place = marker.getPosition();

        marker.setSnippet(place.latitude + ", " + place.longitude);
        marker.showInfoWindow();

        String lati = String.valueOf(place.latitude);
        String longi = String.valueOf(place.longitude);

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        btncpy = (Button) findViewById(R.id.btnCopy);
        btncpy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtcopy = lati + ", " + longi;
                clipData = ClipData.newPlainText("text", txtcopy);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "Data Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });


    }

}