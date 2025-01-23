package com.example.random_menu.ElementsList;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.random_menu.Data.Item;
import com.example.random_menu.Element.ElementActivity;
import com.example.random_menu.ElementsList.DialogFragments.MoreElemDialogFragment;
import com.example.random_menu.R;
import com.example.random_menu.Utils.ToastHelper;
import com.example.random_menu.databinding.AlertDialogBinding;
import com.example.random_menu.databinding.ListFragmentBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
public class ElementsListRecycleFragment extends Fragment {

    private ElemPlaceholderContent viewModel;


    ListFragmentBinding binding;
    AlertDialogBinding alertBinding;
    MoreElemDialogFragment moreElemDialogFragment = new MoreElemDialogFragment();
    // TODO: Customize parameters
    @Inject
    ToastHelper toast;
    //id элемента для которого вызвано moreView
    private int moreViewItemId;

    private ElementsListRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ListFragmentBinding.inflate(inflater, container, false);
        //View view = inflater.inflate(R.layout.list_elem_fragment, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ElemPlaceholderContent.class);
        Handler handler = new Handler(Looper.getMainLooper());
        viewModel.getShareGroup();
        viewModel.loadElements();

        adapter = new ElementsListRecyclerViewAdapter(
                viewModel.getElements(),
                (screenPosition,elem, number,listPosition) ->{//функция для отрисовки moreView
                    //выхватываем id элемента списка
                    moreElemDialogFragment.setVars(
                            Integer.valueOf(listPosition),
                            screenPosition,
                            elem,
                            (element)->{
                                LayoutInflater alertInflater = getLayoutInflater();
                                alertBinding = AlertDialogBinding.inflate(alertInflater);
                                AlertDialog dialog = new AlertDialog.Builder(binding.getRoot().getContext())
                                        .setView(alertBinding.getRoot())
                                        .create();
                                alertBinding.getRoot().setBackground(null);
                                alertBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        viewModel.deleteElem(element.id);

                                        //GroupPlaceholderContent.deleteGroup(dbID);
                                        //binding.list1.getAdapter().notifyDataSetChanged();
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
                                //binding.list1.getAdapter().notifyDataSetChanged();
                            },
                            ()->{
                                //ComponentPlaceholderContent.loadComponents();
                                Intent intent = new Intent(getActivity(), ElementActivity.class);
                                startActivity(intent);
                            },(currentElem)->{
                                viewModel.SelectesElements.clear();
                                viewModel.checkElement(viewModel.ITEM_MAP.get(currentElem.id));
                                toast.showMessage(getString(R.string.start_export));

                                viewModel.exportSelectedElements(()->{
                                    //суем данные в буффер, далеко не лучшая идея но надо без пермишнов.
                                    String result = viewModel.groupsToXml();

                                    //ElemPlaceholderContent.xmlToClass(result);
                                    ///Log.e("RESULTTTT",result);
                                    ClipboardManager clipboard = (ClipboardManager) binding.getRoot().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("XML Data", result);
                                    clipboard.setPrimaryClip(clip);
                                    handler.post(()->{
                                        toast.showMessage(getString(R.string.xml_copied_to_clipboard));
                                    });
                                });
                            }
                    );
                    moreElemDialogFragment.show(getParentFragmentManager(),"MoreItemDialog");
                    //формируем параметры для отступов окна от границ экрана


                },
                (element,position) ->{
                    //Log.e("list111",name);
                    viewModel.setShareElement(element);
                    //ComponentPlaceholderContent.loadComponents();
                    Intent intent = new Intent(getActivity(), ElementActivity.class);
                    startActivity(intent);
                });

        // Set the adapter
        binding.list1.setAdapter(adapter);
        //return view;
        viewModel.getElements().observe(getViewLifecycleOwner(), elems ->{
            if(elems != null){
                adapter.submitList(new ArrayList<>(elems));
            }
        });
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
            viewModel.swap(fromPosition,toPosition);

