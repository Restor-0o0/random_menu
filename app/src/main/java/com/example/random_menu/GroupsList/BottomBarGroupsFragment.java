package com.example.random_menu.GroupsList;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.random_menu.GroupsList.DialogFragments.AddGroupDialogFragment;
import com.example.random_menu.GroupsList.DialogFragments.GroupsCheckListDialogFragment;
import com.example.random_menu.Utils.ImportDialogFragment;
import com.example.random_menu.GroupsList.DialogFragments.MoreGroupListDialogFragment;
import com.example.random_menu.GroupsList.DialogFragments.WinGroupElemDialogFragment;
import com.example.random_menu.R;
import com.example.random_menu.databinding.AlertDialogBinding;
import com.example.random_menu.databinding.BottomBarFragmentBinding;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

public class BottomBarGroupsFragment extends Fragment {
    boolean imp = true;
    static BottomBarFragmentBinding binding;
    AlertDialogBinding alertBinding;
    AddGroupDialogFragment addGroupDialogFragment = new AddGroupDialogFragment();
    WinGroupElemDialogFragment winGroupElemDialogFragment = new WinGroupElemDialogFragment();
    MoreGroupListDialogFragment moreGroupListDialogFragment = new MoreGroupListDialogFragment();
    GroupsCheckListDialogFragment GroupsCheckListDialogFragment = new GroupsCheckListDialogFragment();
    ImportDialogFragment importDialogFragment = new ImportDialogFragment();
    private ObjectAnimator mAnimator;
    boolean isKeyboardShowing = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = BottomBarFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());

        //слушатель, который сначала скроет клавиатуру, а после второго тапа скроет view
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        binding.getRoot().getWindowVisibleDisplayFrame(r);
                        int screenHeight = binding.getRoot().getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        //Log.d(TAG, "keypadHeight = " + keypadHeight);

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                              }
                        }
                        else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                InputMethodManager imm = (InputMethodManager)binding.getRoot().getContext().getSystemService(binding.getRoot().getContext().INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                            }
                        }
                    }
                });

        //нопка вызова окна добавления группы
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGroupDialogFragment.setVars(()->{
                    GroupsRecycleFragment fm =(GroupsRecycleFragment) getParentFragmentManager().findFragmentById(R.id.frameMain);
                    fm.binding.list1.getAdapter().notifyItemChanged(GroupPlaceholderContent.getCount() - 1);
                });
                addGroupDialogFragment.show(getParentFragmentManager(),"AddGroupDialog");
            }
        });
        
        //кнопка с настройками текущего экрана
        binding.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moreGroupListDialogFragment.setVars(()->{

                    //action для чеклиста на удаление групп
                    //после подтверждения чеклиста вызывается функция удаления
                    GroupsCheckListDialogFragment.setVars(()->{

                        LayoutInflater alertInflater = getLayoutInflater();
                        alertBinding = AlertDialogBinding.inflate(alertInflater);
                        AlertDialog dialog = new AlertDialog.Builder(binding.getRoot().getContext())
                                .setView(alertBinding.getRoot())
                                .create();
                        alertBinding.getRoot().setBackground(null);
                        alertBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for(GroupPlaceholderContent.PlaceholderItem item : GroupPlaceholderContent.SelectesGroups){
                                    Log.e("DeletingSelect",item.name);
                                }
                                GroupPlaceholderContent.deleteSelectedGroups();
                                GroupsRecycleFragment fm =(GroupsRecycleFragment) getParentFragmentManager().findFragmentById(R.id.frameMain);
                                fm.binding.list1.getAdapter().notifyDataSetChanged();
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

                    });
                    GroupsCheckListDialogFragment.show(getParentFragmentManager(),"CheckListDialog");
                },()->{
                    //action для чеклиста на экспорт групп
                    //после подтверждения чеклиста вызывается функция экспорта
                    GroupsCheckListDialogFragment.setVars(()->{
                        for(GroupPlaceholderContent.PlaceholderItem item : GroupPlaceholderContent.SelectesGroups){
                            Log.e("ExportingSelect",item.name);
                        }
                        Toast.makeText(binding.getRoot().getContext(), R.string.start_export, Toast.LENGTH_SHORT).show();
                        GroupPlaceholderContent.exportSelectedGroups(()->{
                            //суем данные в буффер, далеко не лучшая идея но надо без пермишнов.
                            String result = GroupPlaceholderContent.groupsToXml();

                            GroupPlaceholderContent.xmlToClass(result);
                            Log.e("RESULTTTT",result);
                            ClipboardManager clipboard = (ClipboardManager) binding.getRoot().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("XML Data", result);
                            clipboard.setPrimaryClip(clip);

                            Toast.makeText(binding.getRoot().getContext(), R.string.xml_copied_to_clipboard, Toast.LENGTH_SHORT).show();
                        });

                    });
                    GroupsCheckListDialogFragment.show(getParentFragmentManager(),"CheckListDialog");

                },()->{
                    importDialogFragment.setVars((xmlString)->{
                        int res = GroupPlaceholderContent.xmlToClass(xmlString);
                        switch (res){
                            case 1:{
                                Toast.makeText(binding.getRoot().getContext(), "Неверные данные", Toast.LENGTH_SHORT).show();
                            }
                            case 2:{
                                Toast.makeText(binding.getRoot().getContext(), "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                            }
                        }
                        GroupPlaceholderContent.importIntoDB(()->{
                            handler.post(() ->{
                                GroupPlaceholderContent.loadGroups();
                                GroupsRecycleFragment fm =(GroupsRecycleFragment) getParentFragmentManager().findFragmentById(R.id.frameMain);
                                fm.binding.list1.getAdapter().notifyDataSetChanged();
                            });
                        });
                        //запускаем поток на обновление и потомв мейн поток возвращаем задачи на присваивание
                        });
                    importDialogFragment.show(getParentFragmentManager(),"ImportDialog");
                });
                moreGroupListDialogFragment.show(getParentFragmentManager(),"MoreListDialog");
            }
        });
        //кнопка рандома
        binding.randButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.anim_rotate);
                anim.setDuration(1000);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //выхватываем группу
                        try {
                            winGroupElemDialogFragment.show(getParentFragmentManager(),"WinElemDialog");
                        }
                        catch(Exception e){
                            Log.e("randGroupbutton",e.toString());
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                binding.circ.startAnimation(anim);
            }
        });
    }
    
}