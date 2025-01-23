package com.example.random_menu.Element;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Element.DialogFragments.GroupCheckListDialogFragment;
import com.example.random_menu.R;
import com.example.random_menu.databinding.InfoElementFragmentBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class InfoElementFragment extends Fragment {
    InfoElementFragmentBinding binding;
    private ComponentPlaceholderContent viewModel;


    public InfoElementFragment() {}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = InfoElementFragmentBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(ComponentPlaceholderContent.class);
        viewModel.getShareElement();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GroupCheckListDialogFragment groupListFragment = new GroupCheckListDialogFragment((groupsVal, groupsBut) ->{
            binding.groupValue.setText(groupsVal);
            binding.groupButton.setText(groupsBut);
        });

        //замена теста в input`ах после активации редактирования
        binding.name.text.setVisibility(View.GONE);
        binding.comment.text.setText(R.string.comment_element);
        viewModel.loadData();

        //пуляем в отдельный поток запрос данных и потом отправляем
        //в мейн поток задачи на присвоение, все по hb
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                handler.post(() ->{
                    Log.e("ErrorBinding","Fuck");
                    binding.name.value.setText(viewModel.selectedElemen.name);
                    binding.comment.value.setText(viewModel.selectedElemen.comment);
                    binding.groupValue.setText(viewModel.getActiveGroupsStr());
                    binding.groupButton.setText(viewModel.getActiveGroupsStr());
                    FragmentManager fragmentManager = getParentFragmentManager();
                    ComponentsRecycleFragment  fragment = (ComponentsRecycleFragment) fragmentManager.findFragmentById(R.id.componentsListFragment);
                    //fragment.binding.list1.getAdapter().notifyDataSetChanged();
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        //активация режима редактирования
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                binding.name.value.setVisibility(View.GONE);
                binding.comment.value.setVisibility(View.GONE);
                binding.edit.setVisibility(View.GONE);
                binding.groupValue.setVisibility(View.GONE);

                binding.submit.setVisibility(View.VISIBLE);
                binding.back.setVisibility(View.VISIBLE);
                binding.groupButton.setVisibility(View.VISIBLE);
                binding.name.input.setVisibility(View.VISIBLE);
                binding.comment.input.setVisibility(View.VISIBLE);

                binding.groupButton.setText(binding.groupValue.getText());
                binding.comment.input.setText(binding.comment.value.getText());
                binding.name.input.setText(binding.name.value.getText());



            }
        });
        //сохранение отредактированного элемента
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.submit.setVisibility(View.GONE);
                binding.back.setVisibility(View.GONE);
                binding.groupButton.setVisibility(View.GONE);
                binding.name.input.setVisibility(View.GONE);
                binding.comment.input.setVisibility(View.GONE);

                binding.edit.setVisibility(View.VISIBLE);
                binding.name.value.setVisibility(View.VISIBLE);
                binding.comment.value.setVisibility(View.VISIBLE);
                binding.groupValue.setVisibility(View.VISIBLE);
                if(binding.name.input.getText().toString().length() != 0) {
                    binding.name.value.setText(binding.name.input.getText());
                    binding.comment.value.setText(binding.comment.input.getText());
                    viewModel.updateElem(
                            viewModel.selectedElemen.id,
                            binding.name.input.getText().toString(),
                            binding.comment.input.getText().toString(),
                            viewModel.selectedElemen.priority
                    );
                    //в отдейльный поток запрос на изменение бд

                }

            }
        });
        //отмена редактирования
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.submit.setVisibility(View.GONE);
                binding.back.setVisibility(View.GONE);
                binding.groupButton.setVisibility(View.GONE);
                binding.name.input.setVisibility(View.GONE);
                binding.comment.input.setVisibility(View.GONE);

                binding.edit.setVisibility(View.VISIBLE);
                binding.name.value.setVisibility(View.VISIBLE);
                binding.comment.value.setVisibility(View.VISIBLE);
                binding.groupValue.setVisibility(View.VISIBLE);


            }
        });
        //подтверждения редактирования списка групп, к которым пренадлежит элемент
        //запуск редактированиясписка групп к кторым пренадлежит элемент
        binding.groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupListFragment.show(getParentFragmentManager(),"GroupsCheck");
                Log.e("ErrorBinding",""+viewModel.getGroups().size());
                try {
                    GroupsCheckListRecyclerViewAdapter adapter = new GroupsCheckListRecyclerViewAdapter(
                            viewModel.getGroups(),
                            (group)->{
                                viewModel.checkGroups(
                                        group
                                );
                            }

                    );

                    groupListFragment.binding.checkList.setAdapter(adapter);
                }catch (Exception e){
                    Log.e("ErrorBinding",e.toString());
                }
            }
        });
    }


}