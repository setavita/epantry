package com.example.teamfoodie.epantry.listAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teamfoodie.R;
import com.example.teamfoodie.database.DatabaseHandler;
import com.example.teamfoodie.models.Recipe;

import java.util.ArrayList;
import java.util.List;
/*
This class is used to create the outline for the recipe list
outlining variable attributes
in which to streamline the performance
 */
public class CustomRecipeListAdapter extends BaseAdapter {

    private List<Recipe> recipeList;
    private List<Recipe> searchList;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomRecipeListAdapter(Context aContext, List<Recipe> recipeList) {
        this.context = aContext;
        this.recipeList = recipeList;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //    Hold the reference to the id of view resource instantiating its context
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.recipe_list_layout, null);
            holder = new ViewHolder();
            holder.recipePicView = (ImageView) convertView.findViewById(R.id.imageView_recipe);
            holder.RecipeNameView = (TextView) convertView.findViewById(R.id.textView_recipeName);
            holder.descriptionView = (TextView) convertView.findViewById(R.id.textView_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Recipe Recipe = this.recipeList.get(position);
        holder.RecipeNameView.setText(Recipe.getRecipeName());
        holder.descriptionView.setText(Recipe.getDescription());


        //int imageId = this.getMipmapResIdByName(Recipe.getPhoto());
        if(!(Recipe.getPhoto()==null)){
            if(isInteger(Recipe.getPhoto())){// checking valid integer using thread
                Integer.parseInt(Recipe.getPhoto());
                holder.recipePicView.setImageResource(Integer.valueOf(Recipe.getPhoto()));
            }else {
                byte[] decodedString = Base64.decode(Recipe.getPhoto(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.recipePicView.setImageBitmap(decodedByte);
                System.out.println("This is not a valid integer number");
            }

        }
////        if(Recipe.getPhoto() != null){
//            holder.recipePicView.setImageResource(Recipe.getPhoto());
//        }


        return convertView;
    }

    //  Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    static class ViewHolder {
        ImageView recipePicView;
        TextView RecipeNameView;
        TextView descriptionView;
    }

    public void filter(String charText, DatabaseHandler db) {
        charText = charText.toLowerCase();
        searchList = new ArrayList<>();
        searchList.addAll(recipeList);
        if (charText.length() == 0) {
            recipeList.clear();
            List<Recipe> fullList = db.loadAllRecipes();
            recipeList.addAll(fullList);
        } else {
            recipeList.clear();
            for (Recipe recipe: searchList) {
                if (recipe.getRecipeName().toLowerCase().contains(charText)) {
                    recipeList.add(recipe);
                }
            }
        }
        notifyDataSetChanged();
    }

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }
}
