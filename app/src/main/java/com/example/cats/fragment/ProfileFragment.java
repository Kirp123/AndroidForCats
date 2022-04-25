package com.example.cats.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.cats.R;
import com.example.cats.MainActivity;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private static final int RESULT_LOAD_IMG = 1212;
   ImageView mImg;
    EditText mHobby;
    EditText mName;
    EditText mDesc;
    EditText mAge;

    Button mSaveProfile;
    Uri url=null;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private StorageReference mStorage;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        mImg = view.findViewById(R.id.profile_photo);
        mHobby = view.findViewById(R.id.hobby_text);
        mDesc = view.findViewById(R.id.desc_box);
        mAge = view.findViewById(R.id.age_txt2);

        mName = view.findViewById(R.id.name_text);
        mSaveProfile = view.findViewById(R.id.update_btn);
        mAuth=FirebaseAuth.getInstance();
        mStore=FirebaseFirestore.getInstance();
        mStorage=FirebaseStorage.getInstance().getReference();


        //get profiledata
        getProfileData();

        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        //logout
        Button btn=view.findViewById(R.id.singOUT);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });
        //save data
        mSaveProfile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                if(url!=null){
                    mStorage.child(ts+"/").putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
                            Task<Uri> res = taskSnapshot.getStorage().getDownloadUrl();
                            res.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    Map<String,Object> map=new HashMap<>();
                                    map.put("name", mName.getText().toString());
                                    map.put("desc",mDesc.getText().toString());
                                    map.put("img_url", downloadUrl);

                                    mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                            .update(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });

                        }
                    });
                }else{
                    Map<String,Object> map=new HashMap<>();
                    map.put("name", mName.getText().toString());
                    map.put("age", mAge.getText().toString());
                    map.put("desc",mDesc.getText().toString());

                    mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .update(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

        return view;
    }

    private void getProfileData() {
        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    String name = task.getResult().getString("name");
                    String age = task.getResult().getString("age");
                    String desc = task.getResult().getString("desc");
                    String hobby = task.getResult().getString("hobby");
                    String img_url = task.getResult().getString("img_url");

                    mAge.setText(age);
                    mDesc.setText(desc);
                    mName.setText(name);

                    if(hobby!=null){
                        List<String> mList = Arrays.asList(hobby.split("\\s*,\\s*"));

                    }
                    if(img_url!=null){
                        Glide.with(getContext()).load(img_url).into(mImg);
                    }

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();
            url =imageUri;
            Glide.with(getContext()).load(imageUri).into(mImg);

        }else {
            Toast.makeText(getContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

}

