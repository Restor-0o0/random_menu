package com.example.random_menu.ElementsList.DialogFragments;

import android.app.Dialog;
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
import com.example.random_menu.R;
import com.example.random_menu.databinding.MoreItemDialogBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.ArrayList;
import java.util.List;


public class MoreElemDialogFragment extends DialogFragment {
    MoreItemDialogBinding binding;
    private static Integer listPositionCalledItem;
    private static int screenPositionCalledItem;
    private static int dbId;
    private static NotifyList callNotify;

    public interface NotifyList{
        void CallNotify();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }
    public void setVars(Integer listPosition, int screenPosition, int dbId, NotifyList callNotify){
        listPositionCalledItem = listPosition;
        screenPositionCalledItem = screenPosition;
        this.dbId = dbId;
        this.callNotify = callNotify;
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

        }
    }
    public MoreElemDialogFragment() {}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MoreItemDialogBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());
        ViewGroup.MarginLayoutParams lay = (ViewGroup.MarginLayoutParams) binding.moreItemView.getLayoutParams();
        lay.topMargin = screenPositionCalledItem;
        if((binding.moreItemView.getHeight() + screenPositionCalledItem) < binding.getRoot().getHeight()){
            binding.moreItemView.setLayoutParams(lay);
        }
        else{
            lay.topMargin = screenPositionCalledItem - binding.moreItemView.getHeight();
            binding.moreItemView.setLayoutParams(lay);
        }

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
        binding.moreItemView.startAnimation(anim);

        //обработчик нажатия на кнопку удаления компонента
        binding.deleteItemBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            ContentProviderDB.delete(
                                    MainBaseContract.Components.TABLE_NAME,
                                    MainBaseContract.Components.COLUMN_NAME_ELEMENT + " = " + String.valueOf(dbId),
                                    null
                            );
                            ContentProviderDB.delete(
                                    MainBaseContract.ElemGroup.TABLE_NAME,
                                    MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " = " + String.valueOf(dbId),
                                    null
                            );
                            ContentProviderDB.delete(
                                    MainBaseContract.Elements.TABLE_NAME,
                                    MainBaseContract.Elements._ID + " = " + String.valueOf(dbId),
                                    null
                            );

                            handler.post(()->{
                                ElemPlaceholderContent.deleteElem(listPositionCalledItem);
                                //Log.e("DeleteGrouperror", String.valueOf(GroupPlaceholderContent.getGroups().size()));
                                callNotify.CallNotify();
                            });
                        }catch (Exception e){
                            Log.e("DeleteGrouperror",e.toString());
                        }
                    }
                }).start();

                    Animation anim = AnimationUtils.loadAnimation(binding.getRoot().getContext(),R.anim.anim_hide);
                    anim.setDuration(100);
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

                    binding.moreItemView.startAnimation(anim);
                    getDialog().dismiss();


            }
        });
        //закрытия диалога
        binding.backFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

    }


}