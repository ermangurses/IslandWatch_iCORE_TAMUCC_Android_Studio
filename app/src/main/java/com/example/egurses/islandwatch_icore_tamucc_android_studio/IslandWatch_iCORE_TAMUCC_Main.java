package com.example.egurses.islandwatch_icore_tamucc_android_studio;

/**
 * Created by egurses on 2/10/17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.egurses.islandwatch_icore_tamucc_android_studio.R;

public class IslandWatch_iCORE_TAMUCC_Main extends Activity{

    String agreement_flag= "false";
    ImageButton Call_Button;
    ImageButton Report_Button;
    ImageView my_image;

    ImageButton imageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        setTitle("Island Watch");

        Call_Button   = (ImageButton) findViewById(R.id.imageButtonMainCallUdp);
        Report_Button = (ImageButton) findViewById(R.id.imageButtonMainReportUdp);
        imageButton   = (ImageButton) findViewById(R.id.imageButtonMainBottom);

        Call_Button.getBackground().setAlpha(0);
        Report_Button.getBackground().setAlpha(0);
        imageButton.getBackground().setAlpha(0);
        imageButton.setOnClickListener(imageButtonListener);

        LoadPreferences();
        if(agreement_flag.equalsIgnoreCase("false") || agreement_flag.equalsIgnoreCase(""))
        {
            AgreementDialogBox();
        }
        Call_Button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(IslandWatch_iCORE_TAMUCC_Main.this);

                builder.setTitle("Call Police");

                builder.setMessage("Are you sure you want to call UPD?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String url = "tel:3618254444";
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "Call Canceled", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        Report_Button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                AlertDialog.Builder builder  = new AlertDialog.Builder(IslandWatch_iCORE_TAMUCC_Main.this);

                builder.setTitle("Call Police");

                builder.setMessage("If this is a real emergency, please call UPD.")
                        .setCancelable(false)
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String url = "tel:3618254444";
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Report", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent=new Intent(IslandWatch_iCORE_TAMUCC_Main.this,IslandWatch_iCORE_TAMUCC_Report.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }
    public OnClickListener  imageButtonListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            Intent intent=new Intent(IslandWatch_iCORE_TAMUCC_Main.this,IslandWatch_iCORE_TAMUCC_About.class);
            startActivity(intent);
        }
    };
    @Override
    public void onBackPressed() {
        finish();
    }
    public void AgreementDialogBox()
    {
        AlertDialog.Builder builder  = new AlertDialog.Builder(IslandWatch_iCORE_TAMUCC_Main.this);
        builder.setTitle("Disclaimer");
        builder.setMessage("Please do not rely on this mobile app in case of emergency. Dial 911 or call UPD at 361-825-4444.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        agreement_flag = "true";
                        SavePreferences("agreement_flag", agreement_flag);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void SavePreferences(String key, String value)
    {
        SharedPreferences sharedPreferences =getSharedPreferences("mypref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        agreement_flag = sharedPreferences.getString("agreement_flag", "");
    }
}