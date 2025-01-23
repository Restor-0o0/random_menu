package com.example.random_menu.ElementsList;

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

import com.example.random_menu.Data.Element;
import com.example.random_menu.Data.Item;
import com.example.random_menu.databinding.ItemElemSettFragmentBinding;

import com.example.random_menu.databinding.ListFragmentBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import java.util.List;
import java.util.Objects;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Item}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ElementsListRecyclerViewAdapter extends ListAdapter<Element,ElementsListRecyclerViewAdapter.ViewHolder> {

    private static LiveData<List<Element>> mValues;
    private static OnSettingItemClickListener settingClickListener;
    private static OnItemClickListener itemClickListener;

    //слушатель нажатия на кнопку настроек
    public interface OnSettingItemClickListener {
        void onButtonClick(int screenPosition,Element element, String number,String listPosition);
    }
    //слушатель нажатия на элемент
    public interface OnItemClickListener {
        void onItemClick(Element elem, int position);
    }

    public ElementsListRecyclerViewAdapter(
            LiveData<List<Element>> items,
            OnSettingItemClickListener settingClickListener,
            OnItemClickListener itemClickListener
    ) {
        super(new DiffUtil.ItemCallback<Element>() {
            @Override
            public boolean areItemsTheSame(@NonNull Element oldItem, @NonNull Element newItem) {
                return Objects.equals(oldItem.id,newItem.id);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Element oldItem, @NonNull Element newItem) {
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
        return mValues.getValue().size();
    }

    public void UpdateItem(String name, Integer position){
        mValues.getValue().set(position,new Element(
                mValues.getValue().get(position).id,
                name,
                mValues.getValue().get(position).comment,
                mValues.getValue().get(position).priority
        ));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mIdView;
        private final TextView mNameView;
        private ImageButton mImageBut;
        private Element mItem;


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
                    itemClickListener.onItemClick(
                            mItem,
                            getBindingAdapterPosition()

                    );
                    Log.e("list111",mIdView.getText().toString());
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
                            mItem,
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
