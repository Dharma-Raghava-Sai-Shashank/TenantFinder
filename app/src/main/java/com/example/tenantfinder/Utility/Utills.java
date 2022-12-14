package com.example.tenantfinder.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

public class Utills {

    static boolean b;

    // Alert Dialouge :
    public static boolean AlertDialouge(Context context,String s){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(s).setCancelable(false).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        b=true;
                        dialog.cancel();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        b=false;
                        dialog.cancel();
                    }
                }).show();
        return b;
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
