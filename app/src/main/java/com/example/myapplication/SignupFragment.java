package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;

public class SignupFragment extends Fragment {
    private EditText etUsername, etPassword, etFirstName,
            etLastName,etPhoneNumber,etAddress;
    private Button btnSignup,btnBack;
    ImageView ivUserPhoto;
    private FirebaseServices fbs;
    private Utils msg;
    private static final int GALLERY_REQUEST_CODE = 123;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // connecting components
        fbs = FirebaseServices.getInstance();
        etUsername = getView().findViewById(R.id.etUsernameSignup);
        etPassword = getView().findViewById(R.id.etPasswordSignup);
        btnSignup = getView().findViewById(R.id.btnSignupSignup);
        btnBack = getView().findViewById(R.id.btnBackSignUp);
        etFirstName =getView().findViewById(R.id.etFirstNameSignUp);
        etLastName = getView().findViewById(R.id.etLastNameSignUp);
        etAddress = getView().findViewById(R.id.etAddressSignUp);
        etPhoneNumber = getView().findViewById(R.id.etPhoneNumberSignUp);
        msg = Utils.getInstance();
        ivUserPhoto = getView().findViewById(R.id.ivPhotoSignupFragment);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gotoLoginFragment();
                setNavigationBarGone();
            }
        });
        ivUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Data validation
                String firstname = etFirstName.getText().toString();
                String lastname = etLastName.getText().toString();
                String phonenumber = etPhoneNumber.getText().toString();
                String address = etAddress.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.trim().isEmpty() && password.trim().isEmpty() && address.trim().isEmpty()
                        && phonenumber.trim().isEmpty()&& lastname.trim().isEmpty()&& firstname.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Some fiels are empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Signup procedure
                Uri selectedImageUri = fbs.getSelectedImageURL();
                String imageURL = "";
                if (selectedImageUri != null) {
                    imageURL = selectedImageUri.toString();
                }
                User user = new User(firstname, lastname,  phonenumber,  imageURL,password,address,username);
                fbs.getAuth().createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            fbs.getFire().collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    fbs= FirebaseServices.reloadInstance();
                                    gotoMealListFragment();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("SignupFragment: signupOnClick: ", e.getMessage());
                                }
                            });
                            // String firstName, String lastName, String username, String phone, String address, String photo) {
                            Toast.makeText(getActivity(), "you have succesfully signed up", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "failed to sign up! check user or password", Toast.LENGTH_SHORT).show();

                        }

                    }
                        });

            }
        });
        ((MainActivity)getActivity()).pushFragment(new SignupFragment());
    }
    public void  gotoLoginFragment()
    {
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new LoginFragment());
        ft.commit();
        setNavigationBarVisible();
    }
    public void gotoMealListFragment()
    {
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new MealListFragment());
        ft.commit();
        setNavigationBarVisible();
    }
    private void setNavigationBarVisible() {
        ((MainActivity)getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }
    private void setNavigationBarGone() {
        ((MainActivity)getActivity()).getBottomNavigationView().setVisibility(View.GONE);
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
            ivUserPhoto.setImageURI(selectedImageUri);
            Utils.getInstance().uploadImage(getActivity(), selectedImageUri);
        }
    }
}
