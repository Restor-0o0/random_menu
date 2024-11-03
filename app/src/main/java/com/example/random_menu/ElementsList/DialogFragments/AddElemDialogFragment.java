package com.example.random_menu.ElementsList.DialogFragments;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.R;
import com.example.random_menu.databinding.AddElemDialogBinding;
import com.example.random_menu.databinding.AddGroupDialogBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;


public class AddElemDialogFragment extends DialogFragment {
    AddElemDialogBinding binding;
    NotifyList notify;



    public interface NotifyList{
        void notifyList();
    }

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

        }
    }
    public void setVars(
        NotifyList notifyList
    ){
        this.notify = notifyList;
    }
    public AddElemDialogFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddElemDialogBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());

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
        binding.layout.startAnimation(anim);

        //кнопка подтверждения добавления группы
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "";
                String comment ="";
                if(binding.addElemName.getTextSize() > 0) {name = String.valueOf(binding.addElemName.getText());}
                if(binding.addElemComment.getTextSize() > 0) {comment = String.valueOf(binding.addElemComment.getText());}

                if(name.length() != 0){
                    String finalName = name;
                    String finalComment = comment;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentValues cv = new ContentValues();
                            cv.put(MainBaseContract.Elements.COLUMN_NAME_NAME, finalName);
                            cv.put(MainBaseContract.Elements.COLUMN_NAME_COMMENT, finalComment);
                            int id = (int) ContentProviderDB.insert(MainBaseContract.Elements.TABLE_NAME,null,cv);
                            cv.clear();
                            cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT,id);
                            cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP, ElemPlaceholderContent.idSelectGroup);
                            ContentProviderDB.insert(MainBaseContract.ElemGroup.TABLE_NAME,null,cv);
                            handler.post(()->{
                                ElemPlaceholderContent.loadElements();
                                notify.notifyList();
                            });
                        }
                    }).start();

                }
                InputMethodManager imm = (InputMethodManager)binding.getRoot().getContext().getSystemService(binding.getRoot().getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                getDialog().dismiss();
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