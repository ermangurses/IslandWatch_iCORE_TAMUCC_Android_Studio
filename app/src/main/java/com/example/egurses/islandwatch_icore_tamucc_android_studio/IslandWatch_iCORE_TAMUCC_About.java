package com.example.egurses.islandwatch_icore_tamucc_android_studio;

/**
 * Created by egurses on 2/10/17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.example.egurses.islandwatch_icore_tamucc_android_studio.IslandWatch_iCORE_TAMUCC_About.*;
public class IslandWatch_iCORE_TAMUCC_About extends Activity{


    private AlertDialog alert;
    private TextView 	iCoreWeb;
    private TextView 	iCoreEmail;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_about);
        setTitle(" About");

        iCoreWeb   = (TextView) findViewById(R.id.textView_Icore_About5);
        iCoreEmail = (TextView) findViewById(R.id.textView_Icore_About7);
        iCoreWeb.setOnClickListener(iCoreWebListener);
        iCoreEmail.setOnClickListener(iCoreEmailListener);


    }
    public OnClickListener iCoreWebListener = new OnClickListener()
    {

        public void onClick(View v)
        {

            if(inOnline2())
            {
                Uri uri = Uri.parse("http://icore.tamucc.edu");
                startActivity(new Intent (Intent.ACTION_VIEW,uri));
            }
            else
            {
                ConnectionDialogBox3();
            }

        }
    };
    public OnClickListener iCoreEmailListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            if(inOnline2())
            {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"icore@tamucc.edu"});
                intent.putExtra(Intent.EXTRA_SUBJECT," Request for more information from Island Watch App");
                String message = String.format(" ");

                intent.putExtra(Intent.EXTRA_TEXT, message);
                try
                {
                    startActivity(intent);
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(IslandWatch_iCORE_TAMUCC_About.this, "There are no email clients installed.", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                ConnectionDialogBox2();
            }
        }
    };
    private boolean inOnline2()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void ConnectionDialogBox2()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Connection");
        builder.setMessage("You must have a network connection to email iCORE.")

                .setCancelable(false)

                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                IslandWatch_iCORE_TAMUCC_About.this.finish();
                            }
                        }
                );
        alert = builder.create();
        alert.show();
    }
    public void ConnectionDialogBox3()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("No Connection");

        builder.setMessage("You must have a network connection to visit iCORE website.")

                .setCancelable(false)

                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                IslandWatch_iCORE_TAMUCC_About.this.finish();
                            }
                        }
                );
        alert = builder.create();
        alert.show();
    }
}