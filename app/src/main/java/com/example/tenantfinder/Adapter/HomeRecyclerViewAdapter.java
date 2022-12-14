package com.example.tenantfinder.Adapter;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.Activity.MainActivity;
import com.example.tenantfinder.DataModel.HouseData;
import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.DataModel.MyFavouriteData;
import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.Fragment.AddFragment;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.R;
import com.example.tenantfinder.Utility.Utills;
import com.example.tenantfinder.ViewModel.FragmentViewModel;
import com.example.tenantfinder.databinding.HouseLayoutBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeRecyclerViewAdapter extends FirebaseRecyclerAdapter<HouseData,HomeRecyclerViewAdapter.ViewHolder> {

    FragmentViewModel fragmentViewModel;

    public HomeRecyclerViewAdapter( FirebaseRecyclerOptions<HouseData> data) {
        super(data);
    }

    @NonNull
    @Override
    public HomeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.house_layout,parent,false);
        fragmentViewModel=new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(FragmentViewModel.class);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position,HouseData houseData) {

        fragmentViewModel.GetHouseImage(holder.itemView.getContext(),holder.HouseImage,houseData.getUid());
        holder.House.setText(houseData.getHouseName());
        holder.OwnerName.setText("Ownername: " + houseData.getOwnerName());
        holder.Price.setText("Price: " + houseData.getPrice());
        holder.Address.setText("Address: " + houseData.getAddress());
        holder.Description.setText("Description: " + houseData.getDescription());

        if(fragmentViewModel.is_hexist(houseData.getUid()))
            holder.RedLike.setVisibility(View.VISIBLE);
        else
            holder.RedLike.setVisibility(View.INVISIBLE);

        if(fragmentViewModel.is_cexist(houseData.getUid().substring(0,28)))
            holder.BlueSend.setVisibility(View.VISIBLE);
        else
            holder.BlueSend.setVisibility(View.INVISIBLE);

        // Full House Image View :
        holder.HouseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentViewModel.HouseFullImage(v.getContext(),houseData.getUid());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fragmentViewModel.HouseFullImage(v.getContext(),houseData.getUid());
                return true;
            }
        });

        // Like :
        holder.itemView.setOnClickListener(new Utills.DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {}

            @Override
            public void onDoubleClick(View v) {
                if(holder.RedLike.getVisibility()==View.INVISIBLE){
                    holder.RedLike.setVisibility(View.VISIBLE);
                    if(!fragmentViewModel.is_hexist(houseData.getUid()))
                    {
                        fragmentViewModel.SetFavouriteData(new MyFavouriteData(houseData.getUid(),houseData.getHouseName(),
                                houseData.getOwnerName(),houseData.getPrice(),houseData.getAddress(),houseData.getDescription()));
                    }
                }
                else{
                    fragmentViewModel.DeleteFavouriteData(houseData.getUid());
                    holder.RedLike.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.RedLike.getVisibility()==View.INVISIBLE){
                    holder.RedLike.setVisibility(View.VISIBLE);
                    if(!fragmentViewModel.is_hexist(houseData.getUid()))
                    {
                        fragmentViewModel.SetFavouriteData(new MyFavouriteData(houseData.getUid(),houseData.getHouseName(),
                                houseData.getOwnerName(),houseData.getPrice(),houseData.getAddress(),houseData.getDescription()));
                    }
                }
                else{
                    fragmentViewModel.DeleteFavouriteData(houseData.getUid());
                    holder.RedLike.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Connection :
        holder.Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.BlueSend.getVisibility()==View.INVISIBLE){
                    holder.BlueSend.setVisibility(View.VISIBLE);

                    if(!fragmentViewModel.is_cexist(houseData.getUid().substring(0,28)))
                    {
                        fragmentViewModel.SetConnectionData(new MyConnectionData(houseData.getUid().substring(0,28)));
                    }
                }
                else{
                    holder.BlueSend.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    // View Haolder :
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView HouseImage, Like, RedLike, Send,BlueSend;
        TextView House, OwnerName, Price, Address, Description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            RedLike = itemView.findViewById(R.id.RedLikeIcon);
            Like = itemView.findViewById(R.id.LikeIcon);
            Send = itemView.findViewById(R.id.Send);
            BlueSend=itemView.findViewById(R.id.BlueSend);
            House = itemView.findViewById(R.id.House);
            HouseImage = itemView.findViewById(R.id.HouseImage);
            OwnerName = itemView.findViewById(R.id.OwnerName);
            Price = itemView.findViewById(R.id.Price);
            Address = itemView.findViewById(R.id.Address);
            Description = itemView.findViewById(R.id.Description);

        }
    }
}
