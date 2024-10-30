package com.example.random_menu.Element;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.random_menu.R;
import com.example.random_menu.databinding.ItemElemCheckboxFragmentBinding;
import com.example.random_menu.databinding.ItemElemSettFragmentBinding;
import com.example.random_menu.databinding.ListFragmentBinding;
import com.example.random_menu.databinding.ListRedactorCheckboxBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import java.util.List;


public class GroupsCheckListRecyclerViewAdapter extends RecyclerView.Adapter<GroupsCheckListRecyclerViewAdapter.ViewHolder> {

    private static List<ComponentPlaceholderContent.GroupsPlaceholderItem> mValues;



    public GroupsCheckListRecyclerViewAdapter(List<ComponentPlaceholderContent.GroupsPlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(ItemElemCheckboxFragmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        try{
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(String.valueOf(position+1));
            holder.mNameView.setText(mValues.get(position).name);
            if(holder.mItem.active){
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
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mIdView;
        private final TextView mNameView;
        private ImageButton mImageBut;
        private ComponentPlaceholderContent.GroupsPlaceholderItem mItem;

        public ViewHolder(@NonNull ItemElemCheckboxFragmentBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mNameView = binding.content;
            mImageBut = binding.checkButton;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("list111",mIdView.getText().toString());
                    if(mItem.active){
                        mItem.active = false;
                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
                    }
                    else{
                        mItem.active = true;
                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_24);
                    }
                    ComponentPlaceholderContent.checkUpdateGroups(mItem);
                }
            });
            binding.checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mItem.active){
                        mItem.active = false;

                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
                    }
                    else{
                        mItem.active = true;

                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_24);
                    }
                    ComponentPlaceholderContent.checkUpdateGroups(mItem);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}