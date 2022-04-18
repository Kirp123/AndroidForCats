package com.example.cats.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cats.MainActivity;
import com.example.cats.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private static final int RESULT_LOAD_IMG = 1212 ;
    ImageView mImg;
    EditText mName;
    EditText mHobby;
    EditText mDesc;
    ChipGroup mChipGroup;
    List<String> mChipList;
    Button mUpdate;
    Uri url = null;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private StorageReference mStorage;

    public ProfileFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mImg = view.findViewById(R.id.profile_photo);
        mName = view.findViewById(R.id.name_text);
        mHobby = view.findViewById(R.id.hobby_text);
        mChipGroup = view.findViewById(R.id.chipc_c);
        mDesc = view.findViewById(R.id.desc_box);
        mUpdate = view.findViewById(R.id.update_btn);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        mChipList = new ArrayList<>();

        getProfileData();

        displayChipData(mChipList);
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
        mHobby.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_GO){
                    mChipList.add(mHobby.getText().toString());
                    displayChipData(mChipList);
                    mHobby.setText("");
                    return true;

                }
                return false;
            }
        });

        Button btn = view.findViewById(R.id.singOUT);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                if(url!=null){
                    mStorage.child(ts+"/").putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        String downloadUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
//                        Log.i("TAG", "onSuccess: "+downloadUrl);
                            Task<Uri> res = taskSnapshot.getStorage().getDownloadUrl();
                            res.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    Map<String,Object> map=new HashMap<>();
                                    map.put("hobby",String.join(",",mChipList));
                                    map.put("desc",mDesc.getText().toString());
                                    map.put("img_url",downloadUrl);
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
                    map.put("hobby",String.join(",",mChipList));
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

                    String desc = task.getResult().getString("desc");
                    String name = task.getResult().getString("name");
                    String img_url = task.getResult().getString("img_url");

                    mDesc.setText(desc);
                    mName.setText(name);

                    Glide.with(getContext()).load(img_url).into(mImg);
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

    private void displayChipData(List<String> mChipList) {
        mChipGroup.removeAllViews();
        for(String s: mChipList){
            Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.single_chip_item,null,false);
            chip.setText(s);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mChipGroup.removeView(view);
                    Chip c = (Chip) view;
                    mChipList.remove(c.getText().toString());
                }
            });
            mChipGroup.addView(chip);
        }

    }

}