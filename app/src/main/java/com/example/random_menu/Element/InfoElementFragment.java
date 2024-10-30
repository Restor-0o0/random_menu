package com.example.random_menu.Element;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.random_menu.R;
import com.example.random_menu.databinding.InfoElementFragmentBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.ElemPlaceholderContent;


public class InfoElementFragment extends Fragment {
    InfoElementFragmentBinding binding;

    public InfoElementFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = InfoElementFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ComponentPlaceholderContent.loadComponents();
        //кнопка редактирования
        binding.name.text.setVisibility(View.GONE);
        binding.name.value.setGravity(Gravity.CENTER);
        binding.comment.text.setText(R.string.comment_element);


        Handler handler = new Handler(Looper.getMainLooper());
        //Object object = new Object();
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                ComponentPlaceholderContent.loadData();
                handler.post(() ->{
                    Log.e("ErrorBinding","Fuck");
                    binding.name.value.setText(ComponentPlaceholderContent.nameSelectElem);
                    binding.comment.value.setText(ComponentPlaceholderContent.commentSelectElem);
                    binding.groupValue.setText(ComponentPlaceholderContent.getActiveGroupsStr());
                    binding.groupButton.setText(ComponentPlaceholderContent.getActiveGroupsStr());
                    FragmentManager fragmentManager = getParentFragmentManager();
                    ComponentsRecycleFragment  fragment = (ComponentsRecycleFragment) fragmentManager.findFragmentById(R.id.componentsListFragment);
                    fragment.binding.list1.getAdapter().notifyDataSetChanged();
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();



        //binding.name.value.setText(ComponentPlaceholderContent.nameSelectElem);
       /// binding.groupValue.setText(ElemPlaceholderContent.nameSelectGroup);


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
                binding.name.input.setText(binding.name.value.getText());
                binding.comment.input.setText(binding.comment.value.getText());
                binding.name.input.setVisibility(View.VISIBLE);
                binding.comment.input.setVisibility(View.VISIBLE);



            }
        });
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

            }
        });
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
        binding.groupsCheckList.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.groupsCheckList.getRoot().setVisibility(View.INVISIBLE);
                Runnable runnable = new Runnable(){
                    @Override
                    public void run() {
                        ComponentPlaceholderContent.UpdatedGroupsDB();
                        ComponentPlaceholderContent.loadGroupsData();
                        handler.post(() ->{
                            Log.e("ErrorBinding","Fuck");
                            binding.groupValue.setText(ComponentPlaceholderContent.getActiveGroupsStr());
                            binding.groupButton.setText(ComponentPlaceholderContent.getActiveGroupsStr());
                        });
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();

            }
        });
        binding.groupsCheckList.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.groupsCheckList.getRoot().setVisibility(View.INVISIBLE);
                ComponentPlaceholderContent.clearUpdateGroups();
            }
        });
        binding.groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.groupsCheckList.getRoot().setVisibility(View.VISIBLE);
                Log.e("ErrorBinding",""+ComponentPlaceholderContent.getGroups().size());
                try {
                    GroupsCheckListRecyclerViewAdapter adapter = new GroupsCheckListRecyclerViewAdapter(ComponentPlaceholderContent.getGroups());
                    binding.groupsCheckList.checkList.setAdapter(adapter);
                }catch (Exception e){
                    Log.e("ErrorBinding",e.toString());
                }
            }
        });
    }


}