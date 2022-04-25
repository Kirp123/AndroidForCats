package com.example.cats.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    
    @Override
    public int getItemViewType(int position){
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
