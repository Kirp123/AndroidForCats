package com.example.cats.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cats.ChatActivity;
import com.example.cats.R;
import com.example.cats.util.Match;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    Context context;
    List<Match> mMatchList;

    public MatchAdapter(Context context, List<Match> mMatchList) {

        this.context = context;
        this.mMatchList = mMatchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_user, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mName.setText(mMatchList.get(position).getName());
        Glide.with(context).load(mMatchList.get(position).getImg_url()).into(holder.mImg);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("doc_id", mMatchList.get(position).getUser_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMatchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mName;
        ImageView mImg;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            mName = itemView.findViewById(R.id.match_name);
            mImg = itemView.findViewById(R.id.match_photo);
        }
    }
}
