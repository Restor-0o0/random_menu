package com.example.random_menu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.example.random_menu.databinding.FragmentBottomBarBinding;
import com.example.random_menu.placeholder.PlaceholderContent;

public class BottomBarGroupsFragment extends Fragment {
    boolean imp = true;
    static FragmentBottomBarBinding binding;
    private ObjectAnimator mAnimator;

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

        //binding.randButton.clearAnimation();
        //binding.randButton.animate().setDuration(0);
        binding.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.moreView.setVisibility(View.VISIBLE);
                binding.closeView.setVisibility(View.VISIBLE);
            }
        });

        binding.randButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imp){
                    Animation anim = AnimationUtils.loadAnimation(binding.getRoot().getContext(),R.anim.anim_rotate);
                    anim.setDuration(1000);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            PlaceholderContent.PlaceholderItem win = PlaceholderContent.getRandom();
                            binding.winView.setVisibility(View.VISIBLE);
                            binding.closeView.setVisibility(View.VISIBLE);
                            binding.elemName.setText(win.content);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                    binding.circ.startAnimation(anim);
                    //binding.randButton.setImageResource(R.drawable.rand_button_on);
                    //imp = !imp;
                }
                else{
                   // binding.randButton.setImageResource(R.drawable.rand_button);
                    //imp = !imp;
                }





            }
        });
        binding.closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.moreView.setVisibility(View.INVISIBLE);
                binding.closeView.setVisibility(View.INVISIBLE);
                binding.winView.setVisibility(View.INVISIBLE);
            }
        });
    }
}
/*
* <ImageButton
                android:id="@+id/rand_button"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_weight="1"
                android:background="@null"
                android:contentDescription="TODO"
                app:srcCompat="@layout/ramd_circ" />*/