package com.afollestad.seeMeCloudClient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;

import java.io.File;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int CAMERA_RQ = 6969;
    private final static int PERMISSION_RQ = 84;
    private static File saveDir = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // findViewById(R.id.launchCamera).setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
        }

        if(saveDir==null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
                saveDir.mkdirs();
            }
        }

        MaterialCamera materialCamera = new MaterialCamera(this)
                .saveDir(saveDir)
                .countdownImmediately(true)
                .showPortraitWarning(false)
                .autoSubmit(true)
                .allowRetry(false)
                .defaultToFrontFacing(false)
                .labelConfirm(R.string.mcam_use_video)
                .autoRecordWithDelayMs(1000)
                .countdownSeconds(13f);
        materialCamera.start(CAMERA_RQ);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onClick(View view) {
        File saveDir = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
            saveDir.mkdirs();
        }


        // Setting-1
        MaterialCamera materialCamera = new MaterialCamera(this)
                .saveDir(saveDir)
                .countdownImmediately(true)
                .showPortraitWarning(false)
                .allowRetry(true)
                .defaultToFrontFacing(false)
                .allowRetry(true)
                .autoSubmit(false)
                .labelConfirm(R.string.mcam_use_video)
                .autoRecordWithDelaySec(3)
                .countdownSeconds(15f);


        materialCamera.start(CAMERA_RQ);
    }

    private String readableFileSize(long size) {
        if (size <= 0) return size + " B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private String fileSize(File file) {
        return readableFileSize(file.length());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Added to support chunk videos
        MaterialCamera materialCamera = new MaterialCamera(this)
                .saveDir(saveDir)
                .countdownImmediately(true)
                .showPortraitWarning(false)
                .autoSubmit(true)
                .allowRetry(false)
                .defaultToFrontFacing(false)
                .labelConfirm(R.string.mcam_use_video)
                .autoRecordWithDelayMs(100)
                .countdownSeconds(13f);
        materialCamera.start(CAMERA_RQ);
/*
        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                final File file = new File(data.getData().getPath());
                Toast.makeText(this, String.format("Saved to: %s, size: %s",
                        file.getAbsolutePath(), fileSize(file)), Toast.LENGTH_LONG).show();
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // denied WRITE_EXTERNAL_STORAGE permission
            Toast.makeText(this, "WRITE_EXTERNAL_STORAGE permission denied.", Toast.LENGTH_LONG).show();
        }
    }
}
/*

    new MaterialCamera(this)                               // Constructor takes an Activity
        .allowRetry(true)                                  // Whether or not 'Retry' is visible during playback
        .autoSubmit(false)                                 // Whether or not user is allowed to playback videos after recording. This can affect other things, discussed in the next section.
        .saveDir(saveFolder)                               // The folder recorded videos are saved to
        .primaryColorAttr(R.attr.colorPrimary)             // The theme color used for the camera, defaults to colorPrimary of Activity in the constructor
        .showPortraitWarning(true)                         // Whether or not a warning is displayed if the user presses record in portrait orientation
        .defaultToFrontFacing(false)                       // Whether or not the camera will initially show the front facing camera
        .allowChangeCamera(true)                           // Allows the user to change cameras.
        .retryExits(false)                                 // If true, the 'Retry' button in the playback screen will exit the camera instead of going back to the recorder
        .restartTimerOnRetry(false)                        // If true, the countdown timer is reset to 0 when the user taps 'Retry' in playback
        .continueTimerInPlayback(false)                    // If true, the countdown timer will continue to go down during playback, rather than pausing.
        .videoEncodingBitRate(1024000)                     // Sets a custom bit rate for video recording.
        .audioEncodingBitRate(50000)                       // Sets a custom bit rate for audio recording.
        .videoFrameRate(24)                                // Sets a custom frame rate (FPS) for video recording.
        .qualityProfile(MaterialCamera.QUALITY_HIGH)       // Sets a quality profile, manually setting bit rates or frame rates with other settings will overwrite individual quality profile settings
        .videoPreferredHeight(720)                         // Sets a preferred height for the recorded video output.
        .videoPreferredAspect(4f / 3f)                     // Sets a preferred aspect ratio for the recorded video output.
        .maxAllowedFileSize(1024 * 1024 * 5)               // Sets a max file size of 5MB, recording will stop if file reaches this limit. Keep in mind, the FAT file system has a file size limit of 4GB.
        .iconRecord(R.drawable.mcam_action_capture)        // Sets a custom icon for the button used to start recording
        .iconStop(R.drawable.mcam_action_stop)             // Sets a custom icon for the button used to stop recording
        .iconFrontCamera(R.drawable.mcam_camera_front)     // Sets a custom icon for the button used to switch to the front camera
        .iconRearCamera(R.drawable.mcam_camera_rear)       // Sets a custom icon for the button used to switch to the rear camera
        .iconPlay(R.drawable.evp_action_play)              // Sets a custom icon used to start playback
        .iconPause(R.drawable.evp_action_pause)            // Sets a custom icon used to pause playback
        .iconRestart(R.drawable.evp_action_restart)        // Sets a custom icon used to restart playback
        .labelRetry(R.string.mcam_retry)                   // Sets a custom button label for the button used to retry recording, when available
        .labelConfirm(R.string.mcam_use_video)             // Sets a custom button label for the button used to confirm/submit a recording
        .autoRecordWithDelaySec(5)                         // The video camera will start recording automatically after a 5 second countdown. This disables switching between the front and back camera initially.
        .autoRecordWithDelayMs(5000)                       // Same as the above, expressed with milliseconds instead of seconds.
        .audioDisabled(false)                              // Set to true to record video without any audio.
        .start(CAMERA_RQ);                                 // Starts the camera activity, the result will be sent back to the current Activity

*/
