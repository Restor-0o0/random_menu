package com.example.random_menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.random_menu.databinding.FragmentBottomBarBinding;
import com.example.random_menu.databinding.FragmentLoginBinding;

public class BottomBarFragment extends Fragment {

    static FragmentBottomBarBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentBottomBarBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.moreView.setVisibility(View.VISIBLE);
                binding.closeView.setVisibility(View.VISIBLE);
            }
        });
        binding.closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.moreView.setVisibility(View.INVISIBLE);
                binding.closeView.setVisibility(View.INVISIBLE);
            }
        });
    }
}