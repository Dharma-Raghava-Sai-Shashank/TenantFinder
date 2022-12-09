package com.example.tenantfinder;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.DataModel.HouseData;
import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.DataModel.MyFavouriteData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeRecyclerViewAdapter extends FirebaseRecyclerAdapter<HouseData,HomeRecyclerViewAdapter.ViewHolder> {

    AppDatabase Database;
    ImageView FullHouseImage;

    public HomeRecyclerViewAdapter( FirebaseRecyclerOptions<HouseData> data) {
        super(data);
    }

    @NonNull
    @Override
    public HomeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.house_layout, parent, false);
        Database= Room.databaseBuilder(view.getContext(), AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position,HouseData houseData) {

        //Room Database :
        AppDataDao houseDataDao=Database.houseDataDao();

        // Firebase Storage :
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference HouseImages=storage.getReference().child("House Image").child(houseData.getUid());

        HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.itemView).load(uri).placeholder(R.drawable.house1).into(holder.HouseImage);
            }
        });

        holder.House.setText(houseData.getHouseName());
        holder.OwnerName.setText("Ownername: " + houseData.getOwnerName());
        holder.Price.setText("Price: " + houseData.getPrice());
        holder.Address.setText("Address: " + houseData.getAddress());
        holder.Description.setText("Description: " + houseData.getDescription());

        if(houseDataDao.is_hexist(houseData.getUid()))
        {
            holder.RedLike.setVisibility(View.VISIBLE);
        }
        else{
            holder.RedLike.setVisibility(View.INVISIBLE);
        }
        if(houseDataDao.is_pexist(houseData.getUid().substring(0,28)))
        {
            holder.BlueSend.setVisibility(View.VISIBLE);
        }
        else {
            holder.BlueSend.setVisibility(View.INVISIBLE);
        }

        // Full House Image View on Long Press :
        final boolean[] isImageFitToScreen = {false};
        holder.HouseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.HouseImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dialog Box :
                        AlertDialog.Builder fullimage=new AlertDialog.Builder(v.getRootView().getContext());
                        View view=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.fullimage_dialog,null);

                        FullHouseImage=view.findViewById(R.id.FullImage);
                        fullimage.setView(view);

                        HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(holder.itemView).load(uri).centerCrop().placeholder(R.drawable.house1).into(FullHouseImage);
                            }
                        });
                        if(isImageFitToScreen[0]) {
                            isImageFitToScreen[0] =false;
                            FullHouseImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            FullHouseImage.setAdjustViewBounds(true);
                        }else{
                            isImageFitToScreen[0] =true;
                            FullHouseImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                            FullHouseImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        }

                        final AlertDialog alertDialog=fullimage.create();
                        alertDialog.setCanceledOnTouchOutside(true);
                        alertDialog.show();
                    }
                });
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Dialog Box :
                AlertDialog.Builder fullimage=new AlertDialog.Builder(v.getRootView().getContext());
                View view=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.fullimage_dialog,null);

                FullHouseImage=view.findViewById(R.id.FullImage);
                fullimage.setView(view);

                HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.itemView).load(uri).placeholder(R.drawable.house1).into(FullHouseImage);
                    }
                });
                if(isImageFitToScreen[0]) {
                    isImageFitToScreen[0] =false;
                    FullHouseImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    FullHouseImage.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen[0] =true;
                    FullHouseImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    FullHouseImage.setScaleType(ImageView.ScaleType.FIT_XY);
                }

                final AlertDialog alertDialog=fullimage.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                return true;
            }
        });

        // Like :
        holder.itemView.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                if(holder.RedLike.getVisibility()==View.INVISIBLE){
                    holder.RedLike.setVisibility(View.VISIBLE);

                    if(!houseDataDao.is_hexist(houseData.getUid()))
                    {
                        houseDataDao.insertFavouriteData(new MyFavouriteData(houseData.getUid(),houseData.getHouseName(),houseData.getOwnerName(),houseData.getPrice(),houseData.getAddress(),houseData.getDescription()));
                    }
                }
                else{
                    houseDataDao.deleteFavouriteDatabyID(houseData.getUid());
                    holder.RedLike.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.RedLike.getVisibility()==View.INVISIBLE){
                    holder.RedLike.setVisibility(View.VISIBLE);

                    if(!houseDataDao.is_hexist(houseData.getUid()))
                    {
                        houseDataDao.insertFavouriteData(new MyFavouriteData(houseData.getUid(),houseData.getHouseName(),houseData.getOwnerName(),houseData.getPrice(),houseData.getAddress(),houseData.getDescription()));
                    }
                }
                else{
                    houseDataDao.deleteFavouriteDatabyID(houseData.getUid());
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

                    if(!houseDataDao.is_pexist(houseData.getUid().substring(0,28)))
                    {
                        houseDataDao.insertConnectionData(new MyConnectionData(houseData.getUid().substring(0,28)));
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

    // On Double Tap :
    public abstract class DoubleClickListener implements View.OnClickListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
                lastClickTime = 0;
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onSingleClick(View v);
        public abstract void onDoubleClick(View v);
    }
}
