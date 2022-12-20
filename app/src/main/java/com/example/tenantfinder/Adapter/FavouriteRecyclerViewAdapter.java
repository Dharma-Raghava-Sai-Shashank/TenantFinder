package com.example.tenantfinder.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.Activity.Registration;
import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.DataModel.MyFavouriteData;
import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.Fragment.ProfileFragment;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.R;
import com.example.tenantfinder.Utility.Utills;
import com.example.tenantfinder.ViewModel.FragmentViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FavouriteRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteRecyclerViewAdapter.ViewHolder> {

    FragmentViewModel fragmentViewModel;
    List<MyFavouriteData> Data;

    public FavouriteRecyclerViewAdapter(List<MyFavouriteData> data) { Data = data; }

    @NonNull
    @Override
    public FavouriteRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.house_layout,parent,false);
        fragmentViewModel=new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(FragmentViewModel.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        fragmentViewModel.GetHouseImage(holder.itemView.getContext(),holder.HouseImage,Data.get(position).getUid());
        holder.House.setText(Data.get(position).getHouseName());
        holder.OwnerName.setText("Ownername: " + Data.get(position).getOwnerName());
        holder.Price.setText("Price: " + Data.get(position).getPrice());
        holder.Address.setText("Address: " + Data.get(position).getAddress());
        holder.Description.setText("Description: " + Data.get(position).getDescription());

        if(fragmentViewModel.is_hexist(Data.get(position).getUid()))
            holder.RedLike.setVisibility(View.VISIBLE);
        else
            holder.RedLike.setVisibility(View.INVISIBLE);

        if(fragmentViewModel.is_cexist(Data.get(position).getUid().substring(0,28)))
            holder.BlueSend.setVisibility(View.VISIBLE);
        else
            holder.BlueSend.setVisibility(View.INVISIBLE);

        // Full House Image View :
        holder.HouseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentViewModel.HouseFullImage(v.getContext(),Data.get(position).getUid());
            }
        });
        // Like :
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Like(position,v.getContext());
                return true;
            }
        });
        holder.Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Like(position,v.getContext());
            }
        });

        // Connection :
        holder.Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.BlueSend.getVisibility()==View.INVISIBLE){
                    holder.BlueSend.setVisibility(View.VISIBLE);

                    if(!fragmentViewModel.is_cexist(Data.get(position).getUid().substring(0,28)))
                    {
                        fragmentViewModel.SetConnectionData(new MyConnectionData(Data.get(position).getUid().substring(0,28)));
                    }
                }
                else{
                    holder.BlueSend.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    // Like :
    public void Like(int position,Context context)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("Do you want to remove from Likes ?").setCancelable(false).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragmentViewModel.DeleteFavouriteData(Data.get(position).getUid());
                        Data.remove(position);
                        notifyDataSetChanged();
                        dialog.cancel();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView HouseImage,Like,RedLike,Send,BlueSend;
        TextView House,OwnerName,Price,Address,Description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            RedLike=itemView.findViewById(R.id.RedLikeIcon);
            Like=itemView.findViewById(R.id.LikeIcon);
            Send=itemView.findViewById(R.id.Send);
            BlueSend=itemView.findViewById(R.id.BlueSend);
            House=itemView.findViewById(R.id.House);
            HouseImage=itemView.findViewById(R.id.HouseImage);
            OwnerName=itemView.findViewById(R.id.OwnerName);
            Price=itemView.findViewById(R.id.Price);
            Address=itemView.findViewById(R.id.Address);
            Description=itemView.findViewById(R.id.Description);

        }
    }
}

