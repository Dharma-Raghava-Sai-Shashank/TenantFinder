package com.example.tenantfinder.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.DataModel.HouseData;
import com.example.tenantfinder.DataModel.MyHouseData;
import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MyHouseRecyclerViewAdapter extends RecyclerView.Adapter<MyHouseRecyclerViewAdapter.ViewHolder> {

    AppDatabase Database;
    List<MyHouseData> Data;
    ImageView camera;
    AlertDialog.Builder builder;
    EditText UHouseName,UOwnerName,UPrice,UAddress,UDescription;
    Button Updateb,Cancel;
    ImageView FullHouseImage;

    public MyHouseRecyclerViewAdapter(List<MyHouseData> data) { Data = data; }

    @NonNull
    @Override
    public MyHouseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.myhouse_layout,parent,false);
        Database= Room.databaseBuilder(view.getContext(), AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHouseRecyclerViewAdapter.ViewHolder holder, int position) {

        //Room Database :
        AppDataDao houseDataDao=Database.appDataDao();

        // Firebase :
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

        // Firebase Storage :
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference HouseImages=storage.getReference().child("House Image").child(Data.get(position).getUid());

        HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.itemView).load(uri).placeholder(R.drawable.house1).into(holder.HouseImage);
            }
        });

//        try {
//            File file=File.createTempFile("tempfile",".jpg");
//            HouseImages.getFile(file)
//                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                            Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
//                            holder.HouseImage.setImageBitmap(bitmap);
//
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        holder.House.setText(Data.get(position).HouseName);
        holder.OwnerName.setText("Ownername: "+Data.get(position).OwnerName);
        holder.Price.setText("Price: "+Data.get(position).Price);
        holder.Address.setText("Address: "+Data.get(position).Address);
        holder.Description.setText("Description: "+Data.get(position).Description);

        // Delete :
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
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Alert Box:
                builder=new AlertDialog.Builder(v.getContext());
                builder.setMessage("Do you want to delete this data ?").setCancelable(false).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference users =firebaseDatabase.getReference("Users");
                        users.child(firebaseAuth.getUid()).child("House").child(Data.get(position).getUid()).setValue(null);
                        DatabaseReference house=firebaseDatabase.getReference("House Data");
                        house.child(Data.get(position).getUid()).setValue(null);

                        houseDataDao.deleteHouseDatabyID(Data.get(position).getUid());
                        Data.remove(position);
                        notifyDataSetChanged();

                        HouseImages.delete();

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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Alert Box:
                builder=new AlertDialog.Builder(v.getContext());
                builder.setMessage("Do you want to delete this data ?").setCancelable(false).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference users =firebaseDatabase.getReference("Users");
                                users.child(firebaseAuth.getUid()).child("House").child(Data.get(position).getUid()).setValue(null);
                                DatabaseReference house=firebaseDatabase.getReference("House Data");
                                house.child(Data.get(position).getUid()).setValue(null);

                                houseDataDao.deleteHouseDatabyID(Data.get(position).getUid());
                                Data.remove(position);

                                HouseImages.putFile(null);

                                notifyDataSetChanged();
                                dialog.cancel();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

                return true;
            }
        });

        // Update :
        holder.Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Dialog Box :
                AlertDialog.Builder update=new AlertDialog.Builder(v.getRootView().getContext());
                View view=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.update_dialog,null);
                UHouseName=view.findViewById(R.id.updatehousename);
                UOwnerName=view.findViewById(R.id.updateownername);
                UPrice=view.findViewById(R.id.updateprice);
                UAddress=view.findViewById(R.id.updateaddress);
                UDescription=view.findViewById(R.id.updatedescription);
                Updateb=view.findViewById(R.id.update);
                Cancel=view.findViewById(R.id.cancel);
                camera=view.findViewById(R.id.camera);
                
                update.setView(view);

                final AlertDialog alertDialog=update.create();
                alertDialog.setCanceledOnTouchOutside(false);

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                Updateb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference users =firebaseDatabase.getReference("Users");
                        users.child(firebaseAuth.getUid()).child("House").child(Data.get(position).getUid()).setValue(new HouseData(Data.get(position).getUid(),UHouseName.getText().toString(),UOwnerName.getText().toString(),UPrice.getText().toString(),UAddress.getText().toString(),UDescription.getText().toString()));
                        DatabaseReference house=firebaseDatabase.getReference("House Data");
                        house.child(Data.get(position).getUid()).setValue(new HouseData(Data.get(position).getUid(),UHouseName.getText().toString(),UOwnerName.getText().toString(),UPrice.getText().toString(),UAddress.getText().toString(),UDescription.getText().toString()));

                        houseDataDao.updateMyHouseData(new MyHouseData(Data.get(position).uid,UHouseName.getText().toString(),UOwnerName.getText().toString(),UPrice.getText().toString(),UAddress.getText().toString(),UDescription.getText().toString()));
                        alertDialog.cancel();
                    }
                });

                alertDialog.show();;
            }
        });

    }
    

    @Override
    public int getItemCount() {
        return Data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView HouseImage,Delete,Update;
        TextView House,OwnerName,Price,Address,Description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Delete=itemView.findViewById(R.id.Delete);
            Update=itemView.findViewById(R.id.Update);
            House=itemView.findViewById(R.id.MyHouse);
            HouseImage=itemView.findViewById(R.id.MyHouseImage);
            OwnerName=itemView.findViewById(R.id.MyOwnerName);
            Price=itemView.findViewById(R.id.MyPrice);
            Address=itemView.findViewById(R.id.MyAddress);
            Description=itemView.findViewById(R.id.MyDescription);
        }
    }
    
}
