package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MyViewHolder> {
    Context context;
    ArrayList<MealItem> carsList;
    private FirebaseServices fbs;
 this.itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {

        }
    } ;
}
    @NonNull
    @Override
    public MealAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MealAdapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MealAdapter.MyViewHolder holder, int position){
        MealItem meal= mealsList.get(position);

        holder.mealName.setText(meal.getNameMeal());
        holder.Price.setText(meal.getPrice() + " ₪");
        holder.mealName.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(position);
            }
        });
/*
        holder.mealName.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.setOnItemClick(position);
            }
        }); */
        if (meal.getPhoto() == null || meal.getPhoto().isEmpty())
        {
            Picasso.get().load(R.drawable.ic_fav).into(holder.ivMeal);
        }
        else {
            Picasso.get().load(meal.getPhoto()).into(holder.ivMeal);
        }

    }
    @Override
    public int getItemCount(){
        return carsList.size();
    }

public static class MyViewHolder extends RecyclerView.ViewHolder{
    TextView carName,Price,Year,location,GearShift,kilometre;
    ImageView ivCar, ivFavourite;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        carName=itemView.findViewById(R.id.tvNameCar_carListFragment);
        Price=itemView.findViewById(R.id.tvPrice_carListFragment);
        Year=itemView.findViewById(R.id.tvYear_carListFragment);
        location=itemView.findViewById(R.id.tvlocation_carListFragment);
        GearShift=itemView.findViewById(R.id.tvGearShift_carListFragment);
        kilometre=itemView.findViewById(R.id.tvKelometer_carListFragment);
        ivCar = itemView.findViewById(R.id.ivCarPhotoItem);

    }
}
public interface OnItemClickListener {
    void onItemClick(int position);

}

    public void setOnItemClickListener(CarListAdapter2.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

}
