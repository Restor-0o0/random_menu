package com.example.random_menu.Groups;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.random_menu.Data.Item;
import com.example.random_menu.databinding.ItemElemSettFragmentBinding;
import com.example.random_menu.databinding.ListElemFragmentBinding;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Item}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.ViewHolder> {

    private static List<GroupPlaceholderContent.PlaceholderItem> mValues;
    private static OnSettingItemClickListener settingClickListener;
    private static OnItemClickListener itemClickListener;


    public interface OnSettingItemClickListener {
        void onButtonClick(String id, String name);
    }

    public interface OnItemClickListener {
        void onItemClick(String id, String name);
    }

    public GroupsRecyclerViewAdapter(List<GroupPlaceholderContent.PlaceholderItem> items, OnSettingItemClickListener settingClickListener, OnItemClickListener itemClickListener) {
        mValues = items;
        this.settingClickListener = settingClickListener;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(ItemElemSettFragmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        try{
            Log.e("errrrrr",mValues.get(0).name);
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).priority);
            holder.mNameView.setText(mValues.get(position).name);
            /*if(mValues.get(position).details == false){
                holder.mImageBut.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
            }
            else{
                holder.mImageBut.setImageResource(R.drawable.baseline_check_box_24);
            }*/
            //holder.mIdView.setText("///////");//mValues.get(0).name


        } catch (Exception e) {
            Log.e("errrrrr","fack");
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mIdView;
        private TextView mNameView;
        private ImageButton mImageBut;
        private GroupPlaceholderContent.PlaceholderItem mItem;

        public ViewHolder(@NonNull ItemElemSettFragmentBinding binding) {
            super(binding.getRoot());
            ListElemFragmentBinding bind;
            mIdView = binding.itemNumberS;
            mNameView = binding.contentS;
           //mIdView.setText(mItem.name);
            this.mImageBut = binding.settButton;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick((String) mItem.id,(String) mNameView.getText());
                    Log.e("list111",mIdView.getText().toString());
                }
            });
            binding.settButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    settingClickListener.onButtonClick((String) mIdView.getText(),(String) mNameView.getText());

                }
            });



            /*if(mValues.get(getAbsoluteAdapterPosition()).details == true){
                this.mImageBut.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
            }
            else{
                this.mImageBut.setImageResource(R.drawable.baseline_check_box_24);
            }*/
            //mContentView = binding.checkButton;
            /*binding.checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mValues.get(getAbsoluteAdapterPosition()).details == true){
                        mValues.get(getAbsoluteAdapterPosition()).details = false;
                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
                    }
                    else{
                        mValues.get(getAbsoluteAdapterPosition()).details = true;
                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_24);
                    }


                }
            });
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mValues.get(getAbsoluteAdapterPosition()).details == true){
                        mValues.get(getAbsoluteAdapterPosition()).details = false;
                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
                    }
                    else{
                        mValues.get(getAbsoluteAdapterPosition()).details = true;
                        binding.checkButton.setImageResource(R.drawable.baseline_check_box_24);
                    }


                }
            });*/
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
