package com.example.random_menu.Element;

import static android.view.ViewGroup.MarginLayoutParams;
import static android.view.View.OnClickListener;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Item;
import com.example.random_menu.ElementsList.ElementsListRecyclerViewAdapter;
import com.example.random_menu.R;
import com.example.random_menu.databinding.ListComponentsFragmentBinding;
import com.example.random_menu.databinding.ListFragmentBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import java.util.ArrayList;
import java.util.List;


public class ComponentsRecycleFragment extends Fragment {

    // TODO: Customize parameter argument names
    ListComponentsFragmentBinding binding;
    // TODO: Customize parameters
    //id элемента для которого вызвано moreView
    private int moreViewItemId;
    boolean isKeyboardShowing = false;

    private ComponentRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ListComponentsFragmentBinding.inflate(inflater, container, false);

        adapter = new ComponentRecyclerViewAdapter(ComponentPlaceholderContent.getComponents(),
                (position,id, number) ->{//функция для отрисовки moreView
            //выхватываем id элемента списка
            moreViewItemId = Integer.valueOf(id);
            //формируем параметры для отступов окна от границ экрана
            MarginLayoutParams lay = (MarginLayoutParams) binding.moreItemView.getLayoutParams();
            lay.topMargin = position;
            if((binding.moreItemView.getHeight() + position) < binding.getRoot().getHeight()){
               binding.moreItemView.setLayoutParams(lay);
            }
            else{
                lay.topMargin = position - binding.moreItemView.getHeight();
                binding.moreItemView.setLayoutParams(lay);
            }
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
                (position,number) ->{//обработчик нажатия на элемент

        });

        binding.list1.setAdapter(adapter);

        return binding.getRoot();

    }
    //обработчик перетаскиваний

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
        binding.closeView.setOnClickListener(new OnClickListener() {
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
        binding.addComponentButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addView.setVisibility(View.VISIBLE);
                binding.closeView.setVisibility(View.VISIBLE);

            }
        });
        binding.closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isKeyboardShowing){
                    InputMethodManager imm = (InputMethodManager)binding.getRoot().getContext().getSystemService(binding.getRoot().getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);

                }
                else{
                    //binding.moreView.setVisibility(View.INVISIBLE);
                    binding.closeView.setVisibility(View.INVISIBLE);
                    ///binding.winView.setVisibility(View.INVISIBLE);
                    binding.addView.setVisibility(View.INVISIBLE);

                }


            }
        });
        binding.submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(Looper.getMainLooper());
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        ContentValues cv = new ContentValues();
                        cv.put(MainBaseContract.Components.COLUMN_NAME_NAME,binding.addComponentName.getText().toString());
                        cv.put(MainBaseContract.Components.COLUMN_NAME_QUANTITY,binding.addComponentQuantity.getText().toString());
                        cv.put(MainBaseContract.Components.COLUMN_NAME_ELEMENT,ComponentPlaceholderContent.idSelectElem);
                        ContentProviderDB.insert(MainBaseContract.Components.TABLE_NAME,null,cv);

                        handler.post(()->{
                            ComponentPlaceholderContent.loadComponentsData();
                            binding.list1.getAdapter().notifyDataSetChanged();
                        });


                    }
                };
                Thread thread = new Thread(task);
                thread.start();
                binding.addView.setVisibility(View.INVISIBLE);
            }

        });
    }

    public ComponentsRecycleFragment(){

    }
    public ComponentsRecycleFragment(List<Item> items){

    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ComponentsRecycleFragment newInstance(ArrayList<Item> items) {
        ComponentsRecycleFragment fragment = new ComponentsRecycleFragment(items);
        return fragment;
    }




}