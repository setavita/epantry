package com.example.teamfoodie.models;

public class Procedure {


    private int recipeID;
    private String step;

    public Procedure(String step) {
        this.setStep(step);
    }

    public Procedure(int id, String step) {
        this.setRecipeID(id);
        this.setStep(step);
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

}
