package com.example.setavita.epantry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.setavita.epantry.R;

public class ViewPantryActivity extends AppCompatActivity {

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pantry);

        configureAddButton();
    }

    private void configureAddButton(){
        ImageView addButton = (ImageView) findViewById(R.id.addIngredient);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(ViewPantryActivity.this, PantryScannerActivity.class));
            }
        });

    }
}