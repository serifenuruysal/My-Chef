package com.soulkitchen.serifenuruysal.mychef.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.soulkitchen.serifenuruysal.mychef.R;
import com.soulkitchen.serifenuruysal.mychef.adapters.MyRecyclerViewAdapter;
import com.soulkitchen.serifenuruysal.mychef.models.Meal;

import java.util.ArrayList;

public class ProfilPageActivity extends AppCompatActivity {
    Meal meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabb);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfilPageActivity.this,AddMealActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent()!=null&&getIntent().getParcelableExtra("MARKER")!=null){
            meal=getIntent().getParcelableExtra("MARKER");
        }
        TextView name_tv = findViewById(R.id.name);
        TextView details_tv = findViewById(R.id.details);
        ImageView img = findViewById(R.id.pic);

        TextView hotel_tv = findViewById(R.id.hotels);
        TextView food_tv = findViewById(R.id.food);
        TextView transport_tv = findViewById(R.id.transport);

        name_tv.setText(meal.getName());
        details_tv.setText(meal.getDescription());
        hotel_tv.setText("Porsiyon adedi: "+meal.getCount());
        food_tv.setText("Ücret: "+meal.getPrice()+" TL");
        transport_tv.setText("Şef: "+meal.getMealOwner());

        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Yemekler cok lezzetli");
        animalNames.add("Cok aciktim hemen gelsin.com");
        animalNames.add("Cok kotu lezzetsiz");
        animalNames.add("Sefi tebrik ederim.");
        animalNames.add("Bu da mi gol degil.");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, animalNames);
        recyclerView.setAdapter(adapter);
    }

}
