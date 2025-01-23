package com.example.random_menu.Element;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.random_menu.Data.Component;
import com.example.random_menu.Data.Item;
import com.example.random_menu.databinding.ItemElemSettFragmentBinding;
import com.example.random_menu.databinding.ListFragmentBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import java.util.List;
import java.util.Objects;


public class ComponentRecyclerViewAdapter extends ListAdapter<Component,ComponentRecyclerViewAdapter.ViewHolder> {

    private static LiveData<List<Component>> mValues;
    private static OnSettingItemClickListener settingClickListener;
    private static OnItemClickListener itemClickListener;
    //private static MoreItemDialogFragment moreItemDialogFragment;

    //слушатель нажатия на кнопку настроек
    public interface OnSettingItemClickListener {
        void onButtonClick(int screenposition,Component comp, String number,String listPosition);
    }
    //слушатель нажатия на элемент
    public interface OnItemClickListener {
        void onItemClick(int listPosition, Component component,String name,String comment,String quntity);
    }

    public ComponentRecyclerViewAdapter(
            LiveData<List<Component>> items,
            OnSettingItemClickListener settingClickListener,
            OnItemClickListener itemClickListener) {
        super(new DiffUtil.ItemCallback<Component>() {
            @Override
            public boolean areItemsTheSame(@NonNull Component oldItem, @NonNull Component newItem) {
                return Objects.equals(oldItem.id, newItem.id);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Component oldItem, @NonNull Component newItem) {
                return oldItem.equals(newItem);
            }
        });
        if(items != null){
            mValues = items;
        }
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

            holder.mItem = mValues.getValue().get(position);
            holder.mIdView.setText(String.valueOf(position+1));
            if(mValues.getValue().get(position).quantity.isEmpty()){
                holder.mNameView.setText(mValues.getValue().get(position).name);
            }else{
                holder.mNameView.setText(mValues.getValue().get(position).name + "\n" + "(" + mValues.getValue().get(position).quantity + ")");
            }
        } catch (Exception e) {
            Log.e("dataBindElementsAdapter",e.toString());
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
        private Component mItem;

        public ViewHolder(@NonNull ItemElemSettFragmentBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumberS;
            mNameView = binding.contentS;
            mImageBut = binding.settButton;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("list111",mIdView.getText().toString());
                    itemClickListener.onItemClick(
                            Integer.valueOf(mIdView.getText().toString())-1,
                            mItem,
                            mItem.name,
                            mItem.comment,
                            mItem.quantity
                    );
                }
            });
            binding.settButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int[] cord = {0,0};
                    Integer position = new Integer(mIdView.getText().toString()) - 1;
                    binding.settButton.getLocationOnScreen(cord);

                    //Log.e("cord",String.valueOf(cord[0]) + " " + String.valueOf(cord[1]));
                    settingClickListener.onButtonClick(
                            cord[1],
                            mItem,
                            (String) mNameView.getText(),
                            position.toString());

                }
            });
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
