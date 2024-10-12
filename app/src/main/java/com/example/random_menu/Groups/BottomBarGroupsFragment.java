package com.example.random_menu.Groups;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.R;
import com.example.random_menu.databinding.FragmentBottomBarBinding;
import com.example.random_menu.databinding.ListElemFragmentBinding;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

public class BottomBarGroupsFragment extends Fragment {
    boolean imp = true;
    static FragmentBottomBarBinding binding;

    private ObjectAnimator mAnimator;
    boolean isKeyboardShowing = false;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentBottomBarBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //binding.randButton.clearAnimation();
        //binding.randButton.animate().setDuration(0);
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
                binding.addView.setVisibility(View.VISIBLE);
                binding.closeView.setVisibility(View.VISIBLE);

            }
        });
        
        //кнопка с настройками текущего экрана
        binding.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.moreView.setVisibility(View.VISIBLE);
                binding.closeView.setVisibility(View.VISIBLE);
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
                            GroupPlaceholderContent.PlaceholderItem win = GroupPlaceholderContent.getRandom();
                            //DBContentProvider provider = new DBContentProvider();
                            //выхватываем запросом случайный элемент из БД
                            Cursor cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME,null,"_ID IN (SELECT Element from ElemGroup where Group_ = " + win.id + ")",null,null,null,"RANDOM()");
                            //Цикл если попали на пустую группу
                            while(cursor.getCount() == 0){
                                Log.e("Resel","res");
                                //выхватываем группу
                                win = GroupPlaceholderContent.getRandom();
                                //выхватываем запросом случайный элемент из БД
                                cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME,null,"_ID IN (SELECT Element from ElemGroup where Group_ = " + win.id + ")",null,null,null,"RANDOM()");

                            }
                            cursor.moveToFirst();
                            binding.winView.setVisibility(View.VISIBLE);
                            binding.closeView.setVisibility(View.VISIBLE);
                            //binding.elemName.setText(win.content);
                            binding.groupName.setText(win.name);
                            binding.elemName.setText(cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_NAME))+"  " + win.id);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                    binding.circ.startAnimation(anim);






            }
        });
        //кнопка закрытия всплывающих окон
        binding.closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isKeyboardShowing){
                    InputMethodManager imm = (InputMethodManager)binding.getRoot().getContext().getSystemService(binding.getRoot().getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);

                }
                else{
                    binding.moreView.setVisibility(View.INVISIBLE);
                    binding.closeView.setVisibility(View.INVISIBLE);
                    binding.winView.setVisibility(View.INVISIBLE);
                    binding.addView.setVisibility(View.INVISIBLE);

                }


            }
        });
        //кнопка подтверждения добавления группы
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(binding.addElemName.getText());
                String comment = String.valueOf(binding.addElemComment.getText());
                Log.e("adada","_"+ name+"_");
                if(name.length() != 0){
                    ContentValues cv = new ContentValues();
                    cv.put(MainBaseContract.Groups.COLUMN_NAME_NAME,name);
                    cv.put(MainBaseContract.Groups.COLUMN_NAME_COMMENT,comment);
                    cv.put(MainBaseContract.Groups.COLUMN_NAME_PRIORITY,String.valueOf(GroupPlaceholderContent.maxPriority + 1));
                    ContentProviderDB.insert(MainBaseContract.Groups.TABLE_NAME,null,cv);
                }

                FragmentManager fm = getParentFragmentManager();
                GroupsRecycleFragment frag = (GroupsRecycleFragment) fm.findFragmentById(R.id.frameMain);
                frag.binding.list1.getAdapter().notifyDataSetChanged();

                InputMethodManager imm = (InputMethodManager)binding.getRoot().getContext().getSystemService(binding.getRoot().getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                binding.addView.setVisibility(View.INVISIBLE);
                binding.closeView.setVisibility(View.INVISIBLE);

                GroupPlaceholderContent.loadGroups();
            }
        });
        //кнопка перехода к выпавшему элементу
        binding.goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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