package com.example.tenantfinder.Adapter;

import android.app.AlertDialog;
import android.content.Context;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.DataModel.HouseData;
import com.example.tenantfinder.DataModel.MyHouseData;
import com.example.tenantfinder.Database.AppDatabase;
import com.example.tenantfinder.Interface.AppDataDao;
import com.example.tenantfinder.R;
import com.example.tenantfinder.ViewModel.FragmentViewModel;
import com.example.tenantfinder.databinding.FullimageDialogBinding;
import com.example.tenantfinder.databinding.UpdateDialogBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MyHouseRecyclerViewAdapter extends RecyclerView.Adapter<MyHouseRecyclerViewAdapter.ViewHolder> {

    UpdateDialogBinding binding;
    FragmentViewModel fragmentViewModel;
    List<MyHouseData> Data;

    public MyHouseRecyclerViewAdapter(List<MyHouseData> data) { Data = data; }

    @NonNull
    @Override
    public MyHouseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.myhouse_layout,parent,false);
        fragmentViewModel=new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(FragmentViewModel.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHouseRecyclerViewAdapter.ViewHolder holder, int position) {

        fragmentViewModel.GetHouseImage(holder.itemView.getContext(),holder.HouseImage,Data.get(position).getUid());
        holder.House.setText(Data.get(position).HouseName);
        holder.OwnerName.setText("Ownername: "+Data.get(position).OwnerName);
        holder.Price.setText("Price: "+Data.get(position).Price);
        holder.Address.setText("Address: "+Data.get(position).Address);
        holder.Description.setText("Description: "+Data.get(position).Description);

        // Full House Image View :
        holder.HouseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentViewModel.HouseFullImage(v.getContext(),Data.get(position).getUid());
            }
        });
        // Delete :
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete(position,v.getContext());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Delete(position,v.getContext());
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
                binding=UpdateDialogBinding.bind(view);

                update.setView(view);
                final AlertDialog alertDialog=update.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                binding.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                binding.update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentViewModel.UpdateMyHouseData(new MyHouseData(Data.get(position).uid,binding.updatehousename.getText().toString(),binding.updateownername.getText().toString(),
                                binding.updateprice.getText().toString(),binding.updateaddress.getText().toString(),binding.updatedescription.getText().toString()));
                        HouseData houseData=new HouseData(binding.updatehousename.getText().toString(),binding.updateownername.getText().toString(),
                                binding.updateprice.getText().toString(),binding.updateaddress.getText().toString(),binding.updatedescription.getText().toString());
                        fragmentViewModel.UpdateHouseData(houseData,Data.get(position).uid);
                        Data.get(position).setHouseName(houseData.getHouseName());Data.get(position).setOwnerName(houseData.getOwnerName());
                        Data.get(position).setPrice(houseData.getPrice());Data.get(position).setAddress(houseData.getAddress());Data.get(position).setDescription(houseData.getDescription());
                        alertDialog.cancel();
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void Delete(int position, Context context)
    {
        // Alert Box:
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("Do you want to delete this House Data ?").setCancelable(false).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragmentViewModel.DeleteHouseData(Data.get(position).uid);
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
