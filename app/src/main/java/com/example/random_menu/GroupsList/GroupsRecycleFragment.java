package com.example.random_menu.GroupsList;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import com.example.random_menu.Data.Item;
import com.example.random_menu.ElementsList.ElementsListActivity;
import com.example.random_menu.GroupsList.DialogFragments.MoreGroupDialogFragment;

import com.example.random_menu.GroupsList.DialogFragments.PropertiesDialogFragment;
import com.example.random_menu.R;
import com.example.random_menu.databinding.AlertDialogBinding;
import com.example.random_menu.databinding.ListFragmentBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class GroupsRecycleFragment extends Fragment {

    // TODO: Customize parameter argument names
    ListFragmentBinding binding;
    AlertDialogBinding alertBinding;
    private final MoreGroupDialogFragment moreItemDialogFragment = new MoreGroupDialogFragment();
    private final PropertiesDialogFragment propertiesDialogFragment = new PropertiesDialogFragment();
    //private static List<Item> ITEMS;
    // TODO: Customize parameters
    private int moreViewItemId;

    private static GroupsRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ListFragmentBinding.inflate(inflater, container, false);
        //View view = inflater.inflate(R.layout.list_elem_fragment, container, false);


        adapter = new GroupsRecyclerViewAdapter(
                GroupPlaceholderContent.GROUPS,
                (screenPosition,id, number,listPosition) ->{//функция для отрисовки moreView
            //выхватываем id элемента списка
            moreItemDialogFragment.setVars(
                    Integer.valueOf(listPosition),
                    screenPosition,
                    Integer.valueOf(id),
                    (dbID)->
                    {
                        LayoutInflater alertInflater = getLayoutInflater();
                        alertBinding = AlertDialogBinding.inflate(alertInflater);
                        AlertDialog dialog = new AlertDialog.Builder(binding.getRoot().getContext())
                                .setView(alertBinding.getRoot())
                                .create();
                        alertBinding.getRoot().setBackground(null);
                        alertBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GroupPlaceholderContent.deleteGroup(dbID);
                                binding.list1.getAdapter().notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                        alertBinding.negativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        if(dialog.getWindow() != null){
                            dialog.getWindow().setBackgroundDrawable(binding.getRoot().getContext().getDrawable(R.drawable.back_item));
                        }
                        dialog.show();

                    },()->{
                        propertiesDialogFragment.setVars(
                                Integer.valueOf(id),
                                Integer.valueOf(listPosition),
                                ()->{
                                    //обновляем только 1 элемент, потому что это таргетное редактирвоание свойств
                                    binding.list1.getAdapter().notifyItemChanged(Integer.valueOf(listPosition));
                                }
                        );
                        propertiesDialogFragment.show(getParentFragmentManager(),"PropertiesDialog");
                    }
            );
            moreItemDialogFragment.show(getParentFragmentManager(),"MoreItemDialog");
            //формируем параметры для отступов окна от границ экрана


        },
                //переход к элементам группы
                (id,name) ->{
                    ElemPlaceholderContent.idSelectGroup = id;
                    ElemPlaceholderContent.nameSelectGroup = name;
                    Intent intent = new Intent(getActivity(), ElementsListActivity.class);
                    startActivity(intent);
        });

        // Set the adapter
        binding.list1.setAdapter(adapter);

        //DividerItemDecoration decorator = new DividerItemDecoration(binding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
        //binding.list1.addItemDecoration(decorator);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(TouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.list1);
        //return view;

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
    }



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupsRecycleFragment(){
    }
    public GroupsRecycleFragment(List<Item> items){
    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GroupsRecycleFragment newInstance(ArrayList<Item> items) {
        GroupsRecycleFragment fragment = new GroupsRecycleFragment(items);
        return fragment;
    }



}