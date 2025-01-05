package com.example.random_menu.ElementsList;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.random_menu.ElementsList.DialogFragments.AddElemDialogFragment;
import com.example.random_menu.ElementsList.DialogFragments.ElementsCheckListDialogFragment;
import com.example.random_menu.ElementsList.DialogFragments.MoreElemListDialogFragment;
import com.example.random_menu.ElementsList.DialogFragments.WinElemDialogFragment;
import com.example.random_menu.Utils.ImportDialogFragment;
import com.example.random_menu.R;
import com.example.random_menu.databinding.AlertDialogBinding;
import com.example.random_menu.databinding.BottomBarFragmentBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

public class BottomBarElementsFragment extends Fragment {
    boolean imp = true;
    static BottomBarFragmentBinding binding;
    AlertDialogBinding alertBinding;
    AddElemDialogFragment addElemDialogFragment = new AddElemDialogFragment();
    WinElemDialogFragment winElemDialogFragment = new WinElemDialogFragment();
    MoreElemListDialogFragment moreElemListDialogFragment = new MoreElemListDialogFragment();
    ElementsCheckListDialogFragment elementsCheckListDialogFragment = new ElementsCheckListDialogFragment();
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
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addElemDialogFragment.setVars(()->{
                    ElementsListRecycleFragment fm =(ElementsListRecycleFragment) getParentFragmentManager().findFragmentById(R.id.frameMain);
                    fm.binding.list1.getAdapter().notifyItemChanged(ElemPlaceholderContent.getCount() - 1);
                });

                addElemDialogFragment.show(getParentFragmentManager(),"AddGroupDialog");
            }
        });
        //кнопка с настройками текущего экрана
        binding.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreElemListDialogFragment.setVars(()->{
                    elementsCheckListDialogFragment.setVars(()->{
                        LayoutInflater alertInflater = getLayoutInflater();
                        alertBinding = AlertDialogBinding.inflate(alertInflater);
                        AlertDialog dialog = new AlertDialog.Builder(binding.getRoot().getContext())
                                .setView(alertBinding.getRoot())
                                .create();
                        alertBinding.getRoot().setBackground(null);
                        alertBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for(ElemPlaceholderContent.PlaceholderItem item : ElemPlaceholderContent.SelectesElements){
                                    Log.e("DeletingSelect",item.name);
                                }
                                ElemPlaceholderContent.deleteSelectedElements();
                                ElementsListRecycleFragment fm =(ElementsListRecycleFragment) getParentFragmentManager().findFragmentById(R.id.frameMain);
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
                    elementsCheckListDialogFragment.show(getParentFragmentManager(),"CheckListDialog");
                },
                ()->{
                    elementsCheckListDialogFragment.setVars(()->{
                        ElemPlaceholderContent.exportSelectedElements(()->{
                            String result = ElemPlaceholderContent.groupsToXml();

                            ElemPlaceholderContent.xmlToClass(result);
                            Log.e("RESULTTTT",result);
                            ClipboardManager clipboard = (ClipboardManager) binding.getRoot().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("XML Data", result);
                            clipboard.setPrimaryClip(clip);

                            Toast.makeText(binding.getRoot().getContext(), R.string.xml_copied_to_clipboard, Toast.LENGTH_SHORT).show();
                        });
                        ElementsListRecycleFragment fm =(ElementsListRecycleFragment) getParentFragmentManager().findFragmentById(R.id.frameMain);
                        fm.binding.list1.getAdapter().notifyDataSetChanged();
                    });
                    elementsCheckListDialogFragment.show(getParentFragmentManager(),"CheckListDialog");
                },
                ()->{
                    importDialogFragment.setVars((xmlString)->{
                        int res = ElemPlaceholderContent.xmlToClass(xmlString);
                        switch (res){
                            case 1:{
                                Toast.makeText(binding.getRoot().getContext(), "Неверные данные", Toast.LENGTH_SHORT).show();
                            }
                            case 2:{
                                Toast.makeText(binding.getRoot().getContext(), "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                            }
                        }
                        ElemPlaceholderContent.importIntoDB(()->{
                            handler.post(() ->{
                                ElemPlaceholderContent.loadElements();
                                ElementsListRecycleFragment fm =(ElementsListRecycleFragment) getParentFragmentManager().findFragmentById(R.id.frameMain);
                                fm.binding.list1.getAdapter().notifyDataSetChanged();
                            });
                        });
                        //запускаем поток на обновление и потомв мейн поток возвращаем задачи на присваивание
                    });
                    importDialogFragment.show(getParentFragmentManager(),"ImportDialog");
                });
                moreElemListDialogFragment.show(getParentFragmentManager(),"MoreListDialog");
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
                            winElemDialogFragment.show(getParentFragmentManager(),"WinElemDialog");
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
