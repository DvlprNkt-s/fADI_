package com.example.fadi.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fadi.Interface.ItemClickListener;
import com.example.fadi.R;

import org.w3c.dom.Text;

public class TestsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView test_name;
    public ImageView test_image;

    private ItemClickListener itemClickListener;

    public TestsViewHolder(@NonNull View itemView) {
        super(itemView);
        test_name = (TextView)itemView.findViewById(R.id.test_name);
        test_image=(ImageView)itemView.findViewById(R.id.test_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
