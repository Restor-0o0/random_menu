package com.example.random_menu.Element;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.random_menu.Data.Item;
import com.example.random_menu.databinding.ItemElemSettFragmentBinding;
import com.example.random_menu.databinding.ListFragmentBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import java.util.List;


public class ComponentRecyclerViewAdapter extends RecyclerView.Adapter<ComponentRecyclerViewAdapter.ViewHolder> {

    private static List<ComponentPlaceholderContent.ComponentsPlaceholderItem> mValues;
    private static OnSettingItemClickListener settingClickListener;
    private static OnItemClickListener itemClickListener;
    //private static MoreItemDialogFragment moreItemDialogFragment;

    //слушатель нажатия на кнопку настроек
    public interface OnSettingItemClickListener {
        void onButtonClick(int screenposition,String id, String number,String listPosition);
    }
    //слушатель нажатия на элемент
    public interface OnItemClickListener {
        void onItemClick(int listPosition, int id);
    }

    public ComponentRecyclerViewAdapter(List<ComponentPlaceholderContent.ComponentsPlaceholderItem> items, OnSettingItemClickListener settingClickListener, OnItemClickListener itemClickListener) {
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

            holder.mItem = mValues.get(position);
            holder.mIdView.setText(String.valueOf(position+1));
            holder.mNameView.setText(mValues.get(position).name);
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
        private ComponentPlaceholderContent.ComponentsPlaceholderItem mItem;

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
                            Integer.valueOf(mItem.id)
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
                            (String) mItem.id,
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
