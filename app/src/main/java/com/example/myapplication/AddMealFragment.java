package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMealFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMealFragment extends Fragment {
    private static final int GALLERY_REQUEST_CODE = 123;
    ImageView img;
    private String strPicture;
    private FirebaseServices fbs;
    private Button btnAdd;
    private EditText etName, etPrice,etIngredients;
    private Utils utils;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddMealFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMealFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMealFragment newInstance(String param1, String param2) {
        AddMealFragment fragment = new AddMealFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_meal, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        init();
    }
    private void init() {
        fbs = FirebaseServices.getInstance();
        utils = Utils.getInstance();
        etName = getView().findViewById(R.id.etMealName);
        btnAdd = getView().findViewById(R.id.btnAddMealFragment);
        img = getView().findViewById(R.id.ivAddMealFragment);
        etIngredients = getView().findViewById(R.id.etMealIngredients);
        etPrice = getView().findViewById(R.id.etMealPrice);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // adding to firestore  'meal' collection

                addToFirestore();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }


        });
        //((MainActivity) getActivity()).pushFragment(new AddMealFragment());

}

    private void addToFirestore() {
        String mealname, price, ingredients;
        //get data from screen
        mealname = etName.getText().toString();
        price = etPrice.getText().toString();
        ingredients = etIngredients.getText().toString();

        if (mealname.trim().isEmpty() ||
                price.trim().isEmpty() ||
                ingredients.trim().isEmpty()) {
            Toast.makeText(getActivity(), "sorry some data missing incorrect !", Toast.LENGTH_SHORT).show();
            return;
        }

        Meal meal;
        if (fbs.getSelectedImageURL() == null)
        {
            meal = new Meal(mealname, Double.parseDouble(price),ingredients, "");
        }
        else {
            meal = new Meal(mealname, Double.parseDouble(price),ingredients, fbs.getSelectedImageURL().toString());
        }
        fbs.getFire().collection("meals").add(meal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getActivity(), "ADD Meal is Succeeded ", Toast.LENGTH_SHORT).show();
                    Log.e("addToFirestore() - add to collection: ", "Successful!");


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Can not ADD Meal! ", Toast.LENGTH_SHORT).show();
                Log.e("addToFirestore() - add to collection: ", e.getMessage());
            }
        });

    }



    private void openGallery() {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
                Uri selectedImageUri = data.getData();
                img.setImageURI(selectedImageUri);
                utils.uploadImage(getActivity(), selectedImageUri);
            }
        }
    }



