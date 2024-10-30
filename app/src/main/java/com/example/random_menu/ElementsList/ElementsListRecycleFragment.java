package com.example.random_menu.ElementsList;

import static android.widget.RelativeLayout.*;

import android.content.Intent;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Item;
import com.example.random_menu.Element.ElementActivity;
import com.example.random_menu.R;
import com.example.random_menu.databinding.ListFragmentBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ElementsListRecycleFragment extends Fragment {

    // TODO: Customize parameter argument names
    ListFragmentBinding binding;
    // TODO: Customize parameters
    //id элемента для которого вызвано moreView
    private int moreViewItemId;

    private ElementsListRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ListFragmentBinding.inflate(inflater, container, false);
        //View view = inflater.inflate(R.layout.list_elem_fragment, container, false);


        adapter = new ElementsListRecyclerViewAdapter(ElemPlaceholderContent.getElements(),
                (position,id, number) ->{//функция для отрисовки moreView
            //выхватываем id элемента списка
            moreViewItemId = Integer.valueOf(id);
            //формируем параметры для отступов окна от границ экрана
            MarginLayoutParams lay = (MarginLayoutParams) binding.moreItemView.getLayoutParams();
            lay.topMargin = position;
            if((binding.moreItemView.getHeight() + position) * 1.1 < binding.getRoot().getHeight()){
                //Log.e("cord",String.valueOf(binding.moreItemView.getHeight() + position)+ " " + String.valueOf(binding.getRoot().getHeight()));
               binding.moreItemView.setLayoutParams(lay);
            }
            else{
                lay.topMargin = position - binding.moreItemView.getHeight();
                binding.moreItemView.setLayoutParams(lay);
            }
            //Log.e("texst","in");
            binding.moreItemView.setVisibility(View.VISIBLE);

            binding.closeView.setVisibility(View.VISIBLE);
            //анимация рскрытия окна
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
                (id,name) ->{
                    Log.e("list111",name);

                    ComponentPlaceholderContent.idSelectElem = id;
                    //ComponentPlaceholderContent.loadComponents();
                    Intent intent = new Intent(getActivity(), ElementActivity.class);
                    startActivity(intent);
        });

        // Set the adapter
        binding.list1.setAdapter(adapter);
        //return view;

        //декоратор и помошник нажатий для перетаскивания элементов по списку и тем самым изменения их приоритетов
        DividerItemDecoration decorator = new DividerItemDecoration(binding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
        binding.list1.addItemDecoration(decorator);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(TouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.list1);
        //binding = ListElemFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    //обработчик перетаскиваний
    ItemTouchHelper.SimpleCallback TouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {


            int fromPosition = (int) viewHolder.getAbsoluteAdapterPosition();
            int toPosition = (int) target.getAbsoluteAdapterPosition();
            ElemPlaceholderContent.swap(fromPosition,toPosition);

            //Collections.swap((List<?>) GroupPlaceholderContent.GROUPS, fromPosition, toPosition);
            try{
                binding.list1.getAdapter().notifyItemMoved(fromPosition, toPosition);
                binding.list1.getAdapter().notifyItemChanged(fromPosition);
                binding.list1.getAdapter().notifyItemChanged(toPosition);
                //binding.list1.getAdapter().notifyDataSetChanged();
                ElemPlaceholderContent.loadElements();
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
    public void onResume() {
        super.onResume();

        adapter = new ElementsListRecyclerViewAdapter(ElemPlaceholderContent.getElements(),
                (position,id, number) ->{//функция для отрисовки moreView
                    //выхватываем id элемента списка
                    moreViewItemId = Integer.valueOf(id);
                    //формируем параметры для отступов окна от границ экрана
                    MarginLayoutParams lay = (MarginLayoutParams) binding.moreItemView.getLayoutParams();
                    lay.topMargin = position;
                    if((binding.moreItemView.getHeight() + position) * 1.1 < binding.getRoot().getHeight()){
                        //Log.e("cord",String.valueOf(binding.moreItemView.getHeight() + position)+ " " + String.valueOf(binding.getRoot().getHeight()));
                        binding.moreItemView.setLayoutParams(lay);
                    }
                    else{
                        lay.topMargin = position - binding.moreItemView.getHeight();
                        binding.moreItemView.setLayoutParams(lay);
                    }
                    //Log.e("texst","in");
                    binding.moreItemView.setVisibility(View.VISIBLE);

                    binding.closeView.setVisibility(View.VISIBLE);
                    //анимация рскрытия окна
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
                (id,name) ->{
                    Log.e("list111",name);

                    ComponentPlaceholderContent.idSelectElem = id;
                    //ComponentPlaceholderContent.loadComponents();
                    Intent intent = new Intent(getActivity(), ElementActivity.class);
                    startActivity(intent);
                });

        // Set the adapter
        binding.list1.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.deleteItemBut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentProviderDB.delete(MainBaseContract.ElemGroup.TABLE_NAME,MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " = " + String.valueOf(moreViewItemId),null);
                ContentProviderDB.delete(MainBaseContract.Components.TABLE_NAME,MainBaseContract.Components.COLUMN_NAME_ELEMENT + " = " + String.valueOf(moreViewItemId),null);
                ContentProviderDB.delete(MainBaseContract.Elements.TABLE_NAME,MainBaseContract.Elements._ID + " = " + String.valueOf(moreViewItemId),null);
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
                ElemPlaceholderContent.loadElements();
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
    public ElementsListRecycleFragment(){
        //super(R.layout.list_elem_fragment);
        //mColumnCount = items.size();
    }
    public ElementsListRecycleFragment(List<Item> items){
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
    public static ElementsListRecycleFragment newInstance(ArrayList<Item> items) {
        ElementsListRecycleFragment fragment = new ElementsListRecycleFragment(items);
        return fragment;
    }




}