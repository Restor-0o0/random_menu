package com.example.random_menu.Element;

import static android.view.View.OnClickListener;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Item;
import com.example.random_menu.Element.DialogFragments.AddComponentDialogFragment;
import com.example.random_menu.Element.DialogFragments.ComponentInfoDialogFragment;
import com.example.random_menu.Element.DialogFragments.MoreItemDialogFragment;
import com.example.random_menu.databinding.ListComponentsFragmentBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ComponentsRecycleFragment extends Fragment {

    ListComponentsFragmentBinding binding;
    private ComponentPlaceholderContent viewModel;
    //id элемента для которого вызвано moreView
    private int moreViewItemId;
    private int listPositionItem;
    boolean isKeyboardShowing = false;
    private final MoreItemDialogFragment moreItemDialogFragment = new MoreItemDialogFragment();
    private final ComponentInfoDialogFragment componentInfoDialogFragment = new ComponentInfoDialogFragment();
    private final AddComponentDialogFragment addComponentDialogFragment = new AddComponentDialogFragment();
    private ComponentRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ListComponentsFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ComponentPlaceholderContent.class);
        adapter = new ComponentRecyclerViewAdapter(
                viewModel.getComponents(),
                (screenPosition,component, number,listPosition) ->{//функция для отрисовки moreView
            //выхватываем id элемента списка
            moreItemDialogFragment.setVars(
                    Integer.valueOf(listPosition),
                    screenPosition,
                    component,
                    ()->{
                    },
                    (comp)->{
                        viewModel.deleteComponent(comp);
                    }
            );
            moreItemDialogFragment.show(getParentFragmentManager(),"MoreItemDialog");
            //формируем параметры для отступов окна от границ экрана


        },
                (listPosition,comp,name,comment,quantity) ->{//обработчик нажатия на элемент
                    componentInfoDialogFragment.setVars(
                            comp.id,
                            listPosition,
                            name,
                            comment,
                            quantity,
                            ()->{
                                //binding.list1.getAdapter().notifyItemChanged(listPosition);
                            }

                    );
                    componentInfoDialogFragment.show(getParentFragmentManager(),"ComponentInfoDialog");
        });
        viewModel.getComponents().observe(getViewLifecycleOwner(), components ->{
            if(components != null){
                //binding.list1.getAdapter().notifyDataSetChanged();
                Log.d("UpdateComponentsRecycle",String.valueOf(components.size()));
                adapter.submitList(new ArrayList<>(components));
            }
        });
        try{
            binding.list1.setAdapter(adapter);

        }catch (Exception e){
            Log.e("LoadAdapterComponentsError",e.toString());
        }
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //запуск добавления элемента
        binding.addComponentButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addComponentDialogFragment.setVars(()->{
                    //binding.list1.getAdapter().notifyDataSetChanged();
                });
                addComponentDialogFragment.show(getParentFragmentManager(),"AddComponentDialog");
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