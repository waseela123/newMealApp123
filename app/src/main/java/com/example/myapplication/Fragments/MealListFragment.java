package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.DataBase.FirebaseServices;
import com.example.myapplication.DataBase.Meal;
import com.example.myapplication.Adapter.MealAdapter;
import com.example.myapplication.DataBase.User;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  MealListFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseServices fbs;
    private ImageView ivProfile,favimj;
    private MealAdapter adapter;
    private MealAdapter myAdapter;
    private SearchView srchView;
    private ArrayList<Meal> meals,filteredList;
    private ArrayList<Meal> rests;
    private RecyclerView rvRests;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MealListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MealListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MealListFragment newInstance(String param1, String param2) {
        MealListFragment fragment = new MealListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        recyclerView =  getView().findViewById(R.id.rvMealListFragment);
        fbs = FirebaseServices.getInstance();
        /*if (fbs.getAuth().getCurrentUser() == null)
            fbs.setCurrentUser(fbs.getCurrentObjectUser()); */
        meals = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        meals = getMeals();
        myAdapter = new MealAdapter(getActivity(), meals);
        filteredList = new ArrayList<>();
        recyclerView.setAdapter(myAdapter);
        rests = new ArrayList<>();
        rvRests = getView().findViewById(R.id.rvMealListFragment);
        rvRests.setHasFixedSize(true);
        rvRests.setLayoutManager(new LinearLayoutManager(getActivity()));
        favimj = getView().findViewById(R.id.ivFavouiteIcon);

        myAdapter.setOnItemClickListener(new MealAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                // Handle item click here
                String selectedItem = meals.get(position).getName();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putParcelable("meal", meals.get(position)); // or use Parcelable for better performance
                MealDetailsFragment cd = new MealDetailsFragment();
                cd.setArguments(args);
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,cd);
                ft.commit();
            }
        });
        fbs.getFire().collection("hotels").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){
                   Meal rest = dataSnapshot.toObject(Meal.class);

                    rests.add(rest);
                }

                adapter = new MealAdapter(getContext(), rests);
                rvRests.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
                Log.e("AllHotelsFragment", e.getMessage());

            }
        });

        srchView = getView().findViewById(R.id.srchViewMealListFragment);
        srchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //applyFilter(newText);
                return false;
            }
        });
        //((MainActivity)getActivity()).pushFragment(new MealListFragment());
    }

    private void applyFilter(String query) {
        // TODO: add onBackspace - old and new query
        if (query.trim().isEmpty())
        {
            myAdapter = new MealAdapter(getContext(), meals);
            recyclerView.setAdapter(myAdapter);
            //MealAdapter.notifyDataSetChanged();
            return;
        }
        filteredList.clear();
        for(Meal meal :  meals)
        {
            if (meal.getIngredients().toLowerCase().contains(query.toLowerCase()) ||
                    meal.getPicture().toLowerCase().contains(query.toLowerCase()) ||
                    meal.getName().toLowerCase().contains(query.toLowerCase()) ||
                   meal.getPrice().toString().contains((query.toLowerCase())))
            {
                filteredList.add(meal);
            }
        }
        if (filteredList.size() == 0)
        {
            showNoDataDialogue();
            return;
        }
        myAdapter = new MealAdapter(getContext(), filteredList);
        recyclerView.setAdapter(myAdapter);



        myAdapter.setOnItemClickListener(new MealAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /*
                // Handle item click here
                String selectedItem = filteredList.get(position).getNameMeal();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putParcelable(meal", filteredList.get(position)); // or use Parcelable for better performance
                MealDetailsFragment cd = new MealDetailsFragment();
                cd.setArguments(args);
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,cd);
                ft.commit(); */
            }
        });
    }
    private void showNoDataDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("No Results");
        builder.setMessage("Try again!");
        builder.show();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.meal_list_fragment, container, false);
    }

    public ArrayList<Meal> getMeals()
    {
        ArrayList<Meal> meals = new ArrayList<>();

        {
            meals.clear();
            fbs.getFire().collection("meals")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // check in favorites array for user if the current post id is there
                                    meals.add(document.toObject(Meal.class));
                                }

                                MealAdapter adapter = new MealAdapter(getActivity(), meals);
                                recyclerView.setAdapter(adapter);
                                //addUserToCompany(companies, user);
                            } else {
                                Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });

        }


        return meals;
    }
    //Favorite Add
    @Override
    public void onPause() {
        super.onPause();
        User u = fbs.getCurrentUser();
        if (u != null && fbs.isUserChangeFlag())
            fbs.updateUser(u); // updating favorites

    }
}