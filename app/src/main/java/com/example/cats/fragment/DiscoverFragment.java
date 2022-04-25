package com.example.cats.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cats.R;
import com.example.cats.util.Profile;
import com.example.cats.util.TinderCard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DiscoverFragment extends Fragment {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;

    String current_username = "";
    String other_username = "";
    String current_image = "";
    String other_image = "";

    List<Profile> mProfileList = new ArrayList<>();
    TinderCard.SelectedListener selectedListener;


    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_discover, container, false);

        mSwipeView = view.findViewById(R.id.swipeView);
        mContext = getContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));


        mStore = FirebaseFirestore.getInstance();
        mProfileList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        mStore.collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot: task.getResult()){

                                String docID = documentSnapshot.getId();
                                if(!docID.equals(mAuth.getCurrentUser().getUid())) {
                                    Profile profile = documentSnapshot.toObject(Profile.class).withId(docID);
                                    mProfileList.add(profile);

                                }
                            }
                            for(Profile profile :mProfileList){
                                mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView, selectedListener));
                            }
                        }
                    }
                });



        view.findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        view.findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        selectedListener = new TinderCard.SelectedListener() {
            @Override
            public void setSwippedDocumentId(String docID, String name) {

                mStore.collection("Users").document(docID).collection("Likes")
                        .document(mAuth.getCurrentUser().getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    if(task.getResult() != null && task.getResult().getData() != null) {
                                        mStore.collection("Users").document(docID).collection("Likes")
                                                .document(mAuth.getCurrentUser().getUid()).delete();
                                        Toast.makeText(mContext, "It's a match!", Toast.LENGTH_SHORT).show();
                                        storeMatchInDatabase(docID, name);
                                    }else {

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("like", true);

                                        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                                .collection("Likes")
                                                .document(docID).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(mContext, "Liked", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }

                                }else {

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("like", true);

                                    mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                            .collection("Likes")
                                            .document(docID).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(mContext, "Liked", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            }
                        });
            }
        };

        return view;
    }

    private void storeMatchInDatabase(final String docId, String name) {

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", docId);


        mStore.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult() != null && task.getResult().getData() != null){
                                current_username = task.getResult().getString("name");
                                current_image = task.getResult().getString("img_url");
                                mStore.collection("Users").document(docId).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    if(task.getResult() != null && task.getResult().getData() != null){
                                                        other_username = task.getResult().getString("name");
                                                        other_image = task.getResult().getString("img_url");
                                                        map.put("name", other_username);
                                                        map.put("img_url", other_image);

                                                        mStore.collection("Users").document(mAuth.getCurrentUser()
                                                                .getUid()).collection("Match").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                if(task.isSuccessful()){
                                                                    map.put("user_id", mAuth.getCurrentUser().getUid());
                                                                    map.put("name", current_username);
                                                                    map.put("img_url", current_image);
                                                                    mStore.collection("Users").document(docId).collection("Match").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                            if(task.isSuccessful()){
                                                                                Toast.makeText(getContext(), "Match", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                    }
                                                }

                                            }
                                        });
                            }
                        }

                    }
                });


    }
}