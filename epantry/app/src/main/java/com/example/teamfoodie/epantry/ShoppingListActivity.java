package com.example.teamfoodie.epantry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.teamfoodie.R;
import com.example.teamfoodie.database.DatabaseHandler;
import com.example.teamfoodie.epantry.listAdapters.ShoppingListAdapter;
import com.example.teamfoodie.models.Ingredient;
import com.example.teamfoodie.models.PantryIngredient;

import java.util.ArrayList;
import java.util.List;

/*
 * ShoppingListActivity class shows Shopping List from database
 * and user can delete items from database and export it to message
 */
public class ShoppingListActivity extends AppCompatActivity {

    protected RecyclerView idRvMaterial;
    protected CheckBox idCbSelect;
    protected Button idBtnDelete;
    protected ImageView export;
    private Context mContext;
    private ShoppingListActivity mainActivity;
    private SharedPreferences mSharedPreferences;
    private List<String> shoppingListList;
    private List<String> shoppingListSelectList;
    private List<PantryIngredient> pantryList = new ArrayList<>();
    private List<Object> recipeIngList = new ArrayList<>();
    private List<Ingredient> ingRecipeIngList = new ArrayList<>();
    private List<String> lowStockAndMissing = new ArrayList<>();
    private String lowStockAndMissingStr = "";
    List<PantryIngredient> lowStock = new ArrayList<>();
    List<Object> missingIngredients = new ArrayList<>();
    DatabaseHandler dbHandler = new DatabaseHandler(this);
    private int currentUSER_ID;
    private int currentRECIPE_ID;


    public List<PantryIngredient> calculateLowStock(List<PantryIngredient> list) {

        for (PantryIngredient ing : list) {
            if (ing.getCurrentQuantity() < ing.getTotalQuantity()) {
                System.out.println("LOWSTOCK!! ING CURRENT QUAN:" + ing.getCurrentQuantity() + "ING TOTAL QUAN:" + ing.getTotalQuantity());
                lowStock.add(ing);
            }
        }
        System.out.println("LOWSTOCK!! size: " + lowStock.size());

        return lowStock;
    }

    //pass in list of recipe ingredients
    public List<Object> calculateMissingIngredients(List<Ingredient> recipeList) {   //take in recipe list
        System.out.println("calculatemissing()");
        for (int i = 0; i < pantryList.size(); i++) {
            for (int j = 0; j < recipeList.size(); j++) {
                System.out.println("If " + recipeList.get(j).getName().toUpperCase() + " contains " + pantryList.get(i).getIngredientName().toUpperCase());
                if (!recipeList.get(j).getName().toUpperCase().contains(pantryList.get(i).getIngredientName().toUpperCase())) {   //if recipe list doesn't equal pantry list
                    missingIngredients.add(recipeList.get(j).getName());
                    System.out.println("Added to missing: " + recipeList.get(j).toString());
                }
            }
        }
        return missingIngredients;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        currentUSER_ID = extras.getInt("USER_ID");
        System.out.println(currentUSER_ID);
        currentRECIPE_ID = extras.getInt("RECIPE_ID");
        pantryList = dbHandler.loadAllPantryIngredients(currentUSER_ID);
        System.out.println("Size of pantry is " + pantryList.size());

        for (int i = 0; i < pantryList.size(); ++i) {
            Log.i("Shopping list: ", "" + pantryList.get(i).getIngredientInformation());
        }
        lowStock = calculateLowStock(pantryList);
        recipeIngList = dbHandler.loadAllRecipeDetails(currentRECIPE_ID, 1);    //add recipe ingredients to list

        Ingredient i;

        //view recipe ingredient list into
        for (int j = 0; j < recipeIngList.size(); ++j) {
            i = (Ingredient) recipeIngList.get(j);
            System.out.println("List of Recipe Ingredients: ");
            System.out.println(i.getName());

            ingRecipeIngList.add((Ingredient) recipeIngList.get(j));
        }

        //add lowStock <PI> into missing&Low<String>
        for (int j = 0; j < lowStock.size(); ++j) {
            lowStockAndMissing.add(lowStock.get(j).getIngredientName());
            System.out.println("CURRENTLY IN LOWANDMISSING: " + lowStockAndMissing.get(j));
        }
        // addFoodMaterial();
        for (PantryIngredient p : lowStock) {
            System.out.println("After calcLowStock(): " + p.getIngredientName());
        }


        calculateMissingIngredients(ingRecipeIngList);

        //add missing <I> into missing&Low<String>
        for (int j = 0; j < missingIngredients.size(); ++j) {
            lowStockAndMissing.add(missingIngredients.get(j).toString());
            System.out.println("CURRENTLY IN LOWANDMISSING: " + lowStockAndMissing.get(j).toString());
        }


        mContext = this;
        super.setContentView(R.layout.shopping_list);
        shoppingListList = new ArrayList<>();
        shoppingListSelectList = new ArrayList<>();

        mSharedPreferences = mContext.getSharedPreferences("cache",
                Context.MODE_PRIVATE);


        initView();


        final ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter(shoppingListList);
        idRvMaterial.setLayoutManager(new LinearLayoutManager(mContext));
        idRvMaterial.setAdapter(shoppingListAdapter);

        shoppingListAdapter.update(lowStockAndMissing);

        mainActivity = this;
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exportMessage = convertToString(lowStockAndMissing);
                sendSms(mainActivity, "000000", exportMessage);
            }
        });

        idBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idCbSelect.isChecked()) {
                    shoppingListSelectList = shoppingListList;
                }
                int len = shoppingListSelectList.size();
                for (int i = 0; i < len; i++) {
                    String shoppingList = shoppingListSelectList.get(i);
                }
                if (len > 0) {
                    shoppingListSelectList.size();
                }
                Toast.makeText(mContext, "Ingredient successfully deleted.", Toast.LENGTH_SHORT).show();
                ShoppingListActivity.this.finish();

            }
        });
    }

    private void initView() {
        idRvMaterial = findViewById(R.id.recycler_view);
        idCbSelect = findViewById(R.id.id_cb_select);
        idBtnDelete = findViewById(R.id.id_btn_delete);
        export = findViewById(R.id.id_btn_export);
    }

    public static void sendSms(Context context, String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
        context.startActivity(intent);
    }

    public String convertToString(List<String> lsam) {

        for (int i = 0; i < lsam.size(); i++) {
            String result = lsam.get(i);
            lowStockAndMissingStr = lowStockAndMissingStr + result + "\n";
        }
        System.out.println(lowStockAndMissingStr);
        return lowStockAndMissingStr;
    }


}