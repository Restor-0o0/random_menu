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
import com.example.random_menu.databinding.AddComponentDialogBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;


public class AddComponentDialogFragment extends DialogFragment {
    AddComponentDialogBinding binding;
    NotifyList notify;
    private static Integer listPositionCalledItem;
    private static int dbId;
    private static String name;
    private static String comment;


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
    public AddComponentDialogFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddComponentDialogBinding.inflate(inflater,container,false);
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
                Handler handler = new Handler(Looper.getMainLooper());
                if(binding.addComponentName.getText().toString().length() != 0) {
                    Runnable task = new Runnable() {
                        @Override
                        public void run() {
                            ContentValues cv = new ContentValues();
                            cv.put(MainBaseContract.Components.COLUMN_NAME_NAME, binding.addComponentName.getText().toString());
                            cv.put(MainBaseContract.Components.COLUMN_NAME_COMMENT, binding.addComponentComment.getText().toString());
                            cv.put(MainBaseContract.Components.COLUMN_NAME_QUANTITY, binding.addComponentQuantity.getText().toString());
                            cv.put(MainBaseContract.Components.COLUMN_NAME_ELEMENT, ComponentPlaceholderContent.idSelectElem);
                            ContentProviderDB.insert(MainBaseContract.Components.TABLE_NAME, null, cv);
                            ComponentPlaceholderContent.loadComponentsData();
                            handler.post(() -> {
                                notify.notifyList();
                            });
                        }
                    };
                    Thread thread = new Thread(task);
                    thread.start();
                }
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