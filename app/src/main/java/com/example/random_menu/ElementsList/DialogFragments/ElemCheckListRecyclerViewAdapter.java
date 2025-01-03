package com.example.random_menu.ElementsList.DialogFragments;

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
import com.example.random_menu.placeholder.ElemPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.List;


public class ElemCheckListRecyclerViewAdapter extends RecyclerView.Adapter<ElemCheckListRecyclerViewAdapter.ViewHolder> {

    private static List<ElemPlaceholderContent.PlaceholderItem> mValues;



    public ElemCheckListRecyclerViewAdapter(List<ElemPlaceholderContent.PlaceholderItem> items) {
        ElemPlaceholderContent.SelectesElements.clear();
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
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mIdView;
        private final TextView mNameView;
        private ImageButton mImageBut;
        private boolean active = false;
        private ElemPlaceholderContent.PlaceholderItem mItem;

        public ViewHolder(@NonNull ItemElemCheckboxFragmentBinding binding) {
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
                    ElemPlaceholderContent.checkElement(mItem);                }
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
                    ElemPlaceholderContent.checkElement(mItem);                }
            });
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
