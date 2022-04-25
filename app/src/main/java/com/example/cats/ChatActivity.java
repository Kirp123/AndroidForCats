package com.example.cats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cats.adapter.ChatAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mChatRecycler;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private EditText mChatText;
    private ImageView mSend;
    String toID = "";
    private ChatAdapter mChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mChatRecycler = findViewById(R.id.chat_recycler);
        mChatText = findViewById(R.id.msg_txt);
        mSend = findViewById(R.id.msg_send);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mChatRecycler.setHasFixedSize(true);
        mChatRecycler.setLayoutManager(new LinearLayoutManager(this));
        toID = getIntent().getStringExtra("doc_id");

        //Toast.makeText(this, ""+getIntent().getStringExtra("doc_id"), Toast.LENGTH_SHORT).show();

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mChatText.getText().toString().isEmpty()){

                    Map<String, Object> map = new HashMap<>();
                    map.put("message", mChatText.getText().toString());
                    map.put("from", mAuth.getCurrentUser().getUid());
                    map.put("to", toID);

                    mStore.collection("Message").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                mChatText.setText("");
                                Toast.makeText(ChatActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }
}