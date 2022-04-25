package com.example.cats.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cats.R;
import com.example.cats.adapter.MatchAdapter;
import com.example.cats.util.Match;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private RecyclerView mMatches;
    private List<Match> mMatchList;
    private MatchAdapter mMatchAdapter;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    public ChatFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mMatches = view.findViewById(R.id.matches);
        mMatchList = new ArrayList<>();
        mMatchAdapter = new MatchAdapter(getContext(), mMatchList);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mMatches.setHasFixedSize(true);
        mMatches.setLayoutManager(new LinearLayoutManager(getContext()));
        mMatches.setAdapter(mMatchAdapter);

        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
        .collection("Match").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        Match match = documentSnapshot.toObject(Match.class);
                        mMatchList.add(match);
                        mMatchAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return view;
    }
}