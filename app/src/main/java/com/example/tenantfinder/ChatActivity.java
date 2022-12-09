package com.example.tenantfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.tenantfinder.DataModel.ChatData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ImageView profileimage,delete,send,refresh;
    EditText chat;
    TextView profilename;
    RecyclerView recyclerView;
    ChatsRecyclerViewAdapter chatsRecyclerViewAdapter;
    ImageView FullHouseImage;
    TextView Name,Email,Phone,About;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Profile :
        profileimage=findViewById(R.id.ChatImage);
        profilename=findViewById(R.id.ChatName);
        delete=findViewById(R.id.ChatDelete);
        refresh=findViewById(R.id.refresh);
        send=findViewById(R.id.ChatSend);
        chat=findViewById(R.id.ChatWrite);
        progressBar=findViewById(R.id.ChatProgressBar);
        recyclerView=findViewById(R.id.ChatRecyclerView);

        // Firebase :
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference users =firebaseDatabase.getReference("Users");
        DatabaseReference chats =firebaseDatabase.getReference("Chats");


        // Firebase Storage :
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference HouseImages=storage.getReference().child("Profile Image").child(MainActivity.ChatUid);

        HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplication()).load(uri).placeholder(R.drawable.profile).into(profileimage);
            }
        });

        users.child(MainActivity.ChatUid).child("Signup Credentials").child("username").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                profilename.setText(dataSnapshot.getValue().toString());
            }
        });

        // Profile Image View :
        final boolean[] isImageFitToScreen = {false};
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dialog Box :
                        AlertDialog.Builder fullimage=new AlertDialog.Builder(v.getRootView().getContext());
                        View view= LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.fullimage_dialog,null);

                        FullHouseImage=view.findViewById(R.id.FullImage);
                        fullimage.setView(view);

                        HouseImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getApplication()).load(uri).placeholder(R.drawable.house1).into(FullHouseImage);
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

        // Profile Details View :
        profilename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                users.child(MainActivity.ChatUid).child("Profile").addValueEventListener(new ValueEventListener() {
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
                        Glide.with(getApplication()).load(uri).placeholder(R.drawable.profile).into(MainActivity.FullImage);
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
                                        Glide.with(getApplication()).load(uri).placeholder(R.drawable.house1).into(FullHouseImage);
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

        // View Chat :
        chat.setText("");
        chat.setSelected(true);
        chat.setActivated(true);
//        chat.setFocusable(true);
        progressBar.setVisibility(View.VISIBLE);
        List<ChatData>Data=new ArrayList<>();
        chats.child(firebaseAuth.getUid()).child(MainActivity.ChatUid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren())
                {
                    final String[] s = new String[1];
                    snap.child("chat").getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            s[0] =snapshot.getValue().toString();
                            Data.add(new ChatData(s[0]));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        }).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                progressBar.setVisibility(View.INVISIBLE);
                chatsRecyclerViewAdapter=new ChatsRecyclerViewAdapter(Data);
                recyclerView.setAdapter(chatsRecyclerViewAdapter);
            }
        });

        // Refresh :
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat.callOnClick();
            }
        });

        // Delete Chat :
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Alert Box:
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setMessage("Do you want to delete all chat ?").setCancelable(false).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chats.child(firebaseAuth.getUid()).child(MainActivity.ChatUid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String s=chat.getText().toString();
                        chats.child(firebaseAuth.getUid()).child(MainActivity.ChatUid).child(dataSnapshot.getChildrenCount()+"-me").child("chat").setValue("M:"+s);
                        chats.child(MainActivity.ChatUid).child(firebaseAuth.getUid()).child(dataSnapshot.getChildrenCount()+"-you").child("chat").setValue("Y:"+s);
                    }
                }).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        chat.setText("");
                        // View Chat :
                        progressBar.setVisibility(View.VISIBLE);
                        List<ChatData>Data=new ArrayList<>();
                        chats.child(firebaseAuth.getUid()).child(MainActivity.ChatUid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snap : dataSnapshot.getChildren())
                                {
                                    final String[] s = new String[1];
                                    snap.child("chat").getRef().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            s[0] =snapshot.getValue().toString();
                                            Data.add(new ChatData(s[0]));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                            }
                        }).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                progressBar.setVisibility(View.INVISIBLE);
                                chatsRecyclerViewAdapter=new ChatsRecyclerViewAdapter(Data);
                                recyclerView.setAdapter(chatsRecyclerViewAdapter);
                            }
                        });
                    }
                });
            }
        });


        // RecyclerView :
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setSmoothScrollbarEnabled(false);
//        layoutManager.setReverseLayout(true);

    }
}