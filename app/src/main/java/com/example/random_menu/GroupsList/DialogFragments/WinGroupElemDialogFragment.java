package com.example.random_menu.GroupsList.DialogFragments;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Element.ElementActivity;
import com.example.random_menu.R;
import com.example.random_menu.databinding.WinElemDialogBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;


public class WinGroupElemDialogFragment extends DialogFragment {
    WinElemDialogBinding binding;
    String nameElem;
    String nameGroup;
    GroupPlaceholderContent.PlaceholderItem win;
    Cursor cursor;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.dimAmount = 0f;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setBackgroundDrawable(null);

            win = GroupPlaceholderContent.getRandom();
            //выхватываем запросом случайный элемент из БД
            if(win != null){
                cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME, null, "_ID IN (SELECT Element from ElemGroup where Group_ = " + win.id + ")", null, null, null, "RANDOM()");
                //Цикл если попали на пустую группу
                while (cursor.getCount() == 0) {
                    Log.e("Resel", "res");
                    //выхватываем группу
                    win = GroupPlaceholderContent.getRandom();
                    //выхватываем запросом случайный элемент из БД
                    cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME, null, "_ID IN (SELECT Element from ElemGroup where Group_ = " + win.id + ")", null, null, null, "RANDOM()");

                }
                cursor.moveToFirst();
                this.nameElem = cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_NAME));
                this.nameGroup = win.name;
                binding.elemName.setText(nameElem);
                binding.groupName.setText(nameGroup);
                ComponentPlaceholderContent.idSelectElem = cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements._ID));
                Log.e("winElem",cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_NAME)));
            }else{
                getDialog().dismiss();

            }
        }
    }
    public WinGroupElemDialogFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WinElemDialogBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());
        //binding.goTo.setBackgroundResource(R.drawable.back_button);
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
        binding.winView.startAnimation(anim);

        //кнопка подтверждения добавления группы

        //кнопка перехода к выпавшему элементу
        binding.goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ElementActivity.class);
                startActivity(intent);
            }
        });

        binding.backFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

    }


}