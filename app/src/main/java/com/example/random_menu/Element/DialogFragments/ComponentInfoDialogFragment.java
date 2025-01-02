package com.example.random_menu.Element.DialogFragments;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.R;
import com.example.random_menu.databinding.InfoComponentDialogBinding;



public class ComponentInfoDialogFragment extends DialogFragment {
    InfoComponentDialogBinding binding;
    SetValues setValues;
    private static Integer listPositionCalledItem;
    private static int dbId;
    private static String name;
    private static String comment;
    private static String quantity;


    public interface SetValues{
        void setValues(String name, String comment);
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
        binding.inputName.setText(name);
        binding.inputComment.setText(comment);
    }
    public void setVars(
            int dbId,
            Integer listPosition,
            String name,
            String comment,
            String quantity,
            SetValues setValues
    ){
        listPositionCalledItem = listPosition;
        this.setValues = setValues;
        this.dbId = dbId;
        this.name = name;
        this.comment = comment;
        this.quantity = quantity;

    }
    public ComponentInfoDialogFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = InfoComponentDialogBinding.inflate(inflater,container,false);
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

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.inputName.getText().toString().length() != 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentValues cv = new ContentValues();
                            cv.put(MainBaseContract.Components.COLUMN_NAME_NAME, binding.inputName.getText().toString());
                            cv.put(MainBaseContract.Components.COLUMN_NAME_COMMENT, binding.inputComment.getText().toString());
                            ContentProviderDB.update(
                                    MainBaseContract.Components.TABLE_NAME,
                                    cv,
                                    MainBaseContract.Components._ID + " = " + dbId,
                                    null
                            );
                        }
                    }).start();
                    setValues.setValues(binding.inputName.getText().toString(), binding.inputComment.getText().toString());
                }
                getDialog().dismiss();
            }
        });
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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