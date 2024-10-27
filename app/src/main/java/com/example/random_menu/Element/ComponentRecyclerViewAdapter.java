package com.example.random_menu.Element;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.random_menu.Data.Item;
import com.example.random_menu.databinding.ItemElemSettFragmentBinding;
import com.example.random_menu.databinding.ListFragmentBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import java.util.List;


public class ComponentRecyclerViewAdapter extends RecyclerView.Adapter<ComponentRecyclerViewAdapter.ViewHolder> {

    private static List<ElemPlaceholderContent.PlaceholderItem> mValues;
    private static OnSettingItemClickListener settingClickListener;
    private static OnItemClickListener itemClickListener;

    //слушатель нажатия на кнопку настроек
    public interface OnSettingItemClickListener {
        void onButtonClick(int position,String id, String number);
    }
    //слушатель нажатия на элемент
    public interface OnItemClickListener {
        void onItemClick(String position, String number);
    }

    public ComponentRecyclerViewAdapter(List<ElemPlaceholderContent.PlaceholderItem> items, OnSettingItemClickListener settingClickListener, OnItemClickListener itemClickListener) {
        mValues = items;
        ComponentRecyclerViewAdapter.settingClickListener = settingClickListener;
        ComponentRecyclerViewAdapter.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(ItemElemSettFragmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        try{
            //Log.e("errrrrr",mValues.get(0).name);
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(String.valueOf(position+1));
            holder.mNameView.setText(mValues.get(position).name);
            /*if(mValues.get(position).details == false){
                holder.mImageBut.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
            }
            else{
                holder.mImageBut.setImageResource(R.drawable.baseline_check_box_24);
            }*/
            //holder.mIdView.setText("///////");//mValues.get(0).name


        } catch (Exception e) {
            Log.e("dataBindElementsAdapter",e.toString());
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
        private ElemPlaceholderContent.PlaceholderItem mItem;

        public ViewHolder(@NonNull ItemElemSettFragmentBinding binding) {
            super(binding.getRoot());
            ListFragmentBinding bind;
            mIdView = binding.itemNumberS;
            mNameView = binding.contentS;
           //mIdView.setText(mItem.name);
            mImageBut = binding.settButton;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("list111",mIdView.getText().toString());
                }
            });
            binding.settButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int[] cord = {0,0};
                    binding.settButton.getLocationOnScreen(cord);
                    Log.e("cord",String.valueOf(cord[0]) + " " + String.valueOf(cord[1]));
                    settingClickListener.onButtonClick(cord[1],(String) mItem.id,(String) mNameView.getText());

                }
            });
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
