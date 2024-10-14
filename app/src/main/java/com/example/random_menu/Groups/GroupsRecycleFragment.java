package com.example.random_menu.Groups;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Item;
import com.example.random_menu.Elements.ElementsActivity;
import com.example.random_menu.R;
import com.example.random_menu.databinding.ListElemFragmentBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class GroupsRecycleFragment extends Fragment {

    // TODO: Customize parameter argument names
    ListElemFragmentBinding binding;
    //private static List<Item> ITEMS;
    // TODO: Customize parameters
    private int moreViewItemId;

    private static GroupsRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ListElemFragmentBinding.inflate(inflater, container, false);
        //View view = inflater.inflate(R.layout.list_elem_fragment, container, false);


        adapter = new GroupsRecyclerViewAdapter(GroupPlaceholderContent.GROUPS,(position,id, name) ->{

            //открытия окна more(что бы открывалось рядом с элментоом и не вызалило за пределы экрана)
            moreViewItemId = Integer.valueOf(id);
            ViewGroup.MarginLayoutParams lay = (ViewGroup.MarginLayoutParams) binding.moreItemView.getLayoutParams();
            lay.topMargin = position;
            if((binding.moreItemView.getHeight() + position) * 1.1 < binding.getRoot().getHeight()){
                Log.e("cord",String.valueOf(binding.moreItemView.getHeight() + position)+ " " + String.valueOf(binding.getRoot().getHeight()));
                binding.moreItemView.setLayoutParams(lay);
            }
            else{
                lay.topMargin = position - binding.moreItemView.getHeight();
                binding.moreItemView.setLayoutParams(lay);
            }
            //Log.e("texst","in");
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
                //переход к элементам группы
                (id,name) ->{
                    ElemPlaceholderContent.idSelectGroup = id;
                    Intent intent = new Intent(getActivity(), ElementsActivity.class);
                    startActivity(intent);
        });

        // Set the adapter
        binding.list1.setAdapter(adapter);

        DividerItemDecoration decorator = new DividerItemDecoration(binding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
        binding.list1.addItemDecoration(decorator);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(TouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.list1);
        //return view;ишт

        //binding = ListElemFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    ItemTouchHelper.SimpleCallback TouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {


            int fromPosition = (int) viewHolder.getAbsoluteAdapterPosition();
            int toPosition = (int) target.getAbsoluteAdapterPosition();
            GroupPlaceholderContent.swap(fromPosition,toPosition);

            //Collections.swap((List<?>) GroupPlaceholderContent.GROUPS, fromPosition, toPosition);
            try{
                binding.list1.getAdapter().notifyItemMoved(fromPosition, toPosition);
                binding.list1.getAdapter().notifyItemChanged(fromPosition);
                binding.list1.getAdapter().notifyItemChanged(toPosition);
                //binding.list1.getAdapter().notifyDataSetChanged();
                GroupPlaceholderContent.loadGroups();
            }
            catch(Exception e){
                Log.e("onMoveListenerError",e.toString());
            }
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.deleteItemBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("coooon","_+_"+String.valueOf(ContentProviderDB.query(MainBaseContract.ElemGroup.TABLE_NAME,null,MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = " + moreViewItemId + " and "+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " NOT IN (SELECT " +MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " FROM "+MainBaseContract.ElemGroup.TABLE_NAME+" WHERE " +MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " != " + moreViewItemId + " and "+MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT+ " IN (SELECT "+MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT+" FROM "+MainBaseContract.ElemGroup.TABLE_NAME+" WHERE "+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+" = "+moreViewItemId+"))",null,null,null,null).getCount()));
                Cursor curr;
                curr = ContentProviderDB.query(MainBaseContract.ElemGroup.TABLE_NAME,null,MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = " + moreViewItemId + " and "+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " NOT IN (SELECT " +MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " FROM "+MainBaseContract.ElemGroup.TABLE_NAME+" WHERE " +MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " != " + moreViewItemId + " and "+MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT+ " IN (SELECT "+MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT+" FROM "+MainBaseContract.ElemGroup.TABLE_NAME+" WHERE "+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+" = "+moreViewItemId+"))",null,null,null,null);
                Log.e("coooon",String.valueOf(curr.getCount())+"_"+moreViewItemId);
                List<String> ids = new ArrayList<String>();

                if(curr.getCount() > 0){
                    curr.moveToFirst();
                    do{
                        ids.add(curr.getString(curr.getColumnIndexOrThrow(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT)));
                        Log.e("coooon","_"+curr.getString(curr.getColumnIndexOrThrow(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT)));
                    }while(curr.moveToNext());
                }
                ContentProviderDB.delete(MainBaseContract.ElemGroup.TABLE_NAME,MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = " + String.valueOf(moreViewItemId),null);

                if(ids.size() > 0){
                    for(int i = 0;i < ids.size();i++){
                        Log.e("coooon","_"+ids.get(i));
                        ContentProviderDB.delete(MainBaseContract.Components.TABLE_NAME,MainBaseContract.Components.COLUMN_NAME_ELEMENT + " = "+ ids.get(i),null);
                        ContentProviderDB.delete(MainBaseContract.Elements.TABLE_NAME,MainBaseContract.Elements._ID + " = "+ ids.get(i),null);
                    }

                }
                ContentProviderDB.delete(MainBaseContract.Groups.TABLE_NAME,MainBaseContract.Groups._ID + " = " + String.valueOf(moreViewItemId),null);


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
                GroupPlaceholderContent.loadGroups();
                binding.list1.setAdapter(adapter);
            }
        });

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
    public GroupsRecycleFragment(){
        //super(R.layout.list_elem_fragment);
        //mColumnCount = items.size();
    }
    public GroupsRecycleFragment(List<Item> items){
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
    public static GroupsRecycleFragment newInstance(ArrayList<Item> items) {
        GroupsRecycleFragment fragment = new GroupsRecycleFragment(items);
        return fragment;
    }



}