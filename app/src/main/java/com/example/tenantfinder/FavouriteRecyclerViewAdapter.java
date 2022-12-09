package com.example.tenantfinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import com.example.tenantfinder.DataModel.MyFavouriteData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FavouriteRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteRecyclerViewAdapter.ViewHolder> {

    AppDatabase Database;
    AlertDialog.Builder builder;
    List<MyFavouriteData> Data;
    ImageView FullHouseImage;
    Bitmap bitmap;

    public FavouriteRecyclerViewAdapter(List<MyFavouriteData> data) { Data = data; }

    @NonNull
    @Override
    public FavouriteRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.house_layout,parent,false);
        Database= Room.databaseBuilder(view.getContext(), AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Room Database :
        AppDataDao houseDataDao=Database.houseDataDao();

        // Connection checking :
        if(houseDataDao.is_pexist(Data.get(position).getUid().substring(0,28)))
        {
            holder.BlueSend.setVisibility(View.VISIBLE);
        }
        else {
            holder.BlueSend.setVisibility(View.INVISIBLE);
        }

        // Firebase Storage :
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference HouseImages=storage.getReference().child("House Image").child(Data.get(position).getUid());

        HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.itemView).load(uri).placeholder(R.drawable.house1).into(holder.HouseImage);
            }
        });

        holder.House.setText(Data.get(position).HouseName);
        holder.OwnerName.setText("Ownername: "+Data.get(position).OwnerName);
        holder.Price.setText("Price: "+Data.get(position).Price);
        holder.Address.setText("Address: "+Data.get(position).Address);
        holder.Description.setText("Description: "+Data.get(position).Description);
        holder.RedLike.setVisibility(View.VISIBLE);

        // Like :
        // Full House Image View on Long Press :
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

                // Alert Box:
                builder=new AlertDialog.Builder(v.getContext());
                builder.setMessage("Do you want to remove from Likes ?").setCancelable(false).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                houseDataDao.deleteFavouriteDatabyID(Data.get(position).getUid());
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
        });
        holder.Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utills.AlertDialouge(v.getContext(),"Do you want to remove from Likes ?");

//                // Alert Box:
//                builder=new AlertDialog.Builder(v.getContext());
//                builder.setMessage("Do you want to remove from Likes ?").setCancelable(false).
//                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                houseDataDao.deleteFavouriteDatabyID(Data.get(position).getUid());
//                                Data.remove(position);
//                                notifyDataSetChanged();
//
//                                dialog.cancel();
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                }).show();
            }
        });

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

//            HouseImage=itemView.findViewById(R.id.HouseImage);
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

