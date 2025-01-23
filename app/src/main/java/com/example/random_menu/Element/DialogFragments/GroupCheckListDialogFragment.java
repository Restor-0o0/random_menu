package com.example.random_menu.Element.DialogFragments;

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
import androidx.lifecycle.ViewModelProvider;

import com.example.random_menu.Element.GroupsCheckListRecyclerViewAdapter;

import com.example.random_menu.R;
import com.example.random_menu.Reposetory.InterfaceSharedDataReposetory;
import com.example.random_menu.databinding.ListRedactorCheckboxDialogBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class GroupCheckListDialogFragment extends DialogFragment {
    public ListRedactorCheckboxDialogBinding binding;
    private ComponentPlaceholderContent viewModel;

    SetValueParentFragmentField setValueParentFragmentField;


    public interface SetValueParentFragmentField{
        void setValues(String groupsVal, String groupsBut);
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

        }
    }

    public GroupCheckListDialogFragment(SetValueParentFragmentField setValueParentFragmentField) {
        this.setValueParentFragmentField = setValueParentFragmentField;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ListRedactorCheckboxDialogBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(ComponentPlaceholderContent.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GroupsCheckListRecyclerViewAdapter adapter = new GroupsCheckListRecyclerViewAdapter(
                viewModel.getGroups(),
                (group)->{
                    viewModel.checkGroups(group);
                }
        );
        Handler handler = new Handler(Looper.getMainLooper());
        binding.checkList.setAdapter(adapter);
        //ActivityElementBinding elementActivity = ((ElementActivity) requireActivity()).getBinding();
        //ActivityElementBinding activityElementBinding = new ActivityElementBinding(getActivity());
        //подтверждения редактирования списка групп, к которым пренадлежит элемент
        binding.titleText.setText(R.string.groups_title);
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //запускаем поток на обновление и потомв мейн поток возвращаем задачи на присваивание
                viewModel.UpdatedGroupsDB(()->{

                    viewModel.loadGroupsData();
                    handler.post(() ->{
                        Log.e("ErrorBinding","Fuck");
                        setValueParentFragmentField.setValues(viewModel.getActiveGroupsStr(),viewModel.getActiveGroupsStr());
                    });

                });
                getDialog().dismiss();
            }
        });
        //отмена редактирования
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.clearUpdateGroups();
                binding.checkList.getAdapter().notifyDataSetChanged();
                getDialog().dismiss();
            }
        });
        //закрытие диалога
        binding.backFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.clearUpdateGroups();
                binding.checkList.getAdapter().notifyDataSetChanged();
                getDialog().dismiss();
            }
        });

    }


}