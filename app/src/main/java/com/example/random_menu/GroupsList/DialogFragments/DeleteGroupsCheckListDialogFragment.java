package com.example.random_menu.GroupsList.DialogFragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.random_menu.Element.GroupsCheckListRecyclerViewAdapter;
import com.example.random_menu.R;
import com.example.random_menu.databinding.ListRedactorCheckboxDialogBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;


public class DeleteGroupsCheckListDialogFragment extends DialogFragment {
    public ListRedactorCheckboxDialogBinding binding;
    public static NotifyList notifyList;
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
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(null);
            CheckListRecyclerViewAdapter adapter = new CheckListRecyclerViewAdapter(GroupPlaceholderContent.getGroups());
            binding.checkList.setAdapter(adapter);

        }
    }
    public void setVars(
            NotifyList notifyList
    ){
        this.notifyList = notifyList;
    }
    public DeleteGroupsCheckListDialogFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ListRedactorCheckboxDialogBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());
        //ActivityElementBinding elementActivity = ((ElementActivity) requireActivity()).getBinding();
        //ActivityElementBinding activityElementBinding = new ActivityElementBinding(getActivity());
        //подтверждения редактирования списка групп, к которым пренадлежит элемент
        binding.titleText.setText(R.string.export_title);
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(GroupPlaceholderContent.PlaceholderItem item : GroupPlaceholderContent.SelectesGroups){
                    Log.e("DeletingSelect",item.name);
                }
                notifyList.CallNotify();
                //запускаем поток на обновление и потомв мейн поток возвращаем задачи на присваивание
                getDialog().dismiss();
            }
        });
        //отмена редактирования
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        //закрытие диалога
        binding.backFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentPlaceholderContent.clearUpdateGroups();
                binding.checkList.getAdapter().notifyDataSetChanged();
                getDialog().dismiss();
            }
        });

    }


}