            //Collections.swap((List<?>) GroupPlaceholderContent.GROUPS, fromPosition, toPosition);
            try{
                binding.list1.getAdapter().notifyItemMoved(fromPosition, toPosition);
                binding.list1.getAdapter().notifyItemChanged(fromPosition);
                binding.list1.getAdapter().notifyItemChanged(toPosition);
                //binding.list1.getAdapter().notifyDataSetChanged();
                viewModel.loadElements();
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

    /*@Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler(Looper.getMainLooper());




        adapter = new ElementsListRecyclerViewAdapter(viewModel.getElements(),
                (screenPosition,elem, number,listPosition) ->{//функция для отрисовки moreView
                    //выхватываем id элемента списка
                    moreElemDialogFragment.setVars(
                            Integer.valueOf(listPosition),
                            screenPosition,
                            elem,
                            (element)->{
                                LayoutInflater alertInflater = getLayoutInflater();
                                alertBinding = AlertDialogBinding.inflate(alertInflater);
                                AlertDialog dialog = new AlertDialog.Builder(binding.getRoot().getContext())
                                        .setView(alertBinding.getRoot())
                                        .create();
                                alertBinding.getRoot().setBackground(null);
                                alertBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        viewModel.deleteElem(element.id);

                                        //GroupPlaceholderContent.deleteGroup(dbID);
                                        //binding.list1.getAdapter().notifyDataSetChanged();
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
                                //binding.list1.getAdapter().notifyDataSetChanged();
                            },
                            ()->{
                                //ComponentPlaceholderContent.loadComponents();
                                Intent intent = new Intent(getActivity(), ElementActivity.class);
                                startActivity(intent);
                            },(currentElem)->{
                                viewModel.SelectesElements.clear();
                                viewModel.checkElement(viewModel.ITEM_MAP.get(currentElem.id));
                                toast.showMessage(getString(R.string.start_export));

                                viewModel.exportSelectedElements(()->{
                                    //суем данные в буффер, далеко не лучшая идея но надо без пермишнов.
                                    String result = viewModel.groupsToXml();

                                    //ElemPlaceholderContent.xmlToClass(result);
                                    ///Log.e("RESULTTTT",result);
                                    ClipboardManager clipboard = (ClipboardManager) binding.getRoot().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("XML Data", result);
                                    clipboard.setPrimaryClip(clip);
                                    handler.post(()->{
                                        toast.showMessage(getString(R.string.xml_copied_to_clipboard));
                                    });
                                });
                            }
                    );
                    moreElemDialogFragment.show(getParentFragmentManager(),"MoreItemDialog");
                    //формируем параметры для отступов окна от границ экрана


                },
                (element,position) ->{
                    //Log.e("list111",name);
                    viewModel.setShareElement(element);
                    //ComponentPlaceholderContent.loadComponents();
                    Intent intent = new Intent(getActivity(), ElementActivity.class);
                    startActivity(intent);
                });

        // Set the adapter
        binding.list1.setAdapter(adapter);
        viewModel.getElements().observe(getViewLifecycleOwner(), elems ->{
            if(elems != null){
                adapter.submitList(new ArrayList<>(elems));
            }
        });
        //binding.list1.getAdapter().notifyDataSetChanged();
        /*if(ComponentPlaceholderContent.positionSelectElem != null){
            ComponentPlaceholderContent.deleteIfNoGroups();
            Log.e("RESUM","ressss" + String.valueOf(ComponentPlaceholderContent.positionSelectElem));
            binding.list1.getAdapter().notifyItemChanged(ComponentPlaceholderContent.positionSelectElem);
        }*/


        //Cursor cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME,null,null,null,null,null,null);
        //Log.e("ELEMCOUNT",String.valueOf(cursor.getCount()));


    //}

    @Override
    public void onDestroy() {
        super.onDestroy();
        //ComponentPlaceholderContent.positionSelectElem = null;
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