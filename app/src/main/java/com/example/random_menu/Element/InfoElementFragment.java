package com.example.random_menu.Element;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.random_menu.databinding.InfoElementFragmentBinding;


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
        //кнопка редактирования
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                binding.name.value.setVisibility(View.GONE);
                binding.comment.value.setVisibility(View.GONE);
                binding.edit.setVisibility(View.GONE);
                binding.groupText.setVisibility(View.GONE);

                binding.submit.setVisibility(View.VISIBLE);
                binding.back.setVisibility(View.VISIBLE);
                binding.groupButton.setVisibility(View.VISIBLE);
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

            }
        });
    }


}