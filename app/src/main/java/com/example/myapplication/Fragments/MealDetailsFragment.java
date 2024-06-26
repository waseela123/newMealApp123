package com.example.myapplication.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.DataBase.FirebaseServices;
import com.example.myapplication.DataBase.Meal;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MealDetailsFragment extends Fragment {
    private TextView tvMealName, tvPrice, tvIngredients;
    private ImageView ivMealPic;
    private Meal myMeal;
    private FirebaseServices fbs;
    private Button btnBack;
    private boolean isEnlarged = false; //משתנה כדי לעקוב אחרי המצב הנוכחי של התמונה (האם היא מגודלת או לא)


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MealDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MealDetailsFragment newInstance(String param1, String param2) {
        MealDetailsFragment fragment = new MealDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MealDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        ImageView ivMealPhoto = getView().findViewById(R.id.ivMealPicDetails);

        ivMealPhoto.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                ViewGroup.LayoutParams layoutParams = ivMealPic.getLayoutParams();
                if (isEnlarged) {
                    layoutParams.height = 500;
                } else {
                    layoutParams.height = 2200;
                }
                ivMealPic.setLayoutParams(layoutParams);

                // נשנה את המצב הנוכחי של התמונה
                isEnlarged = !isEnlarged;

            }
        });
    }
        public void init()
        {
            /*Meal(String nameMeal,  String price, String ingredients String photo)
             * */

            fbs = FirebaseServices.getInstance();
            tvMealName= getView().findViewById(R.id.tvMealNameDetails);
            tvPrice = getView().findViewById(R.id.tvPriceDetails);
            tvIngredients= getView().findViewById(R.id.tvIngredientsDetails);
            ivMealPic = getView().findViewById(R.id.ivMealPicDetails);
            btnBack = getView().findViewById(R.id.btnbackDetails );
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoMealListFragment();
                }
            });

            Bundle args = getArguments();
            if (args != null){
             myMeal = args.getParcelable("meal");
             if (myMeal != null){
                 tvMealName.setText(myMeal.getName());
                 tvIngredients.setText(myMeal.getIngredients());
                 tvPrice.setText(myMeal.getPrice()+" ₪");
                 if (myMeal.getPicture() == null || myMeal.getPicture().isEmpty())
                 {
                 }
                 else {
                     Picasso.get().load(myMeal.getPicture()).into(ivMealPic);
                 }
             }
            }
        }
    public void  gotoMealListFragment()
    {
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new MealListFragment());
        ft.commit();
    }
    }

