package com.example.random_menu;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.random_menu.databinding.ListMenuItemCheckBinding;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>{

    private final List<RecycleItem> itemList;
    private final LayoutInflater inflater;
    RecycleAdapter(Context context, List<RecycleItem> itemList){

        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListMenuItemCheckBinding binding = ListMenuItemCheckBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecycleItem currentItem = itemList.get(position);
        holder.binding.itemName2.setText(currentItem.getName());


        if(currentItem.isButtonActive()){
            holder.binding.checkBut2.setImageResource(R.drawable.baseline_check_box_24);
        }
        else{
            holder.binding.checkBut2.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
        }

        holder.binding.checkBut2.setOnClickListener(v ->{
            boolean newState = !currentItem.isButtonActive();
            currentItem.setButtonActive(newState);

            if(newState){
                holder.binding.checkBut2.setImageResource(R.drawable.baseline_check_box_24);
            }
            else{
                holder.binding.checkBut2.setImageResource(R.drawable.baseline_check_box_outline_blank_24);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ListMenuItemCheckBinding binding;
        public ViewHolder(ListMenuItemCheckBinding binding){
            super(binding.itemName2.getRootView());
            this.binding = binding;
        }
    }
}
