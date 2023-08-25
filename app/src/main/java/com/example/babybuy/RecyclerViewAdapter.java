package com.example.babybuy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private List<BabyItems> babyItemsList = new LinkedList<>();
    Context context;

    public RecyclerViewAdapter(List<BabyItems> babyItemsList) {
        this.babyItemsList = babyItemsList;
    }

    /*public void setFilteredList(List<BabyItems> filteredList){
        this.babyItemsList = filteredList;
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        BabyItems babyItems = babyItemsList.get(position);

        holder.imageView.setImageBitmap(babyItems.getItemImage());
        holder.textViewDesc.setText(babyItems.getDescription());
        holder.textViewPrice.setText(String.valueOf(babyItems.getPrice()));

        holder.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("id", babyItems.getId());
//                bundle.putString("description", babyItems.getDescription());
//                bundle.putFloat("price", babyItems.getPrice());
                Intent intent = new Intent(view.getContext(), ViewItem.class);
                intent.putExtra("itemDescription", babyItems.getDescription());
                intent.putExtra("price", String.valueOf(babyItems.getPrice()));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return babyItemsList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView textViewDesc, textViewPrice;
        ImageView imageView;
        ImageButton imageButtonEdit;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.row_itemImage);
            textViewDesc = itemView.findViewById(R.id.rowItemDesc);
            textViewPrice = itemView.findViewById(R.id.rowItemPrice);
            imageButtonEdit = itemView.findViewById(R.id.editItem);
        }
    }
}
