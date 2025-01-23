package com.example.random_menu.GroupsList.DialogFragments;

import android.app.Dialog;
import android.content.ContentValues;
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
import com.example.random_menu.R;
import com.example.random_menu.databinding.InfoGroupDialogBinding;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PropertiesDialogFragment extends DialogFragment {
    InfoGroupDialogBinding binding;

    private GroupPlaceholderContent viewModel;
    SetValues setValues;
    private static Integer listPositionCalledItem;
    private static int dbId;
    private static String name;
    private static String comment;

    public interface SetValues{
        void setValues();
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
            try{
                this.name = Objects.requireNonNull(viewModel.ITEM_MAP.get(dbId)).name;
                this.comment = Objects.requireNonNull(viewModel.ITEM_MAP.get(dbId)).comment;
            }
            catch (Exception e){
                Log.e("UpdateGroup",e.toString());
            }

        }
        binding.inputName.setText(name);
        binding.inputComment.setText(comment);
    }
    public void setVars(
            int dbId,
            Integer listPosition//,
            //SetValues setValues
    ){
        listPositionCalledItem = listPosition;
        this.setValues = setValues;
        this.dbId = dbId;



    }
    public PropertiesDialogFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = InfoGroupDialogBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(GroupPlaceholderContent.class);

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
                    viewModel.updateGroup(dbId,binding.inputName.getText().toString(),binding.inputComment.getText().toString());
                    setValues.setValues();
                    getDialog().dismiss();
                }
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
