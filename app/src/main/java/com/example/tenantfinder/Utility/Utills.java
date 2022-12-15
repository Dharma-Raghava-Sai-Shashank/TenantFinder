package com.example.tenantfinder.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.tenantfinder.Activity.Registration;
import com.example.tenantfinder.Fragment.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;

public class Utills {
    public static MutableLiveData<Boolean> b=new MutableLiveData<>();

    // Alert Dialouge :
    public static void AlertDialouge(Context context, String s){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(s).setCancelable(false).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        b.setValue(true);
                        dialog.cancel();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        b.setValue(false);
                        dialog.cancel();
                    }
                }).show();
    }

    // Toast :
    public static void Toast(Context context,String s) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show();
    }

    // Dounle tap :
    public abstract static class DoubleClickListener implements View.OnClickListener {

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
