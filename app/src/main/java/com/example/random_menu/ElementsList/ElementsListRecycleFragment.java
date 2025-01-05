package com.example.random_menu.ElementsList;

import static android.widget.RelativeLayout.*;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.random_menu.ElementsList.DialogFragments.MoreElemDialogFragment;
import com.example.random_menu.R;
import com.example.random_menu.databinding.AlertDialogBinding;
import com.example.random_menu.databinding.ListFragmentBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.ElemPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ElementsListRecycleFragment extends Fragment {

    // TODO: Customize parameter argument names
    ListFragmentBinding binding;
    AlertDialogBinding alertBinding;
    MoreElemDialogFragment moreElemDialogFragment = new MoreElemDialogFragment();
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
                (screenPosition,id, number,listPosition) ->{//функция для отрисовки moreView
                    //выхватываем id элемента списка
                    moreElemDialogFragment.setVars(
                            Integer.valueOf(listPosition),
                            screenPosition,
                            Integer.valueOf(id),
                            (dbID)->{
                                LayoutInflater alertInflater = getLayoutInflater();
                                alertBinding = AlertDialogBinding.inflate(alertInflater);
                                AlertDialog dialog = new AlertDialog.Builder(binding.getRoot().getContext())
                                        .setView(alertBinding.getRoot())
                                        .create();
                                alertBinding.getRoot().setBackground(null);
                                alertBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ElemPlaceholderContent.deleteElem(Integer.valueOf(dbID));

                                        //GroupPlaceholderContent.deleteGroup(dbID);
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
                                binding.list1.getAdapter().notifyDataSetChanged();
                            },
                            ()->{
                                ComponentPlaceholderContent.positionSelectElem = Integer.valueOf(listPosition);
                                ComponentPlaceholderContent.idSelectElem = id;
                                //ComponentPlaceholderContent.loadComponents();
                                Intent intent = new Intent(getActivity(), ElementActivity.class);
                                startActivity(intent);
                            }
                    );
                    moreElemDialogFragment.show(getParentFragmentManager(),"MoreItemDialog");
                    //формируем параметры для отступов окна от границ экрана


                },
                (id,name,position) ->{
                    //Log.e("list111",name);
                    ComponentPlaceholderContent.positionSelectElem = position;
                    ComponentPlaceholderContent.idSelectElem = id;
                    //ComponentPlaceholderContent.loadComponents();
                    Intent intent = new Intent(getActivity(), ElementActivity.class);
                    startActivity(intent);
        });

        // Set the adapter
        binding.list1.setAdapter(adapter);
        //return view;

        //декоратор и помошник нажатий для перетаскивания элементов по списку и тем самым изменения их приоритетов
        //DividerItemDecoration decorator = new DividerItemDecoration(binding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
        //binding.list1.addItemDecoration(decorator);

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
                (screenPosition,id, number,listPosition) ->{//функция для отрисовки moreView
                    //выхватываем id элемента списка
                    moreElemDialogFragment.setVars(
                            Integer.valueOf(listPosition),
                            screenPosition,
                            Integer.valueOf(id),
                            (dbID)->{
                                LayoutInflater alertInflater = getLayoutInflater();
                                alertBinding = AlertDialogBinding.inflate(alertInflater);
                                AlertDialog dialog = new AlertDialog.Builder(binding.getRoot().getContext())
                                        .setView(alertBinding.getRoot())
                                        .create();
                                alertBinding.getRoot().setBackground(null);
                                alertBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ElemPlaceholderContent.deleteElem(Integer.valueOf(dbID));

                                        //GroupPlaceholderContent.deleteGroup(dbID);
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
                                binding.list1.getAdapter().notifyDataSetChanged();
                            },
                            ()->{
                                ComponentPlaceholderContent.positionSelectElem = Integer.valueOf(listPosition);
                                ComponentPlaceholderContent.idSelectElem = id;
                                //ComponentPlaceholderContent.loadComponents();
                                Intent intent = new Intent(getActivity(), ElementActivity.class);
                                startActivity(intent);
                            }
                    );
                    moreElemDialogFragment.show(getParentFragmentManager(),"MoreItemDialog");
                    //формируем параметры для отступов окна от границ экрана


                },
                (id,name,position) ->{
                    //Log.e("list111",name);
                    ComponentPlaceholderContent.positionSelectElem = position;
                    ComponentPlaceholderContent.idSelectElem = id;
                    //ComponentPlaceholderContent.loadComponents();
                    Intent intent = new Intent(getActivity(), ElementActivity.class);
                    startActivity(intent);
                });

        // Set the adapter
        binding.list1.setAdapter(adapter);

        //binding.list1.getAdapter().notifyDataSetChanged();
        if(ComponentPlaceholderContent.positionSelectElem != null){
            ComponentPlaceholderContent.deleteIfNoGroups();
            Log.e("RESUM","ressss" + String.valueOf(ComponentPlaceholderContent.positionSelectElem));
            binding.list1.getAdapter().notifyItemChanged(ComponentPlaceholderContent.positionSelectElem);
        }


        //Cursor cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME,null,null,null,null,null,null);
        //Log.e("ELEMCOUNT",String.valueOf(cursor.getCount()));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ComponentPlaceholderContent.positionSelectElem = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }




    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ElementsListRecycleFragment(){

    }
    public ElementsListRecycleFragment(List<Item> items){

    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ElementsListRecycleFragment newInstance(ArrayList<Item> items) {
        ElementsListRecycleFragment fragment = new ElementsListRecycleFragment(items);
        return fragment;
    }




}