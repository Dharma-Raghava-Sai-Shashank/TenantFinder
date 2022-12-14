package com.example.tenantfinder.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.tenantfinder.Activity.ChatActivity;
import com.example.tenantfinder.Activity.MainActivity;
import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MyConnectionRecyclerViewAdapter extends RecyclerView.Adapter<MyConnectionRecyclerViewAdapter.ViewHolder> {

    TextView Name,Email,Phone,About;
    AppDatabase Database;
    AlertDialog.Builder builder;
    List<MyConnectionData> Data;
    ImageView FullHouseImage;
    Intent intent;

    public MyConnectionRecyclerViewAdapter(List<MyConnectionData> data) { Data = data; }

    @NonNull
    @Override
    public MyConnectionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.connection_layout,parent,false);
        Database= Room.databaseBuilder(view.getContext(), AppDatabase.class,"USER DATA").allowMainThreadQueries().build();
        MyConnectionRecyclerViewAdapter.ViewHolder viewHolder=new MyConnectionRecyclerViewAdapter.ViewHolder(view);
        intent=new Intent(parent.getContext(), ChatActivity.class);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyConnectionRecyclerViewAdapter.ViewHolder holder, int position) {

        //Room Database :
        AppDataDao houseDataDao= Database.appDataDao();

        // Firebase :
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference users =firebaseDatabase.getReference("Users");

        // Firebase Storage :
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference HouseImages=storage.getReference().child("Profile Image").child(Data.get(position).getUid());

        // Setting Text and Image:
        users.child(Data.get(position).getUid()).child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Setting Image and Name :
               holder.name.setText(String.valueOf(snapshot.child("username").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.itemView).load(uri).placeholder(R.drawable.profile).into(holder.pic);
            }
        });


        // Delete Connection on Long Press:
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Alert Box:
                builder=new AlertDialog.Builder(v.getContext());
                builder.setMessage("Do you want to remove from Connections ?").setCancelable(false).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                houseDataDao.deleteConnectionDatabyID(Data.get(position).getUid());
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

                return true;
            }
        });

        // Profile image :
        final boolean[] isImageFitToScreen = {false};
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pic.setOnClickListener(new View.OnClickListener() {
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

        // Profile Dialog :
        holder.View.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onDoubleClick(v);
            }

            @Override
            public void onDoubleClick(View v) {

                // Dialog Box :
                AlertDialog.Builder profile=new AlertDialog.Builder(v.getRootView().getContext());
                View view=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.profile_dialog,null);
                MainActivity.FullImage=view.findViewById(R.id.Image);
                Name=view.findViewById(R.id.Name);
                Email=view.findViewById(R.id.Email);
                Phone=view.findViewById(R.id.Phone);
                About=view.findViewById(R.id.About);

                profile.setView(view);

                final AlertDialog alertDialog=profile.create();
                alertDialog.setCanceledOnTouchOutside(true);

                // Uploading Firebase Information :
                users.child(Data.get(position).getUid()).child("Profile").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        Name.setText("Name: "+String.valueOf(snapshot.child("username").getValue()));
                        Email.setText("Email: "+String.valueOf(snapshot.child("email").getValue()));
                        Phone.setText("Phone: "+String.valueOf(snapshot.child("phone").getValue()));
                        About.setText("About: "+String.valueOf(snapshot.child("about").getValue()));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.itemView).load(uri).placeholder(R.drawable.profile).into(MainActivity.FullImage);
                    }
                });
                alertDialog.show();

                // Profile pic full view :
                final boolean[] isImageFitToScreen = {false};
                MainActivity.FullImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.FullImage.setOnClickListener(new View.OnClickListener() {
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
            }
        });

        // Chat :
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ChatUid=Data.get(position).uid;
                holder.chat.callOnClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView pic,chat,View;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pic=itemView.findViewById(R.id.ChatImage);
            name=itemView.findViewById(R.id.ChatName);
            chat=itemView.findViewById(R.id.Chatting);
            View=itemView.findViewById(R.id.profleView);
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
