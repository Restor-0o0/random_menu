package com.example.random_menu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.random_menu.Data.Item;
import com.example.random_menu.databinding.ListElemFragmentBinding;
import com.example.random_menu.placeholder.PlaceholderContent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    ListElemFragmentBinding binding;
    //private static List<Item> ITEMS;
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment(){
        //super(R.layout.list_elem_fragment);
        //mColumnCount = items.size();
    }
    public ItemFragment(List<Item> items){
        //super(R.layout.list_elem_fragment);
        //ITEMS= items;
        //mColumnCount = items.size();
        //Log.e("eee", String.valueOf(ITEMS.size()));
    }
   // public static boolean add(List<Item> items){
    //    return ITEMS.addAll(items);
   // }
    //public static void clear(){
   //    ITEMS.clear();
   // }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(ArrayList<Item> items) {
        ItemFragment fragment = new ItemFragment(items);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_elem_fragment, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //ITEMS.clear();
            //ITEMS.addAll((ArrayList<Item>) requireArguments().getSerializable("itemsList"));
            //Log.e("errrrrr",String.valueOf(ITEMS.size()));
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS));
        }
        return view;
    }


}