package com.example.setavita.epantry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class LandingPageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView restock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        restock = (ImageView) findViewById(R.id.myPantry);
        restock.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.myPantry) {
            Intent intent = new Intent(LandingPageActivity.this, ViewPantryActivity.class);
            startActivity(intent);
        }
    }
}
