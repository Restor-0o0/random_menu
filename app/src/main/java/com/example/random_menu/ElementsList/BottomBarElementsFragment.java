package com.example.random_menu.ElementsList;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Element.ElementActivity;
import com.example.random_menu.ElementsList.DialogFragments.AddElemDialogFragment;
import com.example.random_menu.ElementsList.DialogFragments.MoreElemListDialogFragment;
import com.example.random_menu.ElementsList.DialogFragments.WinElemDialogFragment;
import com.example.random_menu.GroupsList.DialogFragments.AddGroupDialogFragment;
import com.example.random_menu.GroupsList.DialogFragments.MoreGroupListDialogFragment;
import com.example.random_menu.GroupsList.DialogFragments.WinGroupElemDialogFragment;
import com.example.random_menu.R;
import com.example.random_menu.databinding.BottomBarFragmentBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

public class BottomBarElementsFragment extends Fragment {
    boolean imp = true;
    static BottomBarFragmentBinding binding;
    AddElemDialogFragment addElemDialogFragment = new AddElemDialogFragment();
    WinElemDialogFragment winElemDialogFragment = new WinElemDialogFragment();
    MoreElemListDialogFragment moreElemListDialogFragment = new MoreElemListDialogFragment();
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
                    fm.binding.list1.getAdapter().notifyDataSetChanged();
                });
                addElemDialogFragment.show(getParentFragmentManager(),"AddGroupDialog");
            }
        });
        //кнопка с настройками текущего экрана
        binding.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        /*
        //нопка вызова окна добавления группы
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        //кнопка с настройками текущего экрана
        binding.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        /*
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
                            ElemPlaceholderContent.PlaceholderItem win = ElemPlaceholderContent.getRandom();
                            //DBContentProvider provider = new DBContentProvider();
                            ComponentPlaceholderContent.idSelectElem = win.id;
                            /*Cursor cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME,null,"_ID IN (SELECT Element from ElemGroup where Group_ = " + win.id + ")",null,null,null,"RANDOM()");
                            while(cursor.getCount() == 0){
                                Log.e("Resel","res");
                                win = GroupPlaceholderContent.getRandom();
                                cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME,null,"_ID IN (SELECT Element from ElemGroup where Group_ = " + win.id + ")",null,null,null,"RANDOM()");

                            }

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                    binding.circ.startAnimation(anim);






            }
        });
       /*
        //кнопка подтверждения добавления группы
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(binding.addElemName.getText());
                String comment = String.valueOf(binding.addElemComment.getText());
                Log.e("adada","_"+ name+"_");
                if(name.length() != 0){
                    ContentValues cv = new ContentValues();
                    cv.put(MainBaseContract.Elements.COLUMN_NAME_NAME,name);
                    cv.put(MainBaseContract.Elements.COLUMN_NAME_COMMENT,comment);
                    cv.put(MainBaseContract.Elements.COLUMN_NAME_PRIORITY,String.valueOf(ElemPlaceholderContent.maxPriority + 1));


                    long id_ = ContentProviderDB.insert(MainBaseContract.Elements.TABLE_NAME,null,cv);
                    cv.clear();
                    cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP,ElemPlaceholderContent.idSelectGroup);
                    cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT,id_);
                    ContentProviderDB.insert(MainBaseContract.ElemGroup.TABLE_NAME,null,cv);

                    ElemPlaceholderContent.loadElements();
                }
                try {
                    FragmentManager fm = getParentFragmentManager();
                    ElementsListRecycleFragment frag = (ElementsListRecycleFragment) fm.findFragmentById(R.id.frameMain);
                    frag.binding.list1.getAdapter().notifyDataSetChanged();
                }
                catch(Exception e){
                    Log.e("findFragmentElementRecycleViewError",e.toString());
                }

                InputMethodManager imm = (InputMethodManager)binding.getRoot().getContext().getSystemService(binding.getRoot().getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                binding.addView.setVisibility(View.INVISIBLE);
                binding.closeView.setVisibility(View.INVISIBLE);
            }
        });
        binding.goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ElementActivity.class);
                startActivity(intent);
            }
        });*/
    }
    
}
/*
* <ImageButton
                android:id="@+id/rand_button"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_weight="1"
                android:background="@null"
                android:contentDescription="TODO"
                app:srcCompat="@layout/ramd_circ" />*/