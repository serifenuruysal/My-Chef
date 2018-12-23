package com.soulkitchen.serifenuruysal.mychef.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.soulkitchen.serifenuruysal.mychef.adapters.CustomInfoWindowGoogleMap;
import com.soulkitchen.serifenuruysal.mychef.utils.PermissionUtils;
import com.soulkitchen.serifenuruysal.mychef.R;
import com.soulkitchen.serifenuruysal.mychef.utils.Utils;
import com.soulkitchen.serifenuruysal.mychef.models.Meal;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        SeekBar.OnSeekBarChangeListener,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnInfoWindowCloseListener, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap mMap;
    private TextView mTextMessage;
    private FirebaseFirestore mDatabase;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mShowPermissionDeniedDialog = false;
    private FloatingActionButton fab;
    final ArrayList<Marker> mList = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseFirestore.getInstance();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            if (!mMap.isMyLocationEnabled())
                mMap.setMyLocationEnabled(true);
            Location myLocation = Utils.getCurrentLocation(this);
            if (myLocation != null) {
                LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12), 1500, null);
            }

        } else {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }

        mDatabase.collection("Menu")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mList.clear();;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Meal meal = document.toObject(Meal.class);
                                if (meal.getGeoLocation()!=null){
                                    LatLng latlong = new LatLng(meal.getGeoLocation().getLatitude(), meal.getGeoLocation().getLongitude());
                                    MarkerOptions marker = new MarkerOptions().position(latlong).title(meal.getName());
                                    marker.snippet(meal.getDescription());
                                    BitmapDescriptor locationMarkerIcon = Utils.getBitmapFromVector(getApplicationContext(), R.drawable.ic_baker,
                                            ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                                    marker.icon(locationMarkerIcon);

                                    CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(MainActivity.this);
                                    mMap.setInfoWindowAdapter(customInfoWindow);

                                    Marker m =mMap.addMarker(marker);
                                    m.setTag(meal);
                                    mList.add(m);

//                                    m.showInfoWindow();
                                }
                            }

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (Marker marker : mList) {
                                builder.include(marker.getPosition());
                            }
                            LatLngBounds bounds = builder.build();
                            int width = getResources().getDisplayMetrics().widthPixels;
                            int height = getResources().getDisplayMetrics().heightPixels;
                            int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            mMap.animateCamera(cu);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }




    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, results,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            mMap.setMyLocationEnabled(true);
        } else {
            mShowPermissionDeniedDialog = true;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Do nothing.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Do nothing.
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
//        Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
//        Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
//        Toast.makeText(this, "Info Window long click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);
        Toast.makeText(this, marker.getTitle() + " z-index set to " + zIndex,
                Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == fab.getId()) {
            Intent intent=new Intent(MainActivity.this,AddMealActivity.class);
            startActivity(intent);
        }
    }
}
