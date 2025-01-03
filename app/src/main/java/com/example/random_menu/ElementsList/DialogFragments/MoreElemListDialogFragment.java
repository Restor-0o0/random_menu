package com.example.random_menu.ElementsList.DialogFragments;

import android.app.Dialog;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.example.random_menu.R;
import com.example.random_menu.Utils.ThemeManager;
import com.example.random_menu.databinding.MoreListDialogBinding;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MoreElemListDialogFragment extends DialogFragment {
    MoreListDialogBinding binding;
    private static NotifyList callCheckList;


    @Inject
    SharedPreferences saveManager;
    @Inject
    ThemeManager themeManager;
    public interface NotifyList{
        void CallNotify();
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
            NotifyList callCheckList
    ){
        this.callCheckList = callCheckList;
    }
    public MoreElemListDialogFragment() {}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MoreListDialogBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
            binding.themeButtonImg.setImageResource(R.drawable.baseline_sunny_24);
        }
        else{
            binding.themeButtonImg.setImageResource(R.drawable.baseline_bedtime_24);
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
        binding.layout.startAnimation(anim);

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCheckList.CallNotify();
                dismiss();
            }
        });
        //закрытия диалога
        binding.backFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        binding.themeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                themeManager.toggleTheme();
                /*SharedPreferences.Editor editor = saveManager.edit();
                if(saveManager.getString("theme","light") == "dark"){
                    Log.e("ToggleTheme",saveManager.getString("theme","light"));
                    binding.getRoot().getContext().setTheme(R.style.LightTheme);
                    editor.putString("theme","light");
                }else{
                    Log.e("ToggleTheme",saveManager.getString("theme","light"));
                    binding.getRoot().getContext().setTheme(R.style.DarkTheme);
                    editor.putString("theme","dark");
                }
                editor.apply();*/
            }
        });

    }


}