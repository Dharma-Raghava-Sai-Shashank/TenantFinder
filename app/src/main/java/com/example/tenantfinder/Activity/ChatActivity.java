package com.example.tenantfinder.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tenantfinder.Adapter.ChatsRecyclerViewAdapter;
import com.example.tenantfinder.DataModel.ChatData;
import com.example.tenantfinder.DataModel.HouseData;
import com.example.tenantfinder.R;
import com.example.tenantfinder.Utility.Utills;
import com.example.tenantfinder.ViewModel.FragmentViewModel;
import com.example.tenantfinder.ViewModel.MainActivityViewModel;
import com.example.tenantfinder.databinding.ActivityChatBinding;
import com.example.tenantfinder.databinding.ActivityMainBinding;
import com.example.tenantfinder.databinding.ProfileDialogBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    FragmentViewModel fragmentViewModel;
    LinearLayoutManager layoutManager;
    String uid;

    // Firebase :
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference chats =firebaseDatabase.getReference("Chats");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // View Binding :
        binding= ActivityChatBinding.inflate(getLayoutInflater());
        // Setting Layout:
        setContentView(binding.getRoot());
        // View Model :
        fragmentViewModel= new ViewModelProvider(this).get(FragmentViewModel.class);
        uid=MainActivity.ChatUid;

        // Setting Text and Image:
        fragmentViewModel.GetProfileName(uid,binding.ChatName);
        fragmentViewModel.GetProfileImage(this,binding.ChatImage,uid);

        // Profile Image View :
        binding.ChatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentViewModel.ProfileFullImage(v.getRootView().getContext(),uid);
            }
        });

        // RecyclerView :
        layoutManager = new LinearLayoutManager(binding.ChatRecyclerView.getRootView().getContext());

        // Profile Details View :
        binding.ChatName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dialog Box :
                AlertDialog.Builder profile=new AlertDialog.Builder(v.getRootView().getContext());
                View view=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.profile_dialog,null);
                ProfileDialogBinding binding=ProfileDialogBinding.bind(view);
                profile.setView(view);

                final AlertDialog alertDialog=profile.create();
                alertDialog.setCanceledOnTouchOutside(true);

                fragmentViewModel.GetProfile(uid,binding.Name,binding.Email,binding.Phone,binding.About);
                fragmentViewModel.GetProfileImage(view.getContext(),binding.Image,uid);
                alertDialog.show();
            }
        });

        // Refresh :
        binding.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewChat();
            }
        });

        // Delete Chat :
        binding.ChatDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alert Box:
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setMessage("Do you want to delete all chat ?").setCancelable(false).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chats.child(firebaseAuth.getUid()).child(uid).removeValue();
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
        chats.child(firebaseAuth.getUid()).child(uid).getRef().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {ViewChat();}
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {ViewChat();}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // Chatting Activity :
        binding.ChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.ChatWrite.getText().toString().equals(""))
                    return;
                binding.ChatSend.setClickable(false);
                chats.child(firebaseAuth.getUid()).child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String s=binding.ChatWrite.getText().toString();
                        chats.child(firebaseAuth.getUid()).child(uid).child(String.format("%10s", Integer.toBinaryString((int) dataSnapshot.getChildrenCount())).replace(" ", "0")+"-me").child("chat").setValue("M:"+s);
                        chats.child(uid).child(firebaseAuth.getUid()).child(String.format("%10s", Integer.toBinaryString((int) dataSnapshot.getChildrenCount())).replace(" ", "0")+"-you").child("chat").setValue("Y:"+s);
                    }
                }).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        binding.ChatSend.setClickable(true);
                        binding.ChatWrite.setText("");
                    }
                });
            }
        });


    }

    public void ViewChat()
    {
        List<ChatData>Data =new ArrayList<>();
        binding.ChatProgressBar.setVisibility(View.VISIBLE);
        chats.child(firebaseAuth.getUid()).child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap:dataSnapshot.getChildren()) {
                    Data.add(new ChatData(snap.child("chat").getValue().toString()));
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                binding.ChatProgressBar.setVisibility(View.INVISIBLE);
                binding.ChatRecyclerView.setAdapter(new ChatsRecyclerViewAdapter(Data));
                layoutManager.setStackFromEnd(true);
                binding.ChatRecyclerView.setLayoutManager(layoutManager);
            }
        });
    }
}