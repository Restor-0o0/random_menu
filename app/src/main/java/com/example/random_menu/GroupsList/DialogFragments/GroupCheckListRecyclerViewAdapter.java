package com.example.random_menu.GroupsList.DialogFragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.random_menu.Data.Group;
import com.example.random_menu.R;
import com.example.random_menu.databinding.ItemElemCheckboxFragmentBinding;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


public class GroupCheckListRecyclerViewAdapter extends ListAdapter<Group,GroupCheckListRecyclerViewAdapter.ViewHolder> {

    private LiveData<List<Group>> mValues;
    private GroupPlaceholderContent viewModel;

    public GroupCheckListRecyclerViewAdapter(GroupPlaceholderContent viewModel,LiveData<List<Group>> items) {
        super(new DiffUtil.ItemCallback<Group>() {
            @Override
            public boolean areItemsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
                return false;
            }
        });
        this.viewModel = viewModel;
        this.viewModel.SelectesGroups.clear();
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemElemCheckboxFragmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),viewModel);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        try{
            holder.mItem = mValues.getValue().get(position);
            holder.mIdView.setText(String.valueOf(position+1));
            holder.mNameView.setText(mValues.getValue().get(position).name);
            if(holder.active){
                holder.mImageBut.setImageResource(R.drawable.baseline_check_box_24);
            }
            else{
                holder.mImageBut.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
            }
        } catch (Exception e) {
            Log.e("dataBindElemGroupsAdapter",e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return mValues.getValue().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mIdView;
        private final TextView mNameView;
        private ImageButton mImageBut;
        private boolean active = false;
        private Group mItem;

        public ViewHolder(
                @NonNull ItemElemCheckboxFragmentBinding binding,
                GroupPlaceholderContent viewModel
        ){
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mNameView = binding.content;
            mImageBut = binding.checkButton;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("list111",mIdView.getText().toString());

                    if(active){
                        active = false;

                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
                    }
                    else{
                        active = true;

                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_24);
                    }
                viewModel.checkGroups(mItem);                }
            });
            binding.checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(active){
                        active = false;

                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
                    }
                    else{
                        active = true;

                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_24);
                    }
                    viewModel.checkGroups(mItem);
                }
            });
        }
        @Override
        public String toString() {
            return super.toString();
        }
    }
}
