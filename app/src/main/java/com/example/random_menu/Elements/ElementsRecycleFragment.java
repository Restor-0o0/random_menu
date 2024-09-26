package com.example.random_menu.Elements;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Item;
import com.example.random_menu.Groups.GroupsRecyclerViewAdapter;
import com.example.random_menu.R;
import com.example.random_menu.databinding.ListElemFragmentBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ElementsRecycleFragment extends Fragment {

    // TODO: Customize parameter argument names
    ListElemFragmentBinding binding;
    //private static List<Item> ITEMS;
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private ElementsRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ListElemFragmentBinding.inflate(inflater, container, false);
        //View view = inflater.inflate(R.layout.list_elem_fragment, container, false);


        adapter = new ElementsRecyclerViewAdapter(ElemPlaceholderContent.getElements(),(position, number) ->{

            Log.e("texst","in");
            binding.moreItemView.setVisibility(View.VISIBLE);

            binding.closeView.setVisibility(View.VISIBLE);

            Animation anim = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.anim_show);
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            binding.closeView.setScaleY(1.0f);
            binding.moreItemView.setScaleY(1.0f);
            binding.moreItemView.startAnimation(anim);

        },
                (position,number) ->{

        });

        // Set the adapter
        binding.list1.setAdapter(adapter);
        //return view;


        //binding = ListElemFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.closeView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Animation anim = AnimationUtils.loadAnimation(binding.getRoot().getContext(),R.anim.anim_hide);
                 anim.setDuration(100);
                 anim.setAnimationListener(new Animation.AnimationListener() {
                     @Override
                     public void onAnimationStart(Animation animation) {

                     }

                     @Override
                     public void onAnimationEnd(Animation animation) {
                         binding.closeView.setScaleY(0.0f);
                         binding.moreItemView.setScaleY(0.0f);
                         binding.closeView.setVisibility(View.INVISIBLE);
                         binding.moreItemView.setVisibility(View.INVISIBLE);
                     }

                     @Override
                     public void onAnimationRepeat(Animation animation) {

                     }
                 });


                 binding.moreItemView.startAnimation(anim);


             }
        }
        );
    }



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ElementsRecycleFragment(){
        //super(R.layout.list_elem_fragment);
        //mColumnCount = items.size();
    }
    public ElementsRecycleFragment(List<Item> items){
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
    public static ElementsRecycleFragment newInstance(ArrayList<Item> items) {
        ElementsRecycleFragment fragment = new ElementsRecycleFragment(items);
        return fragment;
    }



}