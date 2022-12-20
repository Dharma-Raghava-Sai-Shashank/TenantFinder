package com.example.tenantfinder.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.tenantfinder.DataModel.MyChatData;
import com.example.tenantfinder.NotificationService;
import com.example.tenantfinder.R;
import com.example.tenantfinder.Utility.Utills;
import com.example.tenantfinder.ViewModel.ChatViewModel;
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
    ChatViewModel chatViewModel;
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
        // Stop Notification :
        stopService(new Intent(ChatActivity.this, NotificationService.class));
        // View Model :
        fragmentViewModel= new ViewModelProvider(this).get(FragmentViewModel.class);
        chatViewModel= new ViewModelProvider(this).get(ChatViewModel.class);
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
                                chatViewModel.DeleteAllChatData();
//                                chats.child(firebaseAuth.getUid()).child(uid).removeValue();
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

        // Chatting Activity :
        firebaseDatabase.getReference("Chats").child(firebaseAuth.getUid()).child(uid).getRef().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatViewModel.SetChatData(new MyChatData("",snapshot.child("chat").getValue().toString()));
                snapshot.getRef().removeValue();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        binding.ChatWrite.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {binding.ChatSend.callOnClick();}});
        binding.ChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=binding.ChatWrite.getText().toString();
                if(s.equals(""))
                    return;
                chatViewModel.SetChatData(new MyChatData("","M:"+s));
                chats.child(uid).child(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                            chats.child(uid).child(firebaseAuth.getUid()).child(String.valueOf(dataSnapshot.getChildrenCount())).child("chat").setValue("Y:"+s);
                    }
                });
                binding.ChatWrite.setText("");
                binding.ChatWrite.setSelected(true);
            }
        });

        // Chat Data View :
        chatViewModel.GetAllChatData();
        chatViewModel.Chat.observe(this, new Observer<List<MyChatData>>() {
            @Override
            public void onChanged(List<MyChatData> myChatData) {
                binding.ChatRecyclerView.setAdapter(new ChatsRecyclerViewAdapter(myChatData));
                layoutManager.setStackFromEnd(true);
                binding.ChatRecyclerView.setLayoutManager(layoutManager);
            }
        });
    }
    @Override
    public void onBackPressed() {
            super.onBackPressed();
            startActivity(new Intent(this,MainActivity.class));
    }
}