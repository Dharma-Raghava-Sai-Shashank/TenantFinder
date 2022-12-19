package com.example.tenantfinder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.tenantfinder.Activity.ChatActivity;
import com.example.tenantfinder.Activity.MainActivity;
import com.example.tenantfinder.Activity.Registration;
import com.example.tenantfinder.Utility.Utills;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Notification :
        NotificationManager Notification =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        FirebaseDatabase.getInstance().getReference("Chats").child(FirebaseAuth.getInstance().getUid()).getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren())
                {
                    snap.getRef().addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            MainActivity.ChatUid=snap.getKey();
                            if(snapshot.child("chat").getValue().toString().charAt(0)=='M')
                                return;
                            FirebaseDatabase.getInstance().getReference("Users").child(snap.getKey()).child("Profile").child("username").get()
                                    .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            Notification Note;
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                Note=new Notification.Builder(getApplicationContext())
                                                        .setSmallIcon(R.drawable.tf_image)
                                                        .setLargeIcon(((BitmapDrawable) (ResourcesCompat.getDrawable(getResources(),R.drawable.menusend,null))).getBitmap())
                                                        .setChannelId("My Channel")
                                                        .setContentTitle(String.valueOf(dataSnapshot.getValue()))
                                                        .setContentText(snapshot.child("chat").getValue().toString().substring(2))
                                                        .setSubText("New Message")
                                                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(),100
                                                                ,new Intent(getApplicationContext(), ChatActivity.class)
                                                                ,PendingIntent.FLAG_UPDATE_CURRENT))
                                                        .setAutoCancel(true)
                                                        .build();
                                                Notification.createNotificationChannel(new NotificationChannel("My Channel","New Channel",NotificationManager.IMPORTANCE_HIGH));
                                            }
                                            else{
                                                Note=new Notification.Builder(getApplicationContext())
                                                        .setSmallIcon(R.drawable.tf_image).setLargeIcon(((BitmapDrawable) (ResourcesCompat.getDrawable(getResources(),R.drawable.menusend,null))).getBitmap())
                                                        .setContentTitle(String.valueOf(dataSnapshot.getValue()))
                                                        .setContentText("New Message").setSubText("New Message").build();
                                            }
                                            Notification.notify(100,Note);
                                        }
                                    });
                        }
                        @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                        @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                        @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                        @Override public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Utills.Toast(getApplicationContext(),"Destroyed");
        super.onDestroy();
    }
}
