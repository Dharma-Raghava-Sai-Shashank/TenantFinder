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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.Activity.ChatActivity;
import com.example.tenantfinder.Activity.MainActivity;
import com.example.tenantfinder.DataModel.MyConnectionData;
import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.R;
import com.example.tenantfinder.ViewModel.FragmentViewModel;
import com.example.tenantfinder.databinding.ProfileDialogBinding;
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

    FragmentViewModel fragmentViewModel;
    List<MyConnectionData> Data;

    public MyConnectionRecyclerViewAdapter(List<MyConnectionData> data) { Data = data; }

    @NonNull
    @Override
    public MyConnectionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.connection_layout,parent,false);
        fragmentViewModel=new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(FragmentViewModel.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyConnectionRecyclerViewAdapter.ViewHolder holder, int position) {
        String uid=Data.get(position).uid;

        // Setting Text and Image:
        fragmentViewModel.GetProfileName(uid,holder.name);
        fragmentViewModel.GetProfileImage(holder.itemView.getContext(),holder.pic,uid);

        // Delete Connection on Long Press:
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Alert Box:
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setMessage("Do you want to remove from Connections ?").setCancelable(false).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fragmentViewModel.DeleteConnectionData(uid);
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
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentViewModel.ProfileFullImage(v.getRootView().getContext(), uid);
            }
        });

        // Profile Dialog :
        holder.View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dialog Box :
                AlertDialog.Builder profile=new AlertDialog.Builder(v.getRootView().getContext());
                View view=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.profile_dialog,null);
                ProfileDialogBinding binding=ProfileDialogBinding.bind(view);
                profile.setView(view);

                final AlertDialog alertDialog=profile.create();
                alertDialog.setCanceledOnTouchOutside(true);

                // Uploading Firebase Information :
                fragmentViewModel.GetProfile(uid,binding.Name,binding.Email,binding.Phone,binding.About);
                fragmentViewModel.GetProfileImage(holder.itemView.getContext(),binding.Image,uid);
                alertDialog.show();
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
}
