package com.arkenopterix.lumos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.content.Context;

public class MainActivity extends Activity {

    ImageButton btnSwitch;

    private CameraManager camera;

    private boolean isFlashOn;
    private boolean hasFlash;
    private String camId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // flash switch button
        btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
/*
 * First check if device is supporting flashlight or not
 */
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton(0,"OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }
        Log.d("init","starting");
        //get camera
        getCamera();
        Log.d("init","got camera !");

        //display image button
        toggleButtonImage();

        /*
    Switch click event to toggle flash on/off
     */
        btnSwitch.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    //turn off flash

                    try {
                        turnOffFlash();
                        Log.d("button","OFF");
                    } catch (CameraAccessException e) {
                            Log.d("Arg!",e.getMessage());
                    }
                } else {
                    try {
                        turnOnFlash();
                        Log.d("button","ON");
                    } catch (CameraAccessException e) {
                        Log.d("Arg!",e.getMessage());
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    try {
        // on pause turn off the flash
        turnOffFlash();
         } catch (CameraAccessException e) {

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // on resume turn on the flash
        if(hasFlash)
            try {
                turnOnFlash();
            } catch (CameraAccessException e) {

            }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            //camera.release();
            camera = null;
        }
    }

    /*
    Funtion that toggles button images
     */
    private void toggleButtonImage() {
        if (isFlashOn) {
            //turn off flash
            btnSwitch.setImageResource(R.drawable.flashlight_160x232_images_on);
        } else {
            // turn on flash
            btnSwitch.setImageResource(R.drawable.flashlight_160x232_images_off);
        }
    }


    // getting camera parameters
    private void getCamera() {
        if (camera == null) {
            try {
                camera = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                camId = camera.getCameraIdList()[0];
                //params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error.Error: ", e.getMessage());
                Log.d("Arg!",e.getMessage());
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.d("Arg!",e.getMessage());
            }
        }
    }

    /*
    * Turning On flash
     */
    private void turnOnFlash() throws CameraAccessException {
        if (!isFlashOn) {
            if (camera == null) {
                return;
            }
            // play sound
            //playSound();
            camera.setTorchMode(camId,true);

            isFlashOn = true;

            // change button image
            toggleButtonImage();

        }
    }

    /*
    Turning flashlight off
     */
    private void turnOffFlash() throws CameraAccessException {
        if(isFlashOn) {
            if (camera == null ) {
                return;
            }
            // playsound
            //playSound();

            camera.setTorchMode(camId,false);
            isFlashOn = false;

            // changing button image to off
            toggleButtonImage();
        }
    }

}
