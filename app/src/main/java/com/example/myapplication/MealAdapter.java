package com.example.myapplication;

import static com.google.android.material.internal.ContextUtils.getActivity;

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
    ArrayList<Meal> mealsList;
    private FirebaseServices fbs;
    private MealAdapter.OnItemClickListener itemClickListener;

    public MealAdapter( Context context, ArrayList<Meal> mealsList) {
        this.context = context;
        this.mealsList = mealsList;
        this.fbs = FirebaseServices.getInstance();

        this.itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                   /*
                String selectedItem = filteredList.get(position).getNameCar();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show(); */
                Bundle args = new Bundle();
                args.putParcelable("meal", mealsList.get(position)); // or use Parcelable for better performance
                MealDetailsFragment cd = new MealDetailsFragment();
                cd.setArguments(args);
                FragmentTransaction ft= ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,cd);
                ft.commit();
            }
        };
    }
    @NonNull
    @Override
    public MealAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MealAdapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MealAdapter.MyViewHolder holder, int position){
        Meal meal= mealsList.get(position);

        holder.mealName.setText(meal.getName());
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
        if (meal.getPicture() == null || meal.getPicture().isEmpty())
        {
        }
        else {
            Picasso.get().load(meal.getPicture()).into(holder.ivMeal);
        }

    }
    @Override
    public int getItemCount(){
        return mealsList.size();
    }

public static class MyViewHolder extends RecyclerView.ViewHolder{
    TextView mealName,Price;
    ImageView ivMeal;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        mealName=itemView.findViewById(R.id.tvMealName);
        Price=itemView.findViewById(R.id.tvMealPrice);
        ivMeal = itemView.findViewById(R.id.ivMealimage);

    }
}
public interface OnItemClickListener {
    void onItemClick(int position);

}

    public void setOnItemClickListener(MealAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

}
