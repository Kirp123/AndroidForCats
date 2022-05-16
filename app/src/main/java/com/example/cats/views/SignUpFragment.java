package com.example.cats.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cats.HomeActivity;
import com.example.cats.R;
import com.example.cats.viewmodel.SignUpViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private EditText mDob;
    private EditText mloc;
    private Button mSignUpBtn;
    private FirebaseFirestore mStore;
    private SignUpViewModel signUpViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        signUpViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser !=null){
                    Toast.makeText(getContext(), "User Registered", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();


                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mAuth = FirebaseAuth.getInstance();
        mName = view.findViewById(R.id.pName);
        mEmail = view.findViewById(R.id.signup_email);
        mPassword = view.findViewById(R.id.signup_password);
        mSignUpBtn = view.findViewById(R.id.signup_button);
        mloc = view.findViewById(R.id.location_txt);
        mDob = view.findViewById(R.id.date_pick);
        mStore = FirebaseFirestore.getInstance();




        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                    signUpViewModel.register(email, password);


               /*String email = mEmail.getText().toString();
               String password = mPassword.getText().toString();
               mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           Map<String, Object> map = new HashMap<>();
                           map.put("name", mName.getText().toString());
                           map.put("age", mDob.getText().toString());
                           map.put("location", mloc.getText().toString());

                           mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                   .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){

                                       Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();;
                                       Intent intent = new Intent(getContext(), HomeActivity.class);
                                       intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       startActivity(intent);
                                       getActivity().finish();
                                   }
                               }
                           });

                       } else {
                           Toast.makeText(getContext(), "Fail" +task.getException().getMessage(),Toast.LENGTH_SHORT);
                       }
                   }
               });*/
            }
        });


        return view;
    }
}