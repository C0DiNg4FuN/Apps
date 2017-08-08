package com.coding4fun.apps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.coding4fun.adapters.IngredientRVadapter;
import com.coding4fun.models.Ingredient;
import com.coding4fun.utils.EditDeleteCallbacks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coding4fun on 29-Oct-16.
 */

public class IngredientFragment extends Fragment implements View.OnClickListener, EditDeleteCallbacks {

    RecyclerView rv;
    IngredientRVadapter adapter;
    FloatingActionButton fab;
    ProgressDialog mProgressDialog;
    List<Ingredient> ingredientsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ingredient_fragment, container, false);
        //define views here
        rv = (RecyclerView) v.findViewById(R.id.RV_ingredient);
        fab = (FloatingActionButton) v.findViewById(R.id.fab_ingredient);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //code here
        fab.setOnClickListener(this);
        initRV();
        ingredientsList.add(null);
        adapter = new IngredientRVadapter(getContext(),ingredientsList,this);
        rv.setAdapter(adapter);
        getIngredientsformFBDB();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_ingredient:
                addNewIngredientDialog();
                break;
            default:
                break;
        }
    }

    private void initRV(){
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        DefaultItemAnimator anim = new DefaultItemAnimator();
        anim.setAddDuration(500);
        anim.setRemoveDuration(500);
        rv.setItemAnimator(anim);
    }

    void getIngredientsformFBDB(){
        Log.e("ing","getting ingredients");
        FirebaseDatabase.getInstance().getReference("ingredients").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("ing","DONE getting ingredients");
                ingredientsList.remove(null);
                for (DataSnapshot ingredientSnapshot: dataSnapshot.getChildren()) {
                    //Ingredient i = ingredientSnapshot.getValue(Ingredient.class);
                    //Ingredient i = new Ingredient(ingredientSnapshot.getKey());
                    Ingredient i = ingredientSnapshot.getValue(Ingredient.class);
                    i.setKey(ingredientSnapshot.getKey());
                    ingredientsList.add(i);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    void addNewIngredientDialog(){
        View v = getActivity().getLayoutInflater().inflate(R.layout.add_dialog,null);
        final EditText et = (EditText) v.findViewById(R.id.add_dialog_ET);
        et.setHint("Ingredient Name");
        new AlertDialog.Builder(getContext())
        .setCancelable(true)
        .setTitle("Add New Ingredient")
        .setView(v)
        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String iName = et.getText().toString();
                if(!iName.equals("")) {
                    addNewIngredientFB(iName);
                }
            }
        })
        .setNegativeButton("CANCEL",null)
        .show();
    }

    void addNewIngredientFB(String iName){
        showProgressDialog("Adding New Ingredient...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ingredients");
        final String key = ref.push().getKey();
        final Ingredient ingredient = new Ingredient(key,iName);
        ref.child(key).setValue(ingredient, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
                if(databaseError == null) {
                    Toast.makeText(getContext(), "New Ingredient added successfully", Toast.LENGTH_SHORT).show();
                    ingredientsList.add(ingredient);
                    adapter.notifyItemInserted(ingredientsList.size()-1);
                } else Toast.makeText(getContext(), "Error adding ingredient!\n"+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void editIngredientDialog(final int index){
        View v = getActivity().getLayoutInflater().inflate(R.layout.add_dialog,null);
        final EditText et = (EditText) v.findViewById(R.id.add_dialog_ET);
        final String oldName = ingredientsList.get(index).getName();
        et.setHint("Ingredient Name");
        et.setText(oldName);
        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle("Edit Ingredient")
                .setView(v)
                .setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newName = et.getText().toString();
                        if(!oldName.equals(newName) && !newName.equals("")) {
                            dialogInterface.dismiss();
                            editIngredientFB(index, newName);
                        }
                    }
                })
                .setNegativeButton("CANCEL",null)
                .show();
    }

    void editIngredientFB(final int index, String newName){
        showProgressDialog("Editing Ingredient...");
        ingredientsList.get(index).setName(newName);
        FirebaseDatabase.getInstance().getReference("ingredients").child(ingredientsList.get(index).getKey()).child("name")
                .setValue(ingredientsList.get(index).getName()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Ingredient edited successfully", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemChanged(index);
                } else Toast.makeText(getContext(), "Error editing ingredient!\n"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void deleteIngredientFB(final int index){
        showProgressDialog("Deleting ingredient...");
        FirebaseDatabase.getInstance().getReference("ingredients").child(ingredientsList.get(index).getKey())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Ingredient deleted successfully", Toast.LENGTH_SHORT).show();
                    ingredientsList.remove(index);
                    adapter.notifyItemRemoved(index);
                } else Toast.makeText(getContext(), "Error deleting ingredient!\n"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showProgressDialog(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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

    @Override
    public void edit(int index) {
        editIngredientDialog(index);
    }

    @Override
    public void delete(int index) {
        deleteIngredientFB(index);
    }

}