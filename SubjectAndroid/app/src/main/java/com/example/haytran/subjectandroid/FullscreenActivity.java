package com.example.haytran.subjectandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.concurrent.TimeUnit;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    private static final String TAG = FullscreenActivity.class.getSimpleName();
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private View mContentView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout_row UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    ImageButton btnChangeActivity,btnPlayPause, btnSound , btnStop;
    SeekBar seekBar;
    VideoView videoView;
    TextView textViewVideoTitile, textViewCurrentTime, textViewMaxTime;
    Uri path;
    int currentPosition = 0;
    int videoDuration = 0;
    String filename = "";
    boolean isPlaying = true;
    boolean isMuted = false;
    private Handler threadHandler = new Handler();
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        mapping();
        init();
        addControls();
    }
    @Override
    protected void onDestroy() {
        Log.d(TAG,"On Destroy");
        Log.d(TAG,"Current Position:" + currentPosition);
        if (path != null) {
            MyVideo myVideo = new MyVideo(filename,videoDuration,currentPosition,path+"");
            dbHelper.insertOrUpdateVideo(myVideo);
        }
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"On Destroy");
        Log.d(TAG,"Current Position:" + currentPosition);
        videoView.pause();
        videoView.seekTo(currentPosition);
        videoView.start();
        textViewVideoTitile.setText(filename);
    }
    private void addControls() {
            // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
            // Auto hide after request show from user
        findViewById(R.id.contentView).setOnTouchListener(mDelayHideTouchListener);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
                videoView.seekTo(0);
                isPlaying = !isPlaying;
            }
        });
        btnChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullscreenActivity.this,MainActivity.class);
                intent.setFlags(105);
                intent.putExtra("path",path+"");
                intent.putExtra("currentPosition",videoView.getCurrentPosition());
                intent.putExtra("filename",filename);
                startActivity(intent);
                finish();
            }
        });
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    btnPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    videoView.pause();
                } else  {
                    btnPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
                    videoView.seekTo(videoView.getCurrentPosition());
                    videoView.start();
                }
                isPlaying = !isPlaying;
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.pause();
                videoView.seekTo(seekBar.getProgress());
                videoView.start();
            }
        });
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMuted = !isMuted;
                if (isMuted) {
                    AudioManager audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    try {
                        audioManager.adjustVolume(AudioManager.ADJUST_MUTE,0);
                    } catch (IllegalArgumentException ex) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                    }
                    btnSound.setImageResource(R.drawable.ic_volume_up_black_24dp);
                } else {
                    AudioManager audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    try {
                        audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE,0);
                    } catch (IllegalArgumentException ex) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                    }
                    btnSound.setImageResource(R.drawable.ic_volume_off_black_24dp);
                }
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoDuration = videoView.getDuration();
                Log.d(TAG,"duration = " + videoDuration);
                seekBar.setMax(videoDuration);
                textViewMaxTime.setText(millisecondsToString(videoDuration));
            }
        });

    }

    private void init() {
        mVisible = true;
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition",0);
        path = Uri.parse(intent.getStringExtra("path"));
        filename = intent.getStringExtra("filename");
        Log.d(TAG,"getIntent");
        Log.d(TAG,"Path: " +intent.getStringExtra("path").toString() );
        if(path != null){
            videoView.setVideoURI(path);
            videoView.seekTo(currentPosition);
            videoView.start();
            textViewVideoTitile.setText(filename);
            UpdateSeekBarThread updateSeekBarThread = new UpdateSeekBarThread();
            threadHandler.postDelayed(updateSeekBarThread,50);
        } else {
            Toast.makeText(this, "Ooh! Has a error", Toast.LENGTH_SHORT).show();
        }
        // Register for SQLite
        dbHelper = new DBHelper(FullscreenActivity.this);

    }
    private void mapping() {
        mControlsView = findViewById(R.id.contentControl);
        mContentView = findViewById(R.id.contentView);
        videoView = (VideoView)findViewById(R.id.videoView);
        btnChangeActivity = (ImageButton)findViewById(R.id.btnChangeActivity);
        btnStop = (ImageButton)findViewById(R.id.btnStop);
        btnPlayPause = (ImageButton)findViewById(R.id.btnPlayPause);
        btnSound = (ImageButton)findViewById(R.id.btnSound);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        textViewVideoTitile = (TextView)findViewById(R.id.textViewVideoTitle);
        textViewCurrentTime = (TextView)findViewById(R.id.textViewCurrentTime);
        textViewMaxTime = (TextView)findViewById(R.id.textViewMaxTime);

    }
        // Thread used to update for seekbar
    class UpdateSeekBarThread implements Runnable {
        public void run()  {
            if (videoView.getCurrentPosition() > 0) {
                currentPosition = videoView.getCurrentPosition();
            }
            Log.d(TAG,"Current Position: " + currentPosition);
            seekBar.setProgress(videoView.getCurrentPosition());
            textViewCurrentTime.setText(millisecondsToString(videoView.getCurrentPosition()));
            threadHandler.postDelayed(this, 50);
        }
    }
    // Convert milisecond to time
    private String millisecondsToString(int milliseconds)  {
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds =  TimeUnit.MILLISECONDS.toSeconds((long) milliseconds - (minutes * 60000)) ;
        return minutes+":"+ seconds;
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
            // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

            // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
            // Show the system bar
        videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

            // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
