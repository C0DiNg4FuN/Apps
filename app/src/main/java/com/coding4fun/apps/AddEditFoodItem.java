package com.coding4fun.apps;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.aigestudio.wheelpicker.WheelPicker;
import com.coding4fun.models.Category;
import com.coding4fun.models.FoodItem;
import com.coding4fun.models.Ingredient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coding4fun on 04-Nov-16.
 */

public class AddEditFoodItem extends AppCompatActivity {

    EditText foodItemName,description;
    WheelPicker category;
    List<WheelPicker> ingredients = new ArrayList<>();
    List<Ingredient> ingredientsList = new ArrayList<>();
    List<String> ingredientsNames = new ArrayList<>();
    List<Category> categoriesList = new ArrayList<>();
    List<String> categoriesNames = new ArrayList<>();
    //String ingredientsArray[];
    LinearLayout ll;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_food_item);

        foodItemName = (EditText) findViewById(R.id.add_edit_food_item_name);
        description = (EditText) findViewById(R.id.add_edit_food_item_description);
        category = (WheelPicker) findViewById(R.id.add_edit_food_item_wheel_category);
        category.setData(categoriesNames);
        wheelStyle(category,5);
        ll = (LinearLayout) findViewById(R.id.add_edit_food_item_add_ingredients_LL);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_edit_food_item_add_ingredient);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWheel();
            }
        });
        getIngredientsformFBDB();
    }

    void addWheel(){
        WheelPicker w = new WheelPicker(this);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,15,0,0);
        w.setLayoutParams(lp);
        w.setData(ingredientsNames);
        wheelStyle(w,3);
        ingredients.add(w);
        ll.addView(w);
    }

    void wheelStyle(WheelPicker w, int visibleItems){
        w.setVisibleItemCount(visibleItems);
        w.setIndicator(true);
        w.setIndicatorColor(getResources().getColor(R.color.green));
        w.setItemTextColor(getResources().getColor(R.color.green));
        w.setCurved(true);
        w.setAtmospheric(true);
        w.setSelectedItemPosition(visibleItems/2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                //TODO: save to FBDB then close activity
                List<Ingredient> tempIngredients = new ArrayList<>();
                for(WheelPicker w : ingredients) tempIngredients.add(ingredientsList.get(w.getCurrentItemPosition()));
                //String c = ca;
                FoodItem fi = new FoodItem(foodItemName.getText().toString(),"","");
                return true;
            default:
                return false;
        }
    }

    void getIngredientsformFBDB(){
        showProgressDialog("Wait a moment...");
        FirebaseDatabase.getInstance().getReference("ingredients").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ingredientSnapshot: dataSnapshot.getChildren()) {
                    Ingredient i = ingredientSnapshot.getValue(Ingredient.class);
                    i.setKey(ingredientSnapshot.getKey());
                    ingredientsList.add(i);
                    ingredientsNames.add(i.getName());
                }
                getCategoriesformFBDB();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    void getCategoriesformFBDB(){
        FirebaseDatabase.getInstance().getReference("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot categorySnapshot: dataSnapshot.getChildren()) {
                    Category c = categorySnapshot.getValue(Category.class);
                    c.setKey(categorySnapshot.getKey());
                    categoriesList.add(c);
                    categoriesNames.add(c.getName());
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void showProgressDialog(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(title);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}