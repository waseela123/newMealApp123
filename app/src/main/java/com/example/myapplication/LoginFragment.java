package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginFragment extends Fragment {
    private EditText etUsername, etPassword;
    private Button tvSignupLink;
    private Button btnLogin;
    private Button btnAddLinkAddMeal;
    private FirebaseServices fbs;
    private Button btnForgotPasswordLink, btnAppInfoLink;
    private ImageView logo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onStart() {
        super.onStart();
        // connecting components
       // logo = logo.findViewById(R.id.ivLogoLogin);
        fbs = FirebaseServices.getInstance();
        btnAppInfoLink = getView().findViewById(R.id.btnGoToInfo);
        etUsername = getView().findViewById(R.id.etUsernameLogin);
        etPassword = getView().findViewById(R.id.etPasswordLogin);
        btnLogin = getView().findViewById(R.id.btnLoginLogin);
        tvSignupLink = getView().findViewById(R.id.btnSignupLinkLogin);
        btnForgotPasswordLink = getView().findViewById(R.id.btnForgotPasswordLinkLogin);
        //btnAddLinkAddMeal = getView().findViewById(R.id.btnAddLinkAddMealFragment);
    //    btnAddLinkAddMeal.setOnClickListener(new View.OnClickListener() {
       //     @Override
        //    public void onClick(View v) {
        //        gotoAddMealFragment();
        //    }
       // });
        tvSignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignupFragment();
            }
        });
        btnForgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoForgotPasswordFragment();
            }
        });
        btnAppInfoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {gotoAppInfoFragment();}

        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Data validation
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.trim().isEmpty() && password.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Some Data are empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Login procedure
                fbs.getAuth().signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "You have successfully login!", Toast.LENGTH_SHORT).show();
                            gotoMealListFragment();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Failed to login! Check user or password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void gotoAddMealFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new AddMealFragment());
        ft.commit();
    }

    private void gotoSignupFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new SignupFragment());
        ft.commit();
    }
    private void gotoForgotPasswordFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new ForgotPasswordFragment());
        ft.commit();
    }
    private void gotoMealListFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new MealListFragment());
        ft.commit();
    }
    private void gotoAppInfoFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new BasicInfoAboutAppFragment());
        ft.commit();
    }

}


