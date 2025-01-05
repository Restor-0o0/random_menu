package com.example.random_menu.GroupsList.DialogFragments;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.R;
import com.example.random_menu.databinding.MoreItemDialogBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.ArrayList;
import java.util.List;


public class MoreGroupDialogFragment extends DialogFragment {
    MoreItemDialogBinding binding;
    private static Integer listPositionCalledItem;
    private static int screenPositionCalledItem;
    private static int dbId;
    private static NotifyList callInfoGroupDialog;
    private static DeleteAction callNotify;

    public interface DeleteAction{
        void callAction(Integer dbID);
    }
    //интерфейс для передачи лямбда функций
    public interface NotifyList{
        void CallNotify();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }
    public void setVars(Integer listPosition, int screenPosition, int dbId, DeleteAction callNotify,NotifyList callInfoGroupDialog){
        listPositionCalledItem = listPosition;
        screenPositionCalledItem = screenPosition;
        this.dbId = dbId;
        this.callNotify = callNotify;
        this.callInfoGroupDialog = callInfoGroupDialog;
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
    public MoreGroupDialogFragment() {}
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


        //анимация рскрытия окна
        Animation anim = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.anim_show);
        anim.setDuration(200);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ViewGroup.MarginLayoutParams lay = (ViewGroup.MarginLayoutParams) binding.moreItemView.getLayoutParams();
                lay.topMargin = screenPositionCalledItem;
                if((binding.moreItemView.getHeight() + screenPositionCalledItem) < binding.getRoot().getHeight()){
                    binding.moreItemView.setLayoutParams(lay);
                }
                else{
                    lay.topMargin = screenPositionCalledItem - binding.moreItemView.getHeight();
                    binding.moreItemView.setLayoutParams(lay);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.moreItemView.startAnimation(anim);
        binding.propertiesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callInfoGroupDialog.CallNotify();
            }
        });
        //обработчик нажатия на кнопку удаления компонента
        binding.exportItemBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupPlaceholderContent.SelectesGroups.clear();
                GroupPlaceholderContent.checkGroups(GroupPlaceholderContent.ITEM_MAP.get(String.valueOf(dbId)));
                Toast.makeText(binding.getRoot().getContext(), R.string.start_export, Toast.LENGTH_SHORT).show();
                GroupPlaceholderContent.exportSelectedGroups(()->{
                    //суем данные в буффер, далеко не лучшая идея но надо без пермишнов.
                    String result = GroupPlaceholderContent.groupsToXml();

                    //GroupPlaceholderContent.xmlToClass(result);
                    //Log.e("RESULTTTT",result);
                    ClipboardManager clipboard = (ClipboardManager) binding.getRoot().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("XML Data", result);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(binding.getRoot().getContext(), R.string.xml_copied_to_clipboard, Toast.LENGTH_SHORT).show();
                });
                dismiss();
            }
        });
        binding.deleteItemBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNotify.callAction(dbId);


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