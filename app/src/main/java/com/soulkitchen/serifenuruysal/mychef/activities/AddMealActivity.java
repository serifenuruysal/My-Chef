package com.soulkitchen.serifenuruysal.mychef.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.soulkitchen.serifenuruysal.mychef.utils.PermissionUtils;
import com.soulkitchen.serifenuruysal.mychef.R;
import com.soulkitchen.serifenuruysal.mychef.utils.Utils;
import com.soulkitchen.serifenuruysal.mychef.models.Meal;

import java.util.Date;


public class AddMealActivity extends AppCompatActivity implements GoogleMap.OnMarkerDragListener, OnMapReadyCallback {
    public static final String TAG=AddMealActivity.class.getSimpleName();
    private GoogleMap mMap;
    private GeoPoint chefsGeoPoint;
    private FirebaseFirestore mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseFirestore.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        chefsGeoPoint=Utils.getCurrentGeoPoint(getApplicationContext());

        final EditText mealName = (EditText) findViewById(R.id.editText);
        final EditText mealDescription = (EditText) findViewById(R.id.etDescription);
        final EditText mealPrice = (EditText) findViewById(R.id.etPrice);
        final EditText mealCount = (EditText) findViewById(R.id.etPorsionCount);
        final EditText mealOwner = (EditText) findViewById(R.id.editText3);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(mealCount.getText()==null||mealName.getText()==null||mealDescription.getText()==null||mealOwner.getText()==null||mealPrice.getText()==null){
                    Snackbar.make(view, "Lutfen butun alanlari eksiksiz doldurunuz!", Snackbar.LENGTH_LONG)
                            .setAction("Uyari", null).show();
                    return;
                }
                Meal meal = new Meal();
                meal.setCount(Integer.parseInt(mealCount.getText().toString()));
                meal.setDate(new Date());
                meal.setDescription(mealDescription.getText().toString());
                meal.setName(mealName.getText().toString());
                meal.setPrice(Double.parseDouble(mealPrice.getText().toString()));
                meal.setMealOwner(mealOwner.getText().toString());
                meal.setGeoLocation(chefsGeoPoint);

                mDatabase.collection("Menu")
                        .add(meal)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Snackbar.make(view, "Basarili bir sekilde kayit yapildi!", Snackbar.LENGTH_LONG)
                                        .setAction("Uyari", null).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                Snackbar.make(view, "Kaydedilirken bir hata olustu!"+e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                                        .setAction("Uyari", null).show();
                            }
                        });

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerDragListener(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddMealActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            if (!mMap.isMyLocationEnabled())
                mMap.setMyLocationEnabled(true);

            Location myLocation = Utils.getCurrentLocation(this);
            if (myLocation != null) {
                LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12), 1500, null);
                MarkerOptions marker = new MarkerOptions().position(userLocation).title("Şefin Mutfagını Haritadan surukleyip birakabilirsin!");
                BitmapDescriptor locationMarkerIcon = Utils.getBitmapFromVector(getApplicationContext(), R.drawable.ic_baker,
                        ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                marker.icon(locationMarkerIcon);
                marker.draggable(true);
                mMap.addMarker(marker);
            }

        } else {
            PermissionUtils.requestPermission(this, 99,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        chefsGeoPoint=new GeoPoint(marker.getPosition().latitude,marker.getPosition().longitude);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        chefsGeoPoint=new GeoPoint(marker.getPosition().latitude,marker.getPosition().longitude);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        chefsGeoPoint=new GeoPoint(marker.getPosition().latitude,marker.getPosition().longitude);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (requestCode != 99) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, results,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            mMap.setMyLocationEnabled(true);
        }
    }
}
