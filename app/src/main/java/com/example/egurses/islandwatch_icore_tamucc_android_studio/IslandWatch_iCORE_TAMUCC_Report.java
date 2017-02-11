package com.example.egurses.islandwatch_icore_tamucc_android_studio;

/**
 * Created by egurses on 2/10/17.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.egurses.islandwatch_icore_tamucc_android_studio.R;

public class IslandWatch_iCORE_TAMUCC_Report extends Activity implements LocationListener {

    private IslandWatch_iCORE_TAMUCC_AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private static final int 	ACTION_TAKE_PHOTO_B = 1;
    private OutputStream 		outStream = null;
    private LocationManager 	locationManager;
    private String 				provider;
    private	float 				latitude;
    private	float 				longitude;
    private Uri 				uri;
    private String 				mCurrentPhotoPath;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private ImageButton 		Take_Picture_Button;
    private ImageButton 		Send_Email_Button;
    private String 				status;
    private AlertDialog 		alert;

    int id;
    File file,file_new;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_report);

        status= "Report";
        setTitle(status);

        Take_Picture_Button  = (ImageButton) findViewById(R.id.imageButtonReportPicture);
        Send_Email_Button = (ImageButton) findViewById(R.id.imageButtonReport);

        Take_Picture_Button.getBackground().setAlpha(0);
        Send_Email_Button.getBackground().setAlpha(0);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO)
        {
            mAlbumStorageDirFactory = new IslandWatch_iCORE_TAMUCC_FroyoAlbumDirFactory();
        }
        else
        {
            mAlbumStorageDirFactory = new IslandWatch_iCORE_TAMUCC_BaseAlbumDirFactory();
        }
        Take_Picture_Button.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                if(isUserOnline())
                {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                }
                else
                {
                    ConnectionDialogBox1();
                }
            }
        });
        Send_Email_Button.setOnClickListener( new OnClickListener()
        {
            public void onClick(View v)
            {
                if(isUserOnline())
                {
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);

                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"UniversityPoliceFrontDesk@tamucc.edu"});
                    intent.putExtra(Intent.EXTRA_SUBJECT," Report from Island Watch App");
                    String message = String.format("\n\nThe Location: \n Longitude: %1$s \n Latitude: %2$s",longitude,latitude+"\n\n");
                    String message2 = "\n\nhttp://maps.google.com/maps?q="+latitude+","+longitude + message;
                    intent.putExtra(Intent.EXTRA_TEXT   , message2);

                    final PackageManager pm = getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);

                    ResolveInfo best = null;

                    for (final ResolveInfo info : matches)

                        if (best != null)
                            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                    try
                    {
                        startActivity(intent);
                    }
                    catch (android.content.ActivityNotFoundException ex)
                    {
                        Toast.makeText(IslandWatch_iCORE_TAMUCC_Report.this, "There are no email clients installed.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    ConnectionDialogBox1();
                }
            }
        });
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null)
        {
            latitude = (float) (location.getLatitude());
            longitude = (float) (location.getLongitude());
        }
        else
        {
            latitude = 0;
            longitude = 0;
        }

    }// onCreate

    public void Email_Function(Uri x)
    {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, x);
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"UniversityPoliceFrontDesk@tamucc.edu"});
        intent.putExtra(Intent.EXTRA_SUBJECT, " Report from Island Watch App");
        String message = String.format("\n\nThe Location: \n Longitude: %1$s \n Latitude: %2$s",longitude,latitude+"\n\n");
        String message2 = "\n\nhttp://maps.google.com/maps?q="+latitude+","+longitude + message;

        intent.putExtra(Intent.EXTRA_TEXT   , message2);

        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;

        for (final ResolveInfo info : matches)

            if (best != null)

                intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        try
        {
            startActivity(intent);
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(IslandWatch_iCORE_TAMUCC_Report.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 30000, 5, this);


    }

    @Override
    public void onPause()
    {
        super.onPause();
        locationManager.removeUpdates(this);

        if(!isUserOnline()){
            IslandWatch_iCORE_TAMUCC_Report.this.finish();
        }
    }

    public void onStop()
    {
        super.onStop();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTION_TAKE_PHOTO_B)
        {
            if(resultCode != RESULT_OK)
            {
                Toast.makeText(IslandWatch_iCORE_TAMUCC_Report.this, "Canceled", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                handleBigCameraPhoto();
            }
        }
    }
    //@Override
    public void onLocationChanged(Location location)
    {
        latitude  = (float)(location.getLatitude());
        longitude = (float)(location.getLongitude());
    }

    public void onProviderDisabled(String provider) {
        //Toast.makeText(this, "Disabled provider " + provider,Toast.LENGTH_SHORT).show();
    }

    public void onProviderEnabled(String provider) {
        //Toast.makeText(this, "Enabled new provider " + provider,Toast.LENGTH_SHORT).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private boolean isUserOnline()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void ConnectionDialogBox1()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("No Connection");
        builder.setMessage("You must have a network connection to report to UPD.")

                .setCancelable(false)

                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                IslandWatch_iCORE_TAMUCC_Report.this.finish();
                            }
                        }
                );

        alert = builder.create();
        alert.show();
    }

    /* Photo album for this application */
    private String getAlbumName()
    {
        return getString(R.string.album_name);
    }
    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d(getString(R.string.album_name), "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.appName), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }
    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp 		= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName 	= JPEG_FILE_PREFIX + timeStamp + "_";
        File   albumF 			= getAlbumDir();
        File   imageF 			= File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);

        return imageF;
    }

    private File setUpPhotoFile() throws IOException
    {

        File file 		  = createImageFile();
        mCurrentPhotoPath = file.getAbsolutePath();

        return file;
    }
    private void setPic()
    {
        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        /* Figure out which way needs to be reduced less */
        int scaleFactor = 2 ;

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        File file_new =  new File(mCurrentPhotoPath);

        try
        {
            outStream = new FileOutputStream(file_new);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream);
            outStream.flush();
            outStream.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }



    }
    private void galleryAddPic()
    {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void dispatchTakePictureIntent(int actionCode)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch(actionCode)
        {
            case ACTION_TAKE_PHOTO_B:
                File file = null;
                try
                {
                    file = setUpPhotoFile();
                    mCurrentPhotoPath = file.getAbsolutePath();
                    uri = Uri.fromFile(file);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    file = null;
                    mCurrentPhotoPath = null;
                }
                break;
            default:
                break;
        } // switch
        startActivityForResult(takePictureIntent, actionCode);
    }
    private void handleBigCameraPhoto()
    {
        if (mCurrentPhotoPath != null)
        {
            setPic();
            galleryAddPic();
            Email_Function(uri);
            mCurrentPhotoPath = null;
        }
    }
}

