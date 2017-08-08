package com.coding4fun.apps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.coding4fun.adapters.CategoryRVadapter;
import com.coding4fun.models.Category;
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
 * Created by coding4fun on 31-Oct-16.
 */

public class CategoryFragment extends Fragment implements View.OnClickListener, EditDeleteCallbacks {

    RecyclerView rv;
    CategoryRVadapter adapter;
    FloatingActionButton fab;
    ProgressDialog mProgressDialog;
    List<Category> categoriesList = new ArrayList<>();
    List<Category> searchList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_fragment, container, false);
        //define views here
        rv = (RecyclerView) v.findViewById(R.id.RV_category);
        fab = (FloatingActionButton) v.findViewById(R.id.fab_category);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //code here
        setHasOptionsMenu(true);	//assign menu only for this fragment
        fab.setOnClickListener(this);
        initRV();
        categoriesList.add(null);
        adapter = new CategoryRVadapter(getContext(),categoriesList,this);
        rv.setAdapter(adapter);
        getCategoriesformFBDB();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_category:
                addNewCategoryDialog();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem sItem = menu.findItem(R.id.search);
        SearchView sView = (SearchView) MenuItemCompat.getActionView(sItem);
        sView.setSubmitButtonEnabled(false);
        MenuItemCompat.setOnActionExpandListener(sItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {return true;}
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adapter = new CategoryRVadapter(getContext(),categoriesList,CategoryFragment.this);
                rv.setAdapter(adapter);
                return true;
            }
        });
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}
            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
                //false if the SearchView should perform the default action of showing any suggestions if available,
                // true if the action was handled by the listener.
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:

                return true;
            default:
                return false;
        }
    }*/

    void search(String txt){
        //Log.e("searching","searching for " + txt);
        searchList.clear();
        if(txt.equals("")){
            adapter = new CategoryRVadapter(getContext(),categoriesList,this);
        } else {
            for (Category c : categoriesList) {
                if (c.getName().toLowerCase().contains(txt.toLowerCase())) {
                    searchList.add(c);
                }
            }
            adapter = new CategoryRVadapter(getContext(), searchList, this);
        }
        rv.setAdapter(adapter);
    }

    void addNewCategoryDialog(){
        View v = getActivity().getLayoutInflater().inflate(R.layout.add_dialog,null);
        final EditText et = (EditText) v.findViewById(R.id.add_dialog_ET);
        et.setHint("Category Name");
        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle("Add New Category")
                .setView(v)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String iName = et.getText().toString();
                        if(!iName.equals("")) {
                            addNewCategoryFB(iName);
                        }
                    }
                })
                .setNegativeButton("CANCEL",null)
                .show();
    }

    void addNewCategoryFB(String iName){
        showProgressDialog("Adding New Category...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("categories");
        final String key = ref.push().getKey();
        final Category category = new Category(key,iName);
        ref.child(key).setValue(category, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
                if(databaseError == null) {
                    Toast.makeText(getContext(), "New Category added successfully", Toast.LENGTH_SHORT).show();
                    categoriesList.add(category);
                    adapter.notifyItemInserted(categoriesList.size()-1);
                } else Toast.makeText(getContext(), "Error adding category!\n"+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void getCategoriesformFBDB(){
        FirebaseDatabase.getInstance().getReference("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoriesList.remove(null);
                for (DataSnapshot categorySnapshot: dataSnapshot.getChildren()) {
                    Category c = categorySnapshot.getValue(Category.class);
                    c.setKey(categorySnapshot.getKey());
                    categoriesList.add(c);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    void editCategoryDialog(final int index){
        View v = getActivity().getLayoutInflater().inflate(R.layout.add_dialog,null);
        final EditText et = (EditText) v.findViewById(R.id.add_dialog_ET);
        final String oldName = categoriesList.get(index).getName();
        et.setHint("Category Name");
        et.setText(oldName);
        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle("Edit Category")
                .setView(v)
                .setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newName = et.getText().toString();
                        if(!oldName.equals(newName) && !newName.equals("")) {
                            dialogInterface.dismiss();
                            editCategoryFB(index, newName);
                        }
                    }
                })
                .setNegativeButton("CANCEL",null)
                .show();
    }

    void editCategoryFB(final int index, final String newName){
        showProgressDialog("Editing Category...");
        FirebaseDatabase.getInstance().getReference("categories").child(categoriesList.get(index).getKey()).child("name")
                .setValue(newName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Category edited successfully", Toast.LENGTH_SHORT).show();
                    categoriesList.get(index).setName(newName);
                    adapter.notifyItemChanged(index);
                } else Toast.makeText(getContext(), "Error editing category!\n"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void deleteCategoryFB(final int index){
        showProgressDialog("Deleting category...");
        FirebaseDatabase.getInstance().getReference("categories").child(categoriesList.get(index).getKey())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Category deleted successfully", Toast.LENGTH_SHORT).show();
                    if(searchList.contains(categoriesList.get(index))) searchList.remove(index);
                    categoriesList.remove(index);
                    adapter.notifyItemRemoved(index);
                } else Toast.makeText(getContext(), "Error deleting category!\n"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
        editCategoryDialog(index);
    }

    @Override
    public void delete(int index) {
        deleteCategoryFB(index);
    }

}