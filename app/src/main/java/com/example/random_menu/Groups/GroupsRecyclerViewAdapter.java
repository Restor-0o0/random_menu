package com.example.random_menu.Groups;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
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

    //слушатель нажатия на кнопку настроек
    public interface OnSettingItemClickListener {
        void onButtonClick(int position,String id, String number);
    }

    //слушатель нажатия на элемент
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
            //Log.e("errrrrr",mValues.get(0).name);
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(String.valueOf(position+1));
            holder.mNameView.setText(mValues.get(position).name);


        } catch (Exception e) {
            Log.e("GroupAdapterErr",e.toString());
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
        private GroupPlaceholderContent.PlaceholderItem mItem;

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
                    itemClickListener.onItemClick((String) mItem.id,(String) mNameView.getText());
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
                    Log.e("coooon",String.valueOf(ContentProviderDB.query(MainBaseContract.ElemGroup.TABLE_NAME,null,MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = " + String.valueOf(mItem.id) + " and "+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " NOT IN (SELECT " +MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " FROM "+MainBaseContract.ElemGroup.TABLE_NAME+" WHERE " +MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " != " + mItem.id + " and "+MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT+ " IN (SELECT "+MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT+" FROM "+MainBaseContract.ElemGroup.TABLE_NAME+" WHERE "+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+" = "+mItem.id+"))",null,null,null,null).getCount()));
                    Log.e("coooon",String.valueOf(ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME,null,null,null,null,null,null).getCount()));
                    Log.e("coooon",String.valueOf(ContentProviderDB.query(MainBaseContract.ElemGroup.TABLE_NAME,null,null,null,null,null,null).getCount()));

                }
            });
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
