package com.example.tenantfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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


}
