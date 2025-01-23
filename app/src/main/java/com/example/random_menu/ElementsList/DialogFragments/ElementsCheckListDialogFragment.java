package com.example.random_menu.ElementsList.DialogFragments;

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

import com.example.random_menu.R;
import com.example.random_menu.databinding.ListRedactorCheckboxDialogBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ElementsCheckListDialogFragment extends DialogFragment {
    public ListRedactorCheckboxDialogBinding binding;
    private ElemPlaceholderContent viewModel;

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
            ElemCheckListRecyclerViewAdapter adapter = new ElemCheckListRecyclerViewAdapter(
                    viewModel.getElements(),
                    (elem)->{
                        viewModel.checkElement(elem);
                    }
            );
            binding.checkList.setAdapter(adapter);

        }
    }
    public void setVars(
            NotifyList notifyList
    ){
        this.notifyList = notifyList;
    }
    public ElementsCheckListDialogFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ListRedactorCheckboxDialogBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(ElemPlaceholderContent.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());
        //ActivityElementBinding elementActivity = ((ElementActivity) requireActivity()).getBinding();
        //ActivityElementBinding activityElementBinding = new ActivityElementBinding(getActivity());
        //подтверждения редактирования списка групп, к которым пренадлежит элемент
        binding.titleText.setText(R.string.deleting_title);
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                getDialog().dismiss();
            }
        });

    }


}