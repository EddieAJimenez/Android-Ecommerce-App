package com.example.project_5;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Item> items;
    private DatabaseHelper db;
    private int userId;
    private boolean showAddToCartButton;

    public Adapter(List<Item> items, DatabaseHelper db, int userId, boolean showAddToCartButton) {
        this.items = items;
        this.db = db;
        this.userId = userId;
        this.showAddToCartButton = showAddToCartButton;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(String.valueOf(item.getPrice()));

        if (showAddToCartButton) {
            holder.itemButton.setVisibility(View.VISIBLE);
            holder.itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.addSale(item, userId);
                    Toast.makeText(v.getContext(), "Item added to cart!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.itemButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        Button itemButton;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemButton = itemView.findViewById(R.id.item_button);
        }
    }
}