package com.example.myapplication.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Activity.CropperActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.DataBase.FirebaseServices;
import com.example.myapplication.DataBase.Meal;
import com.example.myapplication.R;
import com.example.myapplication.DataBase.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMealFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMealFragment extends Fragment {
    private static final int GALLERY_REQUEST_CODE = 123;

    private static final int UCROP_REQUEST_CODE = 123;

    ImageView img;

    Uri resultUri;
    ActivityResultLauncher<String> mGetContent;
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
        View view = inflater.inflate(R.layout.fragment_add_meal, container, false);
        init(view);
        return view;
    }

   /* @Override
    public void onStart() {
        super.onStart();
        init();
    }*/

    private void init(View view) {
        fbs = FirebaseServices.getInstance();
        utils = Utils.getInstance();
        etName = view.findViewById(R.id.etMealName);
        btnAdd = view.findViewById(R.id.btnAddMealFragment);
        img = view.findViewById(R.id.ivAddMealFragment);
        etIngredients = view.findViewById(R.id.etMealIngredients);
        etPrice = view.findViewById(R.id.etMealPrice);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFirestore();
            }
        });


        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    Intent intent = new Intent(getContext(), CropperActivity.class);
                    intent.putExtra("DATA", result.toString());
                    startActivityForResult(intent, 101);
                } else {

                    Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
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
                meal = new Meal(UUID.randomUUID().toString(),mealname, Double.parseDouble(price),ingredients, "");
        }
        else {
            meal = new Meal(UUID.randomUUID().toString(),mealname, Double.parseDouble(price),ingredients, fbs.getSelectedImageURL().toString());
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