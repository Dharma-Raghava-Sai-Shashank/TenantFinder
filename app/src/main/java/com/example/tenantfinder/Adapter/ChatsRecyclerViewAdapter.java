package com.example.tenantfinder.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenantfinder.DataModel.ChatData;
import com.example.tenantfinder.R;

import java.util.List;


public class ChatsRecyclerViewAdapter extends RecyclerView.Adapter<ChatsRecyclerViewAdapter.ViewHolder> {

    List<ChatData> Data;

    public ChatsRecyclerViewAdapter(List<ChatData> data) { Data = data; }

    @NonNull
    @Override
    public ChatsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.chat_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(Data.get(position).getChat().charAt(0)=='M') {
            holder.chatY.setVisibility(View.INVISIBLE);
            holder.chatM.setText(Data.get(position).getChat().substring(2));
        }
        else{
            holder.chatM.setVisibility(View.INVISIBLE);
            holder.chatY.setText(Data.get(position).getChat().substring(2));
        }
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView chatM,chatY;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chatM=itemView.findViewById(R.id.Me);
            chatY=itemView.findViewById(R.id.You);
        }
    }
}