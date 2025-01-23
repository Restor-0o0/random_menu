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
import androidx.lifecycle.ViewModelProvider;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Element;
import com.example.random_menu.Data.Group;
import com.example.random_menu.Element.ElementActivity;
import com.example.random_menu.R;
import com.example.random_menu.databinding.WinElemDialogBinding;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WinGroupElemDialogFragment extends DialogFragment {

    private GroupPlaceholderContent viewModel;

    WinElemDialogBinding binding;
    String nameElem;
    String nameGroup;
    Group winGroup;
    Element winElement;
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

            winGroup = viewModel.getRandom();
            //выхватываем запросом случайный элемент из БД
            if(winGroup != null){
                cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME, null, "_ID IN (SELECT Element from ElemGroup where Group_ = " + winGroup.id + ")", null, null, null, "RANDOM()");
                //Цикл если попали на пустую группу
                while (cursor.getCount() == 0) {
                    Log.e("Resel", "res");
                    //выхватываем группу
                    winGroup = viewModel.getRandom();
                    //выхватываем запросом случайный элемент из БД
                    cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME, null, "_ID IN (SELECT Element from ElemGroup where Group_ = " + winGroup.id + ")", null, null, null, "RANDOM()");

                }
                cursor.moveToFirst();
                this.nameElem = cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_NAME));
                this.nameGroup = winGroup.name;
                binding.elemName.setText(nameElem);
                binding.groupName.setText(nameGroup);
                winElement = new Element(
                        cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Elements._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_COMMENT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_PRIORITY))
                );
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
        viewModel = new ViewModelProvider(requireActivity()).get(GroupPlaceholderContent.class);
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
                viewModel.setShareGroup(winGroup);
                viewModel.setShareElement(winElement);
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