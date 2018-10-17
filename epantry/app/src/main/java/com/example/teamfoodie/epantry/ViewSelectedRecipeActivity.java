package com.example.teamfoodie.epantry;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.teamfoodie.R;
import com.example.teamfoodie.database.DatabaseHandler;
import com.example.teamfoodie.database.RecipeIngredientsTable;
import com.example.teamfoodie.epantry.listAdapters.CustomRecipeAdapter;
import com.example.teamfoodie.models.PantryIngredient;
import com.example.teamfoodie.models.Recipe;

import java.util.List;

public class ViewSelectedRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView recipePhoto;
    private TextView recipeName;
    private TextView recipeCooking;
    private TextView recipeCalories;
    private TextView recipePeople;
    private LinearLayout ingredientsListView;
    private LinearLayout proceduresListView;
    private DatabaseHandler dbHandler;
    private Button makeRecipe;
    private Button changeServes;
    private List<Object> ingredientList;
    private List<Object> procedureList;
    private List<PantryIngredient> pantryList;
    private int currentUSER_ID;
    private int currentRECIPE_ID;
    private int currentNumberOfPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_selected_recipe);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                System.out.println("Bundle extra was NULL user");
            } else {
                currentRECIPE_ID = extras.getInt("RECIPE_ID");
            }
        } else {
            currentRECIPE_ID = (Integer) savedInstanceState.getSerializable("RECIPE_ID");
            System.out.println("savedInstance was NULL");
        }

        this.recipePhoto = (ImageView) findViewById(R.id.recipe_photo);
        this.recipeName = (TextView) findViewById(R.id.recipe_name);
        this.recipeCooking = (TextView) findViewById(R.id.recipe_cooking_time);
        this.recipeCalories = (TextView) findViewById(R.id.recipe_calories);
        this.recipePeople = (TextView) findViewById(R.id.recipe_number_of_people);
        this.ingredientsListView = (LinearLayout) findViewById(R.id.recipeIngredientsList);
        this.proceduresListView = (LinearLayout) findViewById(R.id.recipeProcedureList);
        this.makeRecipe = (Button) findViewById(R.id.makeRecipeBtn);
        this.changeServes = (Button) findViewById(R.id.btnChangeServes);
        this.dbHandler = new DatabaseHandler(this);

        this.makeRecipe.setOnClickListener(this);
        this.changeServes.setOnClickListener(this);

        Object obj = dbHandler.findHandle(String.valueOf(currentRECIPE_ID), "StoredRecipe");
        Recipe recipe = (Recipe) obj;
        setInformation(recipe);

        this.ingredientList = dbHandler.loadAllRecipeDetails(currentRECIPE_ID, 1);
        this.procedureList = dbHandler.loadAllRecipeDetails(currentRECIPE_ID, 2);
        compileRecipeDetails(ingredientList, procedureList);

    }

    public void setInformation(Recipe recipe) {
        this.recipePhoto.setImageResource(recipe.getPhoto());
        this.recipeName.setText("Name: " + recipe.getRecipeName());
        this.recipeCooking.setText("Cooking Time: " + recipe.getCookingTime() + " minutes");
        this.recipeCalories.setText("Calorie Count: " + recipe.getCalorieCount());
        this.recipePeople.setText("Serves: " + recipe.getNumberOfPeople() + " people");
        currentNumberOfPeople = recipe.getNumberOfPeople();


    }

    public void compileRecipeDetails(List<Object> ingredients, List<Object> procedures) {
        CustomRecipeAdapter adapter = new CustomRecipeAdapter(this, ingredients, 1);
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, ingredientsListView);
            ingredientsListView.addView(view);
        }

        System.out.println();
        adapter.setListType(2, procedures);
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, proceduresListView);
            proceduresListView.addView(view);
        }
    }

    public void setAlertDialog(String message) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(message);
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            int newNumberOfPeople = 0;
                try{
                    newNumberOfPeople = Integer.parseInt(input.getText().toString());
                }catch (NumberFormatException numberExecption){
                    setAlertDialog("Please enter in numbers ONLY");
                }

                if(newNumberOfPeople != 0){
                    List<Object> UPDATED_INGREDIENT_LIST = RecipeIngredientsTable.calculateNewMeasurements(ingredientList, currentNumberOfPeople, newNumberOfPeople);
                    ingredientsListView.removeAllViews();
                    compileRecipeDetails(UPDATED_INGREDIENT_LIST, procedureList);
//                    startActivity(getIntent());

                }
            }
        });
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnChangeServes){
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setTitle("Number of people to serve");
        dialog.setMessage("Recipe currently cooks for " + currentNumberOfPeople + " people. Do you want to change this?");
        dialog.setNegativeButton("No", null);
        dialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setAlertDialog("Set new number of people to cook for");
                    }
                });

        dialog.show();

    }  else if(v.getId() == R.id.makeRecipeBtn) {
        pantryList = dbHandler.loadAllPantryIngredients(currentUSER_ID);
        System.out.println(pantryList.get(0).getIngredientName());
        System.out.println(pantryList.get(0).getCurrentQuantity());
        pantryList.get(0).setTotalQuantity(100);
        pantryList.get(0).setCurrentQuantity(1);
        System.out.println("TOTAL: "+pantryList.get(0).getTotalQuantity()+ "CURRENT: "+pantryList.get(0).getCurrentQuantity());





//        MyPreferences mypref = new MyPreferences();
//        mypref.calculateThreshold(pantryList);
    }
    }
}
