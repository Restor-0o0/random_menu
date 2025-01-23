package com.example.random_menu.GroupsList;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.random_menu.Data.Group;
import com.example.random_menu.Data.Item;
import com.example.random_menu.databinding.ItemElemSettFragmentBinding;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Item}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GroupsRecyclerViewAdapter extends ListAdapter<Group,GroupsRecyclerViewAdapter.ViewHolder> {

    private static LiveData<List<Group>> mValues;
    private static OnSettingItemClickListener settingClickListener;
    private static OnItemClickListener itemClickListener;

    //слушатель нажатия на кнопку настроек
    public interface OnSettingItemClickListener {
        void onButtonClick(int screenPosition,Integer id, String number,String listPosition);
    }

    //слушатель нажатия на элемент
    public interface OnItemClickListener {
        void onItemClick(Group group);
    }



    public GroupsRecyclerViewAdapter(
            LiveData<List<Group>> items,
            OnSettingItemClickListener settingClickListener,
            OnItemClickListener itemClickListener) {
        super(new DiffUtil.ItemCallback<Group>() {
            @Override
            public boolean areItemsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
                return Objects.equals(oldItem.id, newItem.id);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
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
            //Log.e("errrrrr",mValues.get(0).name);
            holder.mItem = mValues.getValue().get(position);
            holder.mIdView.setText(String.valueOf(position+1));
            holder.mNameView.setText(mValues.getValue().get(position).name);


        } catch (Exception e) {
            Log.e("GroupAdapterError",e.toString());
        }

    }

    @Override
    public int getItemCount() {
        if(mValues.getValue() != null){
            return mValues.getValue().size();
        }
        else{
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mIdView;
        private final TextView mNameView;
        private ImageButton mImageBut;
        private Group mItem;

        public ViewHolder(@NonNull ItemElemSettFragmentBinding binding) {
            super(binding.getRoot());
            //ListElemFragmentBinding bind;
            mIdView = binding.itemNumberS;
            mNameView = binding.contentS;
           //mIdView.setText(mItem.name);
            this.mImageBut = binding.settButton;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(mItem);
                }
            });
            binding.settButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int[] cord = {0,0};
                    Integer position = new Integer(mIdView.getText().toString()) - 1;
                    binding.settButton.getLocationOnScreen(cord);
                    settingClickListener.onButtonClick(
                            cord[1],
                            mItem.id,
                            (String) mNameView.getText(),
                            position.toString()
                    );

                }
            });
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
