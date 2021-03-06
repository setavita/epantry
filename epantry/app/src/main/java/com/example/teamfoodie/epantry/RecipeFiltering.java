package com.example.teamfoodie.epantry;

import com.example.teamfoodie.database.DatabaseHandler;
import com.example.teamfoodie.models.Ingredient;
import com.example.teamfoodie.models.PantryIngredient;
import com.example.teamfoodie.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeFiltering {

    private static int RECIPE_INDEX;
    private static int PANTRY_INDEX;
    private static int RINGREDIENT_INDEX;


    public static List<Recipe> getRecipeList(List<Recipe> recipes, List<PantryIngredient> pantryIngredients, DatabaseHandler dbHandler) {
        List<Object> recipeIngredients;
        List<Recipe> matchingRecipeList = new ArrayList<>();
        int matches = 0;

        for (RECIPE_INDEX = 0; RECIPE_INDEX < recipes.size(); RECIPE_INDEX++) {
            recipeIngredients = dbHandler.loadAllRecipeDetails(recipes.get(RECIPE_INDEX).getRecipeID(), 1);

            for (RINGREDIENT_INDEX = 0; RINGREDIENT_INDEX < recipeIngredients.size(); RINGREDIENT_INDEX++) {
                Ingredient ingredient = (Ingredient) recipeIngredients.get(RINGREDIENT_INDEX);

                for (PANTRY_INDEX = 0; PANTRY_INDEX < pantryIngredients.size(); PANTRY_INDEX++) {
                    String pantryIng = pantryIngredients.get(PANTRY_INDEX).getIngredientName().toLowerCase();
                    String recipeIng = ingredient.getName().toLowerCase();

                    if (recipeIng.contains(pantryIng) || recipeIng.contains("water")) {
                        matches++;
                    }else
                        break;
                }

                if (matches >= recipeIngredients.size()) {
                    matchingRecipeList.add(recipes.get(RECIPE_INDEX));
                }
                matches = 0;

            }


        }


        return matchingRecipeList;
    }
}
