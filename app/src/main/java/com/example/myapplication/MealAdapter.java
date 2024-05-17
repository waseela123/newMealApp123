package com.example.myapplication;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

                fbs.setSelectedMeal( mealsList.get(position));

                MealDetailsFragment cd = new MealDetailsFragment();
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
    public void onBindViewHolder(@NonNull MealAdapter.MyViewHolder holder, int position) {
        Meal meal = mealsList.get(position);
        User u = fbs.getCurrentUser();
        if (u != null) {
            if (u.getFavorites().contains(meal.getId()))
                Picasso.get().load(R.drawable.favcheck).into(holder.ivFavourite);
            else
                Picasso.get().load(R.drawable.ic_fav).into(holder.ivFavourite);
        }
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
        if (meal.getPicture() == null || meal.getPicture().isEmpty()) {
        } else {
            Picasso.get().load(meal.getPicture()).into(holder.ivMeal);
        }
        holder.ivFavourite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked favorite", Toast.LENGTH_SHORT).show();

                if (isFavorite(meal) == true) {
                    removeStar(meal, holder);
                } else {
                    addStar(meal, holder);
                }
                fbs.setUserChangeFlag(true);
                //setFavourite(holder, car);
            }
        });

        holder.ivMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putParcelable("meal", mealsList.get(position)); // or use Parcelable for better performance
                MealDetailsFragment cd = new MealDetailsFragment();
                cd.setArguments(args);
                FragmentTransaction ft= ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,cd);
                ft.commit();
            }
        });

        /*
        this.itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                String selectedItem = filteredList.get(position).getNameCar();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();

                Bundle args = new Bundle();
                args.putParcelable("meal", mealsList.get(position)); // or use Parcelable for better performance
                MealDetailsFragment cd = new MealDetailsFragment();
                cd.setArguments(args);
                FragmentTransaction ft= ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,cd);
                ft.commit();
            }
        }; */
    }
    private void removeStar(Meal meal, MealAdapter.MyViewHolder holder) {
        User u = fbs.getCurrentUser();
        if (u != null) {
            if (u.getFavorites().contains(meal.getId())) {
                u.getFavorites().remove(meal.getId());
                holder.ivFavourite.setImageResource(android.R.color.transparent);
                Picasso.get().load(R.drawable.ic_fav).into(holder.ivFavourite);
            }
        }
    }
    private void addStar(Meal meal, MealAdapter.MyViewHolder holder) {
        User u = fbs.getCurrentUser();
        if (u != null) {
            u.getFavorites().add(meal.getId());
            holder.ivFavourite.setImageResource(android.R.color.transparent);
            Picasso.get().load(R.drawable.favcheck).into(holder.ivFavourite);
        }
    }
    private boolean isFavorite(Meal meal) {
        User u = fbs.getCurrentUser();
        if (u != null)
        {
            if (u.getFavorites().contains(meal.getId()))
                return true;
        }
        return false;
    }
    @Override
    public int getItemCount(){
        return mealsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivFavourite;
        TextView mealName, Price;
        ImageView ivMeal;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.tvMealName);
            Price = itemView.findViewById(R.id.tvMealPrice);
            ivMeal = itemView.findViewById(R.id.ivMealimage);
            ivFavourite = itemView.findViewById(R.id.ivFavouiteIcon); // Initialize ivFavourite here
        }
    }
public interface OnItemClickListener {
    void onItemClick(int position);

}

    public void setOnItemClickListener(MealAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

}
