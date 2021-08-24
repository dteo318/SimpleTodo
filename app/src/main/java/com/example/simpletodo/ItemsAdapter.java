package com.example.simpletodo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Responsible for taking data from model and displaying it as a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    ArrayList<TodoItem> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemsAdapter(ArrayList<TodoItem> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use layout inflater to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        // Wrap it inside a viewHolder and return it
        return new ViewHolder(todoView);
    }

    // Responsible for binding data to a particular viewHolder
    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ViewHolder holder, int position) {
        // Grab item at position
        TodoItem item = items.get(position);
        // Bind item to a specified viewHolder
        holder.bind(item);
    }

    // Tells recycler view how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem, tvItemDate;
        CardView cardItemContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvTodoItemName);
            tvItemDate = itemView.findViewById(R.id.tvTodoItemDate);
            cardItemContainer = itemView.findViewById(R.id.cardItemContainer);
        }

        // Update view inside view holder with this data
        public void bind(TodoItem item) {
            String itemName = item.getName();
            String itemDateString = item.getDateString();

            tvItem.setText(itemName);
            tvItemDate.setText(itemDateString);
            cardItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            cardItemContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Notifying listener which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
