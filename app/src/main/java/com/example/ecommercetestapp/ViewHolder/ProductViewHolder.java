package com.example.ecommercetestapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercetestapp.Interface.ItemClickListener;
import com.example.ecommercetestapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageview;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageview = itemView.findViewById(R.id.product_image);
        txtProductDescription = itemView.findViewById(R.id.product_description);
        txtProductName = itemView.findViewById(R.id.product_name);
        txtProductPrice = itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    public void onClick(View view)
    {
        listener.onClick(view,getAdapterPosition(),false);
    }
}